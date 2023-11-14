package com.repuestosgaston.products.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.products.model.CategoryEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO implements Serializable{
	private static final long serialVersionUID = 1L; 
	                                         
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
	private String stock;
	@JsonProperty("category")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private CategoryEntity category;
	
}
