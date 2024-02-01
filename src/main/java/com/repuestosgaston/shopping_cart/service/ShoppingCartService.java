package com.repuestosgaston.shopping_cart.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;

	private final ModelMapper modelMapper;

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ModelMapper modelMapper) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.modelMapper = modelMapper;
	}

	public Page<ShoppingCartResponseDTO> getAllShoppingCart(int page, int size, String sort, String sortDirection) {
		Sort sorter = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
		Pageable pageable = PageRequest.of(page, size, sorter);

		return shoppingCartRepository.findAll(pageable)
				.map(shoppingCart -> modelMapper.map(shoppingCart, ShoppingCartResponseDTO.class));
	}

	public ShoppingCartResponseDTO getShoppingCartById(Long shoppingCartId) {
		var shoppingCartEntity = shoppingCartRepository.findById(shoppingCartId);
		if (!shoppingCartEntity.isPresent()) {
			throw new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCartId));
		}
		return modelMapper.map(shoppingCartEntity.get(), ShoppingCartResponseDTO.class);
	}

	// Recibe un ShoppingCartRequestDTO
	public void createShoppingCart(ShoppingCartRequestDTO shoppingCartRequestDTO) {
		ShoppingCartEntity shoppingCartEntity = modelMapper.map(shoppingCartRequestDTO, ShoppingCartEntity.class);
		shoppingCartRepository.save(shoppingCartEntity);
	}

	public void updateShoppingCart(Long shoppingCart_id, ShoppingCartRequestDTO shoppingCart) {
		ShoppingCartEntity shoppingCartEntity = shoppingCartRepository.findById(shoppingCart_id).get();
		if (shoppingCartEntity == null) {
			throw new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCart_id));
		}

		modelMapper.map(shoppingCart, ShoppingCartEntity.class);
		// Logica para modificar el shoppingCart en base al shoppingCart que recibe
		shoppingCartRepository.save(shoppingCartEntity);
	}

	public void deleteShoppingCartById(Long id) {
		shoppingCartRepository.deleteById(id);
	}
}
