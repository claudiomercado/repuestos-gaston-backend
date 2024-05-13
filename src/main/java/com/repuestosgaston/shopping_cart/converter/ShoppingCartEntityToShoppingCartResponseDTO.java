package com.repuestosgaston.shopping_cart.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductCartResponseDTO;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.converter.ProductCartToShoppingCartResponse;
import com.repuestosgaston.products.converter.ProductEntityToProductResponse;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartResponseDTO;
import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;

@Component
public class ShoppingCartEntityToShoppingCartResponseDTO implements Converter<ShoppingCartEntity, ShoppingCartResponseDTO>{

	@Autowired
	private ProductCartToShoppingCartResponse productConverter;
	
	@Override
	public ShoppingCartResponseDTO convert(ShoppingCartEntity shoppingCartEntity) {
		ShoppingCartResponseDTO dto = new ShoppingCartResponseDTO();
		dto.setId(shoppingCartEntity.getId());
		dto.setNumberCart(shoppingCartEntity.getNumberCart());
		dto.setTotalPrice(shoppingCartEntity.getTotalPrice());
		
		List<ProductCartResponseDTO> productsResponseDTO = new ArrayList<>();
		for (ProductEntity productEntity : shoppingCartEntity.getProducts()) {
			productsResponseDTO.add(productConverter.convert(productEntity));
		}	
		dto.setProducts(productsResponseDTO);
		
		return dto;
	}


}
