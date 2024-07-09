package com.repuestosgaston.products.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.controller.dto.ProductOrderResponseDTO;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;

@Component
public class ProductEntityToProductOrderResponse implements Converter<ProductEntity, ProductOrderResponseDTO>{

	@Autowired
	ProductEntityToProductResponse productEntityToProductResponse;
	@Autowired
	ProductRepository productRepository;
	
	@Override
	@Nullable
	public ProductOrderResponseDTO convert(ProductEntity source) {
		ProductResponseDTO productResponseDTO = productEntityToProductResponse.convert(source);
		ProductOrderResponseDTO dto = new ProductOrderResponseDTO();
		dto.setProduct(productResponseDTO);
		dto.setQuantity(source.getAmount());
		dto.setSubTotalPrice(source.getSub_total_price());	
		
		source.setAmountZero();
		source.setSubTotalPriceZero();
		
		productRepository.save(source);
		
		return dto;
	}

}
