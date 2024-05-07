package com.repuestosgaston.products.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartResponseDTO {

	@JsonProperty("product_id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("price")
	private Double price;
	@JsonProperty("stock")
	private Integer stock;
	@JsonProperty("amount")
	private Integer amount;
	@JsonProperty("sub_total_price")
	private Double sub_total_price;
}
