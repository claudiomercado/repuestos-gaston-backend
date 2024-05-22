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
public class ProductResponseDTO {
	
	@JsonProperty("product_id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("price")
	private Double price;
	@JsonProperty("stock")
	private Integer stock;
	@JsonProperty("category")
	private String category;
	@JsonProperty("bar_code")
	private Integer barCode;
	@JsonProperty("image")
	private String image;
}
