package com.repuestosgaston.shopping_cart.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.products.converter.ProductEntityToProductOrderResponse;
import com.repuestosgaston.products.converter.ProductEntityToProductResponse;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.ProductSaleEntity;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;

@Component
public class SaleOrderEntityToSaleOrderResponseDTO implements Converter<SaleOrderEntity, SaleOrderResponseDTO>{

	@Autowired
	ProductEntityToProductOrderResponse productEntityToProductOrderResponse;
	
	@Autowired
	ProductEntityToProductResponse productEntityToProductResponse;
		
	
	@Override
	public SaleOrderResponseDTO convert(SaleOrderEntity saleOrderEntity) {
		List<ProductSaleResponseDTO> products = new ArrayList<>();
		
		SaleOrderResponseDTO saleOrderResponseDTO = new SaleOrderResponseDTO();
		saleOrderResponseDTO.setId(saleOrderEntity.getId());
		saleOrderResponseDTO.setNumberSale(saleOrderEntity.getNumberSale());
		saleOrderResponseDTO.setSaleStatus(saleOrderEntity.getSaleStatus());
		saleOrderResponseDTO.setName(saleOrderEntity.getName());
		saleOrderResponseDTO.setSurname(saleOrderEntity.getSurname());
		saleOrderResponseDTO.setDni(saleOrderEntity.getDni());
		
		for(ProductSaleEntity product: saleOrderEntity.getProductSale()) {
			ProductSaleResponseDTO productSaleResponse = new ProductSaleResponseDTO();
			productSaleResponse.setId(product.getId());
			productSaleResponse.setProduct(productEntityToProductResponse.convert(product.getProduct()));
			productSaleResponse.setQuantity(product.getQuantity());
			productSaleResponse.setSubTotalPrice(product.getSubTotalPrice());
			products.add(productSaleResponse);
		}
		saleOrderResponseDTO.setProducts(products);

		return saleOrderResponseDTO;
	}

}
