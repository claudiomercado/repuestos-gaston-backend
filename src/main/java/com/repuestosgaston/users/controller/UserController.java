package com.repuestosgaston.users.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repuestosgaston.users.controller.dto.UserRequestDTO;
import com.repuestosgaston.users.controller.dto.UserResponseDTO;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/user")
public class UserController {
	private final UserService userService;


	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/")
	public ResponseEntity<Page<UserResponseDTO>> getAllUser(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(userService.getAllUser(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("UserController.getAllUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{userId}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") Long userId){
		try {
			return ResponseEntity.ok().body(userService.getUserById(userId));
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.getUserById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@PostMapping(path = "/createUser")
	public ResponseEntity<Object> createUser(@RequestBody UserRequestDTO userRequestDTO){
		try {
			userService.createUser(userRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("UserController.createUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/")
	public ResponseEntity<UserEntity> updateUser(@RequestBody UserRequestDTO userRequestDTO){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.updateUser(username, userRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.updateUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<UserEntity> deleteUser(){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.deleteUser(username);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.deleteUserById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
}
