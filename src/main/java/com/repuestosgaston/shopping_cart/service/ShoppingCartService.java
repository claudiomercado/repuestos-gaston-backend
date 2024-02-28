package com.repuestosgaston.shopping_cart.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;
import com.repuestosgaston.users.service.UserService;

@Service
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository,
			ProductRepository productRepository, ModelMapper modelMapper) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}

	//Ver si sigue
	public Page<ShoppingCartResponseDTO> getAllShoppingCart(int page, int size, String sort, String sortDirection) {
		Sort sorter = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
		Pageable pageable = PageRequest.of(page, size, sorter);

		return shoppingCartRepository.findAll(pageable)
				.map(shoppingCart -> modelMapper.map(shoppingCart, ShoppingCartResponseDTO.class));
	}

	//Ver si sigue
	public ShoppingCartResponseDTO getShoppingCartById(Long shoppingCartId) {
		var shoppingCartEntity = shoppingCartRepository.findById(shoppingCartId);
		if (!shoppingCartEntity.isPresent()) {
			throw new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCartId));
		}
		return modelMapper.map(shoppingCartEntity.get(), ShoppingCartResponseDTO.class);
	}
	
	public ShoppingCartEntity createShoppingCart() {
//		var userEntity = userRepository.findByUsername(username);
//		if (!userEntity.isPresent()) {
//				throw new IllegalArgumentException(String.format("El usuario '[%s]' no existe", username));
//			}
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		shoppingCartEntity.setTotalPrice(0.0);
//		shoppingCartEntity.setUser(userEntity.get());
		
		return shoppingCartRepository.save(shoppingCartEntity);
	}

	public void addProducts(String username,Long idProduct, Integer amount) {
		var userEntity = userRepository.findByUsername(username);
		if (!userEntity.isPresent()) {
			throw new IllegalArgumentException(String.format("El usuario '[%s]' no existe", username));
		}
		Optional<ShoppingCartEntity> cartEntity = shoppingCartRepository.findById(userEntity.get().getCart().getId());
		
		var product = productRepository.findById(idProduct);
		if (!product.isPresent()) {
			throw new IllegalArgumentException(String.format("El producto no existe"));
		}
		
		List<ProductEntity> products = cartEntity.get().getProducts();
		
		for (int i = 0; i < amount; i++) {
			products.add(product.get());
			}
		
		cartEntity.get().setTotalPrice(addPrice(products));
		cartEntity.get().setProducts(products);
		
		shoppingCartRepository.save(cartEntity.get());
		
	}

	private Double addPrice(List<ProductEntity> products) {
		Double totalPrice = 0.0;
		for (ProductEntity productEntity : products) {
			totalPrice += productEntity.getPrice();
		}
		return totalPrice;
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
