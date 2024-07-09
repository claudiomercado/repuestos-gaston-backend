package com.repuestosgaston.users.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.shopping_cart.service.ShoppingCartService;
import com.repuestosgaston.users.controller.dto.UserRequestCreateDTO;
import com.repuestosgaston.users.controller.dto.UserRequestUpdateDTO;
import com.repuestosgaston.users.controller.dto.UserResponseAdminDTO;
import com.repuestosgaston.users.controller.dto.UserResponseDTO;
import com.repuestosgaston.users.convert.UserEntityToUserResponseAdminConverter;
import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final ShoppingCartService shoppingCartService;
	private final RolService rolService;
	private final UserEntityToUserResponseAdminConverter converterUserResponseAdmin;
	private final ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, 
			ShoppingCartService shoppingCartService ,
			ModelMapper modelMapper,
			UserEntityToUserResponseAdminConverter converterUserResponseAdmin,
			RolService rolService) {
		this.shoppingCartService = shoppingCartService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.converterUserResponseAdmin = converterUserResponseAdmin;
		this.rolService = rolService;
	}

	public Page<UserResponseAdminDTO> getAllUser(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return userRepository.findAll(pageable)
				.map(converterUserResponseAdmin::convert);
	}
	
	public UserResponseAdminDTO getUserById(Long userId) {
		return userRepository.findById(userId)
				.map(converterUserResponseAdmin::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Usuario [%s] no encontrado", userId)));
	}
	
	public Page<UserResponseAdminDTO> getUserByDni(String userDni,int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return userRepository.findByDni(pageable,userDni)
				.map(converterUserResponseAdmin::convert);
	}
	
	public UserResponseDTO getUser(String username) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(username);
		
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
				String.format("User [%s] not found", username));
			}
		return modelMapper.map(userEntity, UserResponseDTO.class);	
	}

	@Transactional
	public void createUser(UserRequestCreateDTO userRequestDTO) {
		UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);																											
		RoleEntity roleEntity = rolService.createRoleUser();
		Set<RoleEntity> roles = new HashSet<>();
		roles.add(roleEntity);

		String password = userRequestDTO.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		userEntity.setPassword(encodedPassword);
		userEntity.setCart(shoppingCartService.createShoppingCart());
		userEntity.setRoles(roles);
		userRepository.save(userEntity);
	}
	
	@Transactional
	public void createAdmin(UserRequestCreateDTO userRequestDTO) {
		UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);																											
		RoleEntity roleEntity = rolService.createRoleAdmin();
		Set<RoleEntity> roles = new HashSet<>();
		roles.add(roleEntity);
		
		String password = userRequestDTO.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		userEntity.setPassword(encodedPassword);
		userEntity.setCart(shoppingCartService.createShoppingCart());
		userEntity.setRoles(roles);
		userRepository.save(userEntity);
	}

	public void updateUser(String username, UserRequestUpdateDTO userRequestDTO) {
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

		updateFields(userEntity, userRequestDTO);
		userRepository.save(userEntity);
	}
	
	private void updateFields(UserEntity userEntity, UserRequestUpdateDTO userRequestDTO) {
	    if (userRequestDTO.getPassword() != null) {
	    	String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
			userEntity.setPassword(encodedPassword);
	    }
	    if (userRequestDTO.getName() != null) {
	        userEntity.setName(userRequestDTO.getName());
	    }
	    if (userRequestDTO.getSurname() != null) {
	        userEntity.setSurname(userRequestDTO.getSurname());
	    }
	    if (userRequestDTO.getBirthdate() != null) {
	        userEntity.setBirthdate(userRequestDTO.getBirthdate());
	    }
	}
	
	public void deleteUser(String username) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(username);
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("User [%s] not found", username));
		}	
		userRepository.deleteById(userEntity.get().getId());
	}
	
	public void deleteUserById(Long userId) {
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("User [%s] not found", userId));
		}	
		userRepository.deleteById(userEntity.get().getId());
	}
	
}
