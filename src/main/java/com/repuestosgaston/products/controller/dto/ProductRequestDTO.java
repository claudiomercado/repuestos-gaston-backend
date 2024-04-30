package com.repuestosgaston.products.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
	                                         
	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonProperty("description")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;
	@JsonProperty("price")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double price;
	@JsonProperty("stock")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer stock;
	@JsonProperty("bar_code")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer barCode;
	@JsonProperty("id_category")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long idCategory;
	
}
