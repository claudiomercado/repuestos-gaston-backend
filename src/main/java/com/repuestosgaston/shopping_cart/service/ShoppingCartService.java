package com.repuestosgaston.shopping_cart.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.products.controller.dto.ProductCartResponseDTO;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.converter.ProductCartToShoppingCartResponse;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.shopping_cart.controller.dto.RequestAddProductDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.converter.ShoppingCartEntityToShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.repository.ShoppingCartRepository;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;

@Service
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	private final ShoppingCartEntityToShoppingCartResponseDTO converterShoppingCartResponse;
	private final ProductCartToShoppingCartResponse converterProductCartResponse;
	private Integer productQuantity;
	

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, 
			UserRepository userRepository,
			ProductRepository productRepository, 
			ModelMapper modelMapper,
			ShoppingCartEntityToShoppingCartResponseDTO converterShoppingCartResponse, 
			ProductCartToShoppingCartResponse converterProductCartResponse) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
		this.converterShoppingCartResponse = converterShoppingCartResponse;
		this.converterProductCartResponse = converterProductCartResponse;
	}

	//Ver si sigue
	public Page<ShoppingCartResponseDTO> getAllShoppingCart(int page, int size, String sort, String sortDirection) {
		Sort sorter = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
		Pageable pageable = PageRequest.of(page, size, sorter);

		return shoppingCartRepository.findAll(pageable)
				.map(converterShoppingCartResponse::convert);
	}

	public ShoppingCartResponseDTO getShoppingCartById(String username) {
		UserEntity user = userRepository.findByUsername(username).get();
		ShoppingCartEntity shoppingCartEntity = user.getCart();
		return shoppingCartRepository.findById(shoppingCartEntity.getId())
				.map(converterShoppingCartResponse::convert)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Shopping Cart [%s] not found", shoppingCartEntity.getId())));


	}
	public ShoppingCartEntity createShoppingCart() {
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		shoppingCartEntity.setTotalPrice(0.0);
		//shoppingCartEntity.setNumberCart(1471);
		
		return shoppingCartRepository.save(shoppingCartEntity);
	}

	public void addProducts(String username,RequestAddProductDTO requestAddProductDTO) {
		UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

	    ShoppingCartEntity cartEntity = userEntity.getCart();
	    if (cartEntity == null) {
	        throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
	    }

	    ProductEntity product = productRepository.findById(requestAddProductDTO.getIdProduct())
	            .orElseThrow(() -> new IllegalArgumentException(String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));
	    
	    if (requestAddProductDTO.getAmount() > product.getStock()) {
	        throw new IllegalArgumentException(String.format("Insufficient amount [%s]", requestAddProductDTO.getAmount()));
	    }
	    
	    double subtotalProduct = 0.0;
	    List<ProductEntity> products = cartEntity.getProducts();
	    Integer newAmount = 0;

	    if (products.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
	        // Si el producto ya está en el carrito, actualiza la cantidad
	        for (ProductEntity p : products) {
	            if (p.getId().equals(product.getId())) {
	            	newAmount = p.getAmount() + requestAddProductDTO.getAmount();
	            	subtotalProduct += calculatePriceAmountProduct(p,newAmount);
	            	product.setAmount(newAmount);
	                break;
	            }
	        }
	    } else {
	        // Si el producto no está en el carrito, agrégalo
	        products.add(product);
	        subtotalProduct += calculatePriceAmountProduct(product,requestAddProductDTO.getAmount());
	        product.setAmount(requestAddProductDTO.getAmount());
	    }
	    product.setSub_total_price(subtotalProduct);

	    // Actualiza el total del carrito y la lista de productos
	    cartEntity.setTotalPrice(calculateTotalPrice(products));
	    cartEntity.setProducts(products);

	    shoppingCartRepository.save(cartEntity);
	}
	
	private Double calculateTotalPrice(List<ProductEntity> products) {
		return products.stream()
				.mapToDouble(ProductEntity::getSub_total_price)
				.sum();
	}
	
	private Double calculatePriceAmountProduct(ProductEntity product, Integer amount) {
		 return product.getPrice() * amount;
	}

	public void deleteProductById(String username, Long productId) {
		UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

	    ShoppingCartEntity cartEntity = userEntity.getCart();
	    if (cartEntity == null) {
	        throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
	    }

	    ProductEntity product = productRepository.findById(productId)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("Product [%s] not found", productId)));
	    
	    List<ProductEntity> products = cartEntity.getProducts();

	    for (ProductEntity p : products) {
            if (p.getId().equals(product.getId())) {
            	products.remove(p);
                break;
            }
        }
	    product.setSub_total_price(0.0);
	    product.setAmount(0);

	    // Actualiza el total del carrito y la lista de productos
	    cartEntity.setTotalPrice(calculateTotalPrice(products));
	    cartEntity.setProducts(products);

	    shoppingCartRepository.save(cartEntity);
	}
	
	public void decreaseProduct(String username,RequestAddProductDTO requestAddProductDTO) {
		UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

	    ShoppingCartEntity cartEntity = userEntity.getCart();
	    if (cartEntity == null) {
	        throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
	    }

	    ProductEntity product = productRepository.findById(requestAddProductDTO.getIdProduct())
	            .orElseThrow(() -> new IllegalArgumentException(String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));

	    double subtotalProduct = 0.0;
	    List<ProductEntity> products = cartEntity.getProducts();
	    Integer newAmount = 0;
	    
	    for (ProductEntity p : products) {
            if (p.getId().equals(product.getId())) {
            	newAmount = p.getAmount() - requestAddProductDTO.getAmount();
            	product.setAmount(newAmount);
            	subtotalProduct += calculatePriceAmountProduct(p,newAmount);
                break;
            }
        }
	    
	    product.setSub_total_price(subtotalProduct);
	    // Actualiza el total del carrito y la lista de productos
	    cartEntity.setTotalPrice(calculateTotalPrice(products));
	    cartEntity.setProducts(products);
	    shoppingCartRepository.save(cartEntity);
	}
	
	public void clearShoppingCart(String username) {
		UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

	    ShoppingCartEntity cartEntity = userEntity.getCart();
	    if (cartEntity == null) {
	        throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
	    }
	    
	    cartEntity.clearProducts();
	    shoppingCartRepository.save(cartEntity);
	}
	
}
