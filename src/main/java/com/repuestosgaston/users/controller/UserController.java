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

import com.repuestosgaston.users.controller.dto.UserRequestCreateDTO;
import com.repuestosgaston.users.controller.dto.UserRequestUpdateDTO;
import com.repuestosgaston.users.controller.dto.UserResponseAdminDTO;
import com.repuestosgaston.users.controller.dto.UserResponseDTO;
import com.repuestosgaston.users.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/users")
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/")
	public ResponseEntity<Page<UserResponseAdminDTO>> getAllUser(
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
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable("id") Long userId){
		try {
			return ResponseEntity.ok().body(userService.getUserById(userId));
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.getUserById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping(path = "/dni/{dni}")
	public ResponseEntity<Page<UserResponseAdminDTO>> getUserByDni(
			@PathVariable("dni") String userDni,
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(userService.getUserByDni(userDni,page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("UserController.getUserByDni - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/profile")
	public ResponseEntity<UserResponseDTO> getUser(){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			return ResponseEntity.ok().body(userService.getUser(username));
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.getUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@PostMapping(path = "/")
	public ResponseEntity<Object> createUser(@RequestBody UserRequestCreateDTO userRequestDTO){
		try {
			userService.createUser(userRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("UserController.createUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/")
	public ResponseEntity<Object> updateUser(@RequestBody UserRequestUpdateDTO userRequestDTO){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.updateUser(username, userRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.updateUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<Object> deleteUser(){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.deleteUser(username);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.deleteUser - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Object> deleteUserById(@PathVariable("id") Long userId){
		try {
			userService.deleteUserById(userId);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("UserController.deleteUserById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
}
