package com.repuestosgaston.shopping_cart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.products.service.ProductService;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderStatusRequestDTO;
import com.repuestosgaston.shopping_cart.converter.SaleOrderEntityToSaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.ProductSaleEntity;
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
	private final ProductService productService;
	private final SaleOrderRepository saleOrderRepository;
	private final ShoppingCartRepository shoppingCartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	public SaleOrderService(ProductService productService, SaleOrderRepository saleOrderRepository, ShoppingCartRepository shoppingCartRepository, ModelMapper modelMapper, SaleOrderEntityToSaleOrderResponseDTO saleOrderEntityToSaleOrderResponseDTO, UserRepository userRepository,ProductRepository productRepository, ShoppingCartService shoppingCartService) {
		this.saleOrderRepository = saleOrderRepository;
		this.productService = productService;
		this.shoppingCartRepository = shoppingCartRepository;
		this.shoppingCartService = shoppingCartService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.saleOrderEntityToSaleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO;
		this.productRepository = productRepository;
	}

	@Transactional
	public Page<SaleOrderResponseDTO> getAllOrders(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return saleOrderRepository.findAll(pageable)
				.map(saleOrderEntityToSaleOrderResponseDTO::convert);
	}
	
	@Transactional
	public SaleOrderResponseDTO getOrderById(Long saleOrderId) {
		return saleOrderRepository.findById(saleOrderId)
				.map(saleOrderEntityToSaleOrderResponseDTO::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Shopping Cart [%s] not found", saleOrderId)));

	}
	
	@Transactional
	public Page<SaleOrderResponseDTO> getOrderByNumberSale(Integer numberSale, int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
		return saleOrderRepository.findByNumberSale(pageable,numberSale)
				.map(saleOrderEntityToSaleOrderResponseDTO::convert);
	}

	@Transactional
	public SaleOrderResponseDTO createSaleOrder(String username) {
		UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

	    ShoppingCartEntity shoppingCartEntity = shoppingCartService.getShoppingCartByIdLong(userEntity.getCart().getId());
	    
	    SaleOrderEntity saleOrderEntity = buildOrderSale(shoppingCartEntity);
	    saleOrderEntity.setDni(userEntity.getDni());
	    saleOrderEntity.setName(userEntity.getName());
	    saleOrderEntity.setSurname(userEntity.getSurname());
	    saleOrderEntity.setNumberSale(generateNumberSale(1000000000,2147483647));
	    if (userEntity.getUsername().equals("admin")) {
	    	saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.DELIVERED);
		}else {
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PAID);
		}
	    	    saleOrderRepository.save(saleOrderEntity);

	    // Vaciar carrito
	    shoppingCartEntity.getProducts().clear();
	    shoppingCartRepository.save(shoppingCartEntity);

	    SaleOrderResponseDTO saleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO.convert(saleOrderEntity);
	    return saleOrderResponseDTO;
	}
	
	private SaleOrderEntity buildOrderSale(ShoppingCartEntity shoppingCartEntity) {
		SaleOrderEntity saleOrderEntity = new SaleOrderEntity();
		List<ProductSaleEntity> products = new ArrayList<>();
		
		for (ProductEntity product : shoppingCartEntity.getProducts()) {
	        ProductSaleEntity productSale = new ProductSaleEntity();
	        productSale.setSaleOrder(saleOrderEntity);
	        productSale.setProduct(product);
	        productSale.setQuantity(product.getAmount());
	        productSale.setSubTotalPrice(product.getPrice() * product.getAmount());
	        products.add(productSale);
	        // Actualiza el stock del producto
	        product.setStock(product.getStock() - product.getAmount());
	        product.setAmountZero();
	        product.setSubTotalPriceZero();
	        productRepository.save(product);
	    }
		saleOrderEntity.setProductSale(products);
		return saleOrderEntity;
	}
	
	public Integer generateNumberSale(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }

	@Transactional
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
	
	@Transactional
	public void deleteSaleOrderById(Long orderId) {
		saleOrderRepository.deleteById(orderId);
	}

	@Transactional
	public SaleOrderEntity updateSaleOrderStatus(Long orderId, SaleOrderStatusRequestDTO status) {
		SaleOrderEntity saleOrderEntity = saleOrderRepository.findById(orderId).get();
		parseStatus(saleOrderEntity, status);
		return saleOrderRepository.save(saleOrderEntity);
	}

	public void parseStatus(SaleOrderEntity saleOrderEntity, SaleOrderStatusRequestDTO status) {
		saleOrderEntity.setSaleStatus(status.getSaleStatus());
		/*if (status.equals(SaleOrderStatusEnum.PAID.getName())) {
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PAID);
		} else if(status.equals(SaleOrderStatusEnum.REJECTED.getName())){
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.REJECTED);
		} else if(status.equals(SaleOrderStatusEnum.DELIVERED.getName())) {
			saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.DELIVERED);
		}*/
	}
	
	public Double calculateTotalPrice(List<ProductEntity> products) {
		return products.stream().mapToDouble(ProductEntity::getSub_total_price).sum();
	}
	
}
