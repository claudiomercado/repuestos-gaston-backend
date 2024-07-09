package com.repuestosgaston.shopping_cart.converter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaleResponseDTO {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("product")
	private ProductResponseDTO product;
	@JsonProperty("quantity")
	private Integer quantity;
	@JsonProperty("sub_total_price")
	private Double subTotalPrice;
}
