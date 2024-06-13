package com.repuestosgaston.shopping_cart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.shopping_cart.controller.dto.RequestAddProductDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.converter.SaleOrderEntityToSaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.model.enums.SaleOrderStatusEnum;
import com.repuestosgaston.shopping_cart.repository.SaleOrderRepository;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class SaleOrderService {

	private final SaleOrderEntityToSaleOrderResponseDTO saleOrderEntityToSaleOrderResponseDTO;
	private final ShoppingCartService shoppingCartService;
	private final SaleOrderRepository saleOrderRepository;
	private final ShoppingCartRepository shoppingCartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	public SaleOrderService(SaleOrderRepository saleOrderRepository, ShoppingCartRepository shoppingCartRepository, ModelMapper modelMapper, SaleOrderEntityToSaleOrderResponseDTO saleOrderEntityToSaleOrderResponseDTO, UserRepository userRepository,ProductRepository productRepository, ShoppingCartService shoppingCartService) {
		this.saleOrderRepository = saleOrderRepository;
		this.shoppingCartRepository = shoppingCartRepository;
		this.shoppingCartService = shoppingCartService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.saleOrderEntityToSaleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO;
		this.productRepository = productRepository;
	}

	public Page<SaleOrderResponseDTO> getAllOrders(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return saleOrderRepository.findAll(pageable)
				.map(saleOrderEntityToSaleOrderResponseDTO::convert);
	}
	
	public SaleOrderResponseDTO getOrderById(Long saleOrderId) {
		return saleOrderRepository.findById(saleOrderId)
				.map(saleOrderEntityToSaleOrderResponseDTO::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Shopping Cart [%s] not found", saleOrderId)));

	}
	
	public SaleOrderResponseDTO getOrderByNumberSale(Integer numberSale) {
		Optional<SaleOrderEntity>  saleOrderEntity = saleOrderRepository.findByNumberSale(numberSale);
		return saleOrderEntity
				.map(saleOrderEntityToSaleOrderResponseDTO::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Shopping Cart [%s] not found", numberSale)));
	}

	public SaleOrderResponseDTO createSaleOrderUser(String username) {
		UserEntity user = userRepository.findByUsername(username).get();
		ShoppingCartEntity shoppingCartEntity = user.getCart();
		SaleOrderEntity saleOrderEntity = createOder(shoppingCartEntity); 
		saleOrderRepository.save(saleOrderEntity);
		SaleOrderResponseDTO saleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO.convert(saleOrderEntity);
		
		return saleOrderResponseDTO;
	}

	public SaleOrderEntity createOder(ShoppingCartEntity shoppingCartEntity) {
		SaleOrderEntity saleOrderEntity = new SaleOrderEntity();
		
		saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PENDING_PAYMENT);
		saleOrderEntity.setShoppingCart(shoppingCartEntity);
		saleOrderEntity.setNumberSale(generateNumberSale(1000000000,2147483647));
		return saleOrderEntity;
	}
	
	@Transactional
	public SaleOrderResponseDTO createSaleOrderAdmin(List<RequestAddProductDTO> productsRequest) {
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		
		List<ProductEntity> products = getListProducts(productsRequest);
		
		Integer numberCart = shoppingCartService.generateNumberSale(10000,99999);
		Double totalPriceCart = calculateTotalPrice(products);
		
		shoppingCartEntity.setProducts(products);
		shoppingCartEntity.setNumberCart(numberCart);
		shoppingCartEntity.setTotalPrice(totalPriceCart);
		
		SaleOrderEntity saleOrderEntity = createOder(shoppingCartEntity); 
		saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PAID);
		shoppingCartRepository.save(shoppingCartEntity);
		saleOrderRepository.save(saleOrderEntity);
		
		SaleOrderResponseDTO saleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO.convert(saleOrderEntity);
		
		return saleOrderResponseDTO;
	}
	
	public List<ProductEntity> getListProducts(List<RequestAddProductDTO> productsRequest){
		List<ProductEntity> products = new ArrayList<>();
		
		for (RequestAddProductDTO productRequest : productsRequest) {
				Optional<ProductEntity> product = productRepository.findById(productRequest.getIdProduct());
				products.add(product.get());
				product.get().setAmountZero();
				int newStock = product.get().getStock() - productRequest.getAmount();
				double subTotalPrice = product.get().getPrice() * productRequest.getAmount();
				product.get().setStock(newStock);
				product.get().setSub_total_price(subTotalPrice);
				product.get().setAmount(productRequest.getAmount());
				productRepository.save(product.get());
		}
		
		return products;
	}
	
	public Integer generateNumberSale(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }

	public void updateSaleOrder(Long orderId,SaleOrderRequestDTO saleOrder) {
		SaleOrderEntity saleOrderEntity = saleOrderRepository.findById(orderId).get();
		if (saleOrderEntity==null) {
			throw new IllegalArgumentException(
					String.format("Sale Order [%s] not found", orderId));
		}

		modelMapper.map(saleOrder, SaleOrderEntity.class);
		//Logica para modificar el saleOrder en base al saleOrder que recibe
		saleOrderRepository.save(saleOrderEntity);
	}
	
	public void deleteSaleOrderById(Long orderId) {
		saleOrderRepository.deleteById(orderId);
	}

	public SaleOrderEntity updateSaleOrderStatus(Long orderId, String status) {
		SaleOrderEntity saleOrderEntity = saleOrderRepository.findById(orderId).get();
		parseStatus(saleOrderEntity, status);
		return saleOrderRepository.save(saleOrderEntity);
	}

	public void parseStatus(SaleOrderEntity saleOrderEntity, String status) {
		if (status.equals(SaleOrderStatusEnum.PAID.getName())) {
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PAID);
		} else if(status.equals(SaleOrderStatusEnum.REJECTED.getName())){
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.REJECTED);
		}
	}
	
	public Double calculateTotalPrice(List<ProductEntity> products) {
		return products.stream().mapToDouble(ProductEntity::getSub_total_price).sum();
	}
	
}
