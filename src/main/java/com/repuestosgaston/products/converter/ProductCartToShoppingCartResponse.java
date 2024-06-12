package com.repuestosgaston.products.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductCartResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;

@Component
public class ProductCartToShoppingCartResponse implements Converter<ProductEntity, ProductCartResponseDTO>{

	@Override
	 public ProductCartResponseDTO convert(ProductEntity productEntity) {
		ProductCartResponseDTO dto = new ProductCartResponseDTO();
        dto.setId(productEntity.getId());
        dto.setName(productEntity.getName());
        dto.setStock(productEntity.getStock());
        dto.setPrice(productEntity.getPrice());
        dto.setAmount(productEntity.getAmount());
        dto.setPriceIdStripe(productEntity.getPriceIdStripe());
        dto.setCategory(productEntity.getCategory().getName());
        dto.setImage(productEntity.getImage());
        dto.setSub_total_price(productEntity.getSub_total_price());
        
        return dto;
    }

}
