package com.repuestosgaston.users.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.users.controller.dto.UserResponseAdminDTO;
import com.repuestosgaston.users.model.UserEntity;

@Component
public class UserEntityToUserResponseAdminConverter implements Converter<UserEntity, UserResponseAdminDTO>{

	@Override
	public UserResponseAdminDTO convert(UserEntity userEntity) {
		UserResponseAdminDTO dto = new UserResponseAdminDTO();
		dto.setId(userEntity.getId());
		dto.setUsername(userEntity.getUsername());
		dto.setBirthdate(userEntity.getBirthdate());
		dto.setDni(userEntity.getDni());
		dto.setEmail(userEntity.getEmail());
		dto.setName(userEntity.getName());
		dto.setSurname(userEntity.getSurname());
		
		return dto;
	}
}
