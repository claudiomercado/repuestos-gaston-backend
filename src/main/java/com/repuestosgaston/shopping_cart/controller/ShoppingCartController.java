package com.repuestosgaston.shopping_cart.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.service.ShoppingCartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/shopping_cart")
public class ShoppingCartController {
	private final ShoppingCartService shoppingCartService;

	public ShoppingCartController(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	@GetMapping(path = "/")
	public ResponseEntity<Page<ShoppingCartResponseDTO>> getAllShoppingCart(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(shoppingCartService.getAllShoppingCart(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("ShoppingCartController.getAllShoppingCart - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{shoppingCartId}")
	public ResponseEntity<ShoppingCartResponseDTO> getShoppingCartById(Long shoppingCartId){
		try {
			return ResponseEntity.ok().body(shoppingCartService.getShoppingCartById(shoppingCartId));
		} catch (Exception e) {
			log.error(String.format("ShoppingCartController.getShoppingCartById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(path = "/")
	public ResponseEntity<Object> createShoppingCart(@RequestBody ShoppingCartRequestDTO shoppingCartRequestDTO){
		try {
			shoppingCartService.createShoppingCart(shoppingCartRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("ShoppingCartController.createShoppingCart - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/{shoppingCart_id}")
	public ResponseEntity<ShoppingCartEntity> updateShoppingCart(@PathVariable Long shoppingCart_id,@RequestBody ShoppingCartRequestDTO shoppingCart){
		try {
			shoppingCartService.updateShoppingCart(shoppingCart_id,shoppingCart);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("ShoppingCartController.updateShoppingCart - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<ShoppingCartEntity> deleteShoppingCartById(Long id){
		try {
			shoppingCartService.deleteShoppingCartById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error(String.format("ShoppingCartController.deleteShoppingCartById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
}
