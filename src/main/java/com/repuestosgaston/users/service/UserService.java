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

import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.shopping_cart.service.ShoppingCartService;
import com.repuestosgaston.users.controller.dto.UserRequestDTO;
import com.repuestosgaston.users.controller.dto.UserResponseDTO;
import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final ShoppingCartService shoppingCartService;
	
	private final ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, ShoppingCartService shoppingCartService ,ModelMapper modelMapper) {
		this.shoppingCartService = shoppingCartService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	public Page<UserResponseDTO> getAllUser(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return userRepository.findAll(pageable)
				.map(user -> modelMapper.map(user, UserResponseDTO.class));
	}
	
	public UserResponseDTO getUserById(Long userId) {
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
				String.format("User [%s] not found", userId));
			}
		return modelMapper.map(userEntity, UserResponseDTO.class);	
	}

	//Recibe un UserRequestDTO
	public void createUser(UserRequestDTO userRequestDTO) {
		UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);

		//rawPassword -> obtiene la contraseña sin decodificar
		String rawPassword = userRequestDTO.getPassword(); // Asegúrate de obtener la contraseña del DTO
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		userEntity.setRoles(this.createRole());
		userEntity.setCart(shoppingCartService.createShoppingCart());
		//userEntity.setCart(shoppingCartService.createShoppingCart(userRequestDTO.getUsername()));
	    userEntity.setPassword(encodedPassword);
	    
		userRepository.save(userEntity);
	}

	public void updateUser(String username, UserRequestDTO userRequestDTO) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(username);
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("User [%s] not found", username));
		}		
		//Validar que el username,mail,dni que edita no coincida con ningun username de la base de datos
		
		userEntity.get().setEmail(userRequestDTO.getEmail() != null ? userRequestDTO.getEmail() : userEntity.get().getEmail());
		userEntity.get().setUsername(userRequestDTO.getUsername() != null ? userRequestDTO.getUsername() : userEntity.get().getUsername());
		userEntity.get().setPassword(userRequestDTO.getPassword() != null ? userRequestDTO.getPassword() : userEntity.get().getPassword());
		userEntity.get().setName(userRequestDTO.getName() != null ? userRequestDTO.getName() : userEntity.get().getName());
		userEntity.get().setSurname(userRequestDTO.getSurname() != null ? userRequestDTO.getSurname() : userEntity.get().getSurname());
		userEntity.get().setDni(userRequestDTO.getDni() != null ? userRequestDTO.getDni() : userEntity.get().getDni());
		userEntity.get().setBirthdate(userRequestDTO.getBirthdate() != null ? userRequestDTO.getBirthdate() : userEntity.get().getBirthdate());
		
		userRepository.save(userEntity.get());
	}
	
	public void deleteUser(String username) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(username);
		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("User [%s] not found", username));
		}	
		userRepository.deleteById(userEntity.get().getId());
	}
	
	public Set<RoleEntity> createRole(){
		Set<RoleEntity> roles = new HashSet<>();
		RoleEntity rolEntity = new RoleEntity();
		rolEntity.setName(RoleEnum.USER);	
		roles.add(rolEntity);
	    return roles;
	}


}
