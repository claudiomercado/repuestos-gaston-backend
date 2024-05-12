package com.repuestosgaston.users.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductCartResponseDTO;
import com.repuestosgaston.products.converter.ProductCartToShoppingCartResponse;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.users.controller.dto.UserResponseAdminDTO;
import com.repuestosgaston.users.model.UserEntity;

@Component
public class UserEntityToUserResponseAdminConverter implements Converter<UserEntity, UserResponseAdminDTO>{

	@Override
	public UserResponseAdminDTO convert(UserEntity userEntity) {
		UserResponseAdminDTO dto = new UserResponseAdminDTO();
		dto.setUsername(userEntity.getUsername());
		dto.setBirthdate(userEntity.getBirthdate());
		dto.setDni(userEntity.getDni());
		dto.setEmail(userEntity.getEmail());
		dto.setName(userEntity.getName());
		dto.setSurname(userEntity.getSurname());
		
		return dto;
	}
}
