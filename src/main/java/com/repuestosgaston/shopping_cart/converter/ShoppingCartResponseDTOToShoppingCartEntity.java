package com.repuestosgaston.shopping_cart.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;

import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

public class ShoppingCartResponseDTOToShoppingCartEntity implements Converter<ShoppingCartResponseDTO, ShoppingCartEntity> {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Nullable
	public ShoppingCartEntity convert(ShoppingCartResponseDTO source) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));
		return null;
	}


}
