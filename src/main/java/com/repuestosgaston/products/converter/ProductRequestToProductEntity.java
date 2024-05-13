package com.repuestosgaston.products.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductRequestDTO;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.CategoryRepository;

@Component
public class ProductRequestToProductEntity implements Converter<ProductRequestDTO, ProductEntity>{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public ProductEntity convert(ProductRequestDTO productRequestDTO) {
		var category = categoryRepository.findById(productRequestDTO.getIdCategory());
		if (category.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Category [%s] not found", productRequestDTO.getIdCategory()));
		}
		ProductEntity productEntity = new ProductEntity();
		if (productRequestDTO.getName() != null) {
			productEntity.setName(productRequestDTO.getName());
	    }
	    if (productRequestDTO.getDescription() != null) {
	        productEntity.setDescription(productRequestDTO.getDescription());
	    }
	    if (productRequestDTO.getPrice() != null) {
	        productEntity.setPrice(productRequestDTO.getPrice());
	    }
	    if (productRequestDTO.getStock() != null) {
	        productEntity.setStock(productRequestDTO.getStock());
	    }
	    if (productRequestDTO.getBarCode() != null) {
	        productEntity.setBarCode(productRequestDTO.getBarCode());
	    }
	    productEntity.setAmount(null);
		productEntity.setSub_total_price(null);
		productEntity.setCategory(category.get());
		return productEntity;
//		productEntity.setName(productRequestDTO.getName());
//		productEntity.setDescription(productRequestDTO.getDescription());
//		productEntity.setPrice(productRequestDTO.getPrice());
//		productEntity.setStock(productRequestDTO.getStock());
//		productEntity.setBarCode(productRequestDTO.getBarCode());
//		productEntity.setAmount(null);
//		productEntity.setSub_total_price(null);	
	}

}
