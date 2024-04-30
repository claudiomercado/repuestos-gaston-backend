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
import com.repuestosgaston.shopping_cart.converter.ShoppingCartEntityToShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	private final ShoppingCartEntityToShoppingCartResponseDTO converter;
	private Integer productQuantity;
	

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository,
			ProductRepository productRepository, ModelMapper modelMapper,ShoppingCartEntityToShoppingCartResponseDTO converter) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
		this.converter = converter;
	}

	//Ver si sigue
	public Page<ShoppingCartResponseDTO> getAllShoppingCart(int page, int size, String sort, String sortDirection) {
		Sort sorter = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
		Pageable pageable = PageRequest.of(page, size, sorter);

		return shoppingCartRepository.findAll(pageable)
				.map(converter::convert);
	}

	//Ver si sigue
	public ShoppingCartResponseDTO getShoppingCartById(Long shoppingCartId) {
		return shoppingCartRepository.findById(shoppingCartId)
				.map(converter::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCartId)));
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

	    ShoppingCartEntity cartEntity = userEntity.getCart();
	    if (cartEntity == null) {
	        throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
	    }

	    var product = productRepository.findById(requestAddProductDTO.getIdProduct())
	            .orElseThrow(() -> new IllegalArgumentException(String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));

	    if (requestAddProductDTO.getAmount() > product.getStock()) {
	        throw new IllegalArgumentException(String.format("Insufficient amount [%s]", requestAddProductDTO.getAmount()));
	    }

	    List<ProductEntity> products = cartEntity.getProducts();

	    if (products.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
	        // Si el producto ya está en el carrito, actualiza la cantidad
	        for (ProductEntity p : products) {
	            if (p.getId().equals(product.getId())) {
	                int newAmount = p.getStock() + requestAddProductDTO.getAmount();
	                p.setStock(newAmount);
	                break;
	            }
	        }
	    } else {
	        // Si el producto no está en el carrito, agrégalo
	        products.add(product);
	    }

	    // Actualiza el total del carrito y la lista de productos
	    cartEntity.setTotalPrice(calculateTotalPrice(products));
	    cartEntity.setProducts(products);

	    shoppingCartRepository.save(cartEntity);
	}

	//Descontar stock de productos llamando al servicio de productos
	//Implementar misma funcionalidad pero para quitar productos del carrito
	//Funcion que vacie el carrito
	private Double calculateTotalPrice(List<ProductEntity> products) {
		Double totalPrice = 0.0;
		for (ProductEntity productEntity : products) {
			totalPrice += productEntity.getPrice();
		}
		return totalPrice;
	}

	public void addProductToCart(String username ,Long idProduct) {
				
		
	}
}
