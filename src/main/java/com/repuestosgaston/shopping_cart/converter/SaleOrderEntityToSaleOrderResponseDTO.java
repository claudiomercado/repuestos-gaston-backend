package com.repuestosgaston.shopping_cart.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

@Component
public class SaleOrderEntityToSaleOrderResponseDTO implements Converter<SaleOrderEntity, SaleOrderResponseDTO>{

	@Autowired
	ShoppingCartEntityToShoppingCartResponseDTO cartEntityToShoppingCartResponseDTO;
	
	private final UserRepository userRepository;

    SaleOrderEntityToSaleOrderResponseDTO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	
	@Override
	public SaleOrderResponseDTO convert(SaleOrderEntity saleOrderEntity) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepository.findByUsername(username).get();
		
		SaleOrderResponseDTO saleOrderResponseDTO = new SaleOrderResponseDTO();
		saleOrderResponseDTO.setId(saleOrderEntity.getId());
		saleOrderResponseDTO.setNumberSale(saleOrderEntity.getNumberSale());
		saleOrderResponseDTO.setSaleStatus(saleOrderEntity.getSaleStatus());
		saleOrderResponseDTO.setShoppingCart(cartEntityToShoppingCartResponseDTO.convert(saleOrderEntity.getShoppingCart()));
		saleOrderResponseDTO.setName(user.getName());
		saleOrderResponseDTO.setSurname(user.getSurname());
		saleOrderResponseDTO.setDni(user.getDni());
	
		return saleOrderResponseDTO;
	}

	
	
	
}
