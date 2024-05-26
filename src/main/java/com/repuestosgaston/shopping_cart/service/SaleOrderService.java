package com.repuestosgaston.shopping_cart.service;

import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.converter.SaleOrderEntityToSaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.model.enums.SaleOrderStatusEnum;
import com.repuestosgaston.shopping_cart.repository.SaleOrderRepository;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class SaleOrderService {

	private final SaleOrderEntityToSaleOrderResponseDTO saleOrderEntityToSaleOrderResponseDTO;
	private final SaleOrderRepository saleOrderRepository;
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	public SaleOrderService(SaleOrderRepository saleOrderRepository, ModelMapper modelMapper, SaleOrderEntityToSaleOrderResponseDTO saleOrderEntityToSaleOrderResponseDTO, UserRepository userRepository) {
		this.saleOrderRepository = saleOrderRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.saleOrderEntityToSaleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO;
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

	public SaleOrderResponseDTO createSaleOrder(String username) {
		UserEntity user = userRepository.findByUsername(username).get();
		ShoppingCartEntity shoppingCartEntity = user.getCart();
		SaleOrderEntity saleOrderEntity = createOder(user, shoppingCartEntity); 
		saleOrderRepository.save(saleOrderEntity);
		SaleOrderResponseDTO saleOrderResponseDTO = saleOrderEntityToSaleOrderResponseDTO.convert(saleOrderEntity);
		
		return saleOrderResponseDTO;
	}

	public SaleOrderEntity createOder(UserEntity user, ShoppingCartEntity shoppingCartEntity) {
		SaleOrderEntity saleOrderEntity = new SaleOrderEntity();
		
		saleOrderEntity.setSaleStatus(SaleOrderStatusEnum.PENDING_PAYMENT);
		saleOrderEntity.setShoppingCart(shoppingCartEntity);
		saleOrderEntity.setNumberSale(generateNumberSale(1000000000,2147483647));
		return saleOrderEntity;
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
		
}
