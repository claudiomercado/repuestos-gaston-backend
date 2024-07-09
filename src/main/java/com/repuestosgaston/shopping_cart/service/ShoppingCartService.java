package com.repuestosgaston.shopping_cart.service;

import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;
import com.repuestosgaston.shopping_cart.controller.dto.RequestAddProductDTO;
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
	private final ShoppingCartEntityToShoppingCartResponseDTO converterShoppingCartResponse;

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository,
			ProductRepository productRepository,
			ShoppingCartEntityToShoppingCartResponseDTO converterShoppingCartResponse) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.converterShoppingCartResponse = converterShoppingCartResponse;
	}
	@Transactional
	public Page<ShoppingCartResponseDTO> getAllShoppingCart(int page, int size, String sort, String sortDirection) {
		Sort sorter = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
		Pageable pageable = PageRequest.of(page, size, sorter);

		return shoppingCartRepository.findAll(pageable).map(converterShoppingCartResponse::convert);
	}
	@Transactional
	public ShoppingCartResponseDTO getShoppingCartById(String username) {
		UserEntity user = userRepository.findByUsername(username).get();
		ShoppingCartEntity shoppingCartEntity = user.getCart();
		return shoppingCartRepository.findById(shoppingCartEntity.getId()).map(converterShoppingCartResponse::convert)
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Shopping Cart [%s] not found", shoppingCartEntity.getId())));

	}
	@Transactional
	public ShoppingCartEntity getShoppingCartByIdLong(Long id) {
		ShoppingCartEntity shoppingCartEntity = shoppingCartRepository.findById(id).get();
		
		return shoppingCartEntity;
	}
	
	@Transactional
	public ShoppingCartEntity createShoppingCart() {
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		shoppingCartEntity.setTotalPrice(0.0);
		shoppingCartEntity.setNumberCart(generateNumberSale(10000,99999));
		return shoppingCartRepository.save(shoppingCartEntity);
	}

	public Integer generateNumberSale(int min, int max) {
        Random random = new Random();
        int value = random.nextInt((max - min) + 1) + min;
        return value;
    }
	
	@Transactional
	public void addProducts(String username, RequestAddProductDTO requestAddProductDTO) {
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

		ShoppingCartEntity cartEntity = userEntity.getCart();
		if (cartEntity == null) {
			throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
		}

		ProductEntity product = productRepository.findById(requestAddProductDTO.getIdProduct())
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));

		if (requestAddProductDTO.getAmount() > product.getStock()) {
			throw new IllegalArgumentException(
					String.format("Insufficient amount [%s]", requestAddProductDTO.getAmount()));
		}

		double subtotalProduct = 0.0;
		List<ProductEntity> products = cartEntity.getProducts();
		Integer newAmount = 0;

		if (products.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
			// Si el producto ya está en el carrito, actualiza la cantidad
			for (ProductEntity p : products) {
				if (p.getId().equals(product.getId())) {
					newAmount = p.getAmount() + requestAddProductDTO.getAmount();
					subtotalProduct += calculatePriceAmountProduct(p, newAmount);
					product.setAmount(newAmount);
					break;
				}
			}
		} else {
			products.add(product);
			subtotalProduct += calculatePriceAmountProduct(product, requestAddProductDTO.getAmount());
			product.setAmount(requestAddProductDTO.getAmount());
		}
		product.setSub_total_price(subtotalProduct);

		// Actualiza el total del carrito y la lista de productos
		cartEntity.setTotalPrice(calculateTotalPrice(products));
		cartEntity.setProducts(products);

		shoppingCartRepository.save(cartEntity);
	}

	public Double calculateTotalPrice(List<ProductEntity> products) {
		return products.stream().mapToDouble(ProductEntity::getSub_total_price).sum();
	}

	private Double calculatePriceAmountProduct(ProductEntity product, Integer amount) {
		return product.getPrice() * amount;
	}

	@Transactional
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
	
	@Transactional
	public void decreaseProduct(String username, RequestAddProductDTO requestAddProductDTO) {
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException(String.format("User [%s] not found", username)));

		ShoppingCartEntity cartEntity = userEntity.getCart();
		if (cartEntity == null) {
			throw new IllegalArgumentException(String.format("Cart not found for user [%s]", username));
		}

		ProductEntity product = productRepository.findById(requestAddProductDTO.getIdProduct())
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Product [%s] not found", requestAddProductDTO.getIdProduct())));

		double subtotalProduct = 0.0;
		List<ProductEntity> products = cartEntity.getProducts();
		Integer newAmount = 0;

		for (ProductEntity p : products) {
			if (p.getId().equals(product.getId())) {
				newAmount = p.getAmount() - requestAddProductDTO.getAmount();
				product.setAmount(newAmount);
				subtotalProduct += calculatePriceAmountProduct(p, newAmount);
				break;
			}
		}

		product.setSub_total_price(subtotalProduct);
		
		// Actualiza el total del carrito y la lista de productos
		cartEntity.setTotalPrice(calculateTotalPrice(products));
		cartEntity.setProducts(products);
		shoppingCartRepository.save(cartEntity);
	}

	@Transactional
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
