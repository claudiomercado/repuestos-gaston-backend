package com.repuestosgaston.products.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;

@Component
public class ProductEntityToProductResponse implements Converter<ProductEntity, ProductResponseDTO> {

	@Override
    public ProductResponseDTO convert(ProductEntity productEntity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(productEntity.getId());
        dto.setName(productEntity.getName());
        dto.setDescription(productEntity.getDescription());
        dto.setBarCode(productEntity.getBarCode());
        dto.setStock(productEntity.getStock());
        dto.setPriceIdStripe(productEntity.getPriceIdStripe());
        dto.setPrice(productEntity.getPrice());
        dto.setImage(productEntity.getImage());
        // Mapeo del objeto Category a su nombre
        if (productEntity.getCategory() != null) {
            dto.setCategory(productEntity.getCategory().getName());
        }
        
        return dto;
    }

}
