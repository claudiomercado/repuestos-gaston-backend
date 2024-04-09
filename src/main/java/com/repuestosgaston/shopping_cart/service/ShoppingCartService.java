package com.repuestosgaston.shopping_cart.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.shopping_cart.controller.dto.RequestAddProductDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.users.repository.UserRepository;

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
		Optional<ShoppingCartEntity> shoppingCartEntity = shoppingCartRepository.findById(shoppingCartId);
		if (!shoppingCartEntity.isPresent()) {
			throw new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCartId));
		}
		return modelMapper.map(shoppingCartEntity.get(), ShoppingCartResponseDTO.class);
	}
	
	public ShoppingCartEntity createShoppingCart() {
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		shoppingCartEntity.setTotalPrice(0.0);
		//shoppingCartEntity.setNumberCart(1471);
		
		return shoppingCartRepository.save(shoppingCartEntity);
	}

	public void addProducts(String username,RequestAddProductDTO requestAddProductDTO) {
		var userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

		var idCart = userEntity.getCart().getId();
		Optional<ShoppingCartEntity> cartEntity = shoppingCartRepository.findById(idCart);

		 var product = productRepository.findById(requestAddProductDTO.getIdProduct())
				.orElseThrow(() -> new IllegalArgumentException(String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));
		
		if (requestAddProductDTO.getAmount() < product.getStock()) {
			throw new IllegalArgumentException(String.format("Insufficient amount [%s]",requestAddProductDTO.getAmount()));
		}
		List<ProductEntity> products = cartEntity.get().getProducts();
		
		for (int i = 0; i < requestAddProductDTO.getAmount(); i++) {
			products.add(product);
			}
		//Descontar stock de productos llamando al servicio de productos
		//Implementar misma funcionalidad pero para quitar productos del carrito
		//Funcion que vacie el carrito
		
		cartEntity.get().setTotalPrice(calculateTotalPrice(products));
		cartEntity.get().setProducts(products);		
		shoppingCartRepository.save(cartEntity.get());
	}

	private Double calculateTotalPrice(List<ProductEntity> products) {
		Double totalPrice = 0.0;
		for (ProductEntity productEntity : products) {
			totalPrice += productEntity.getPrice();
		}
		return totalPrice;
	}


	public void deleteShoppingCartById(Long id) {
		shoppingCartRepository.deleteById(id);
	}
}
