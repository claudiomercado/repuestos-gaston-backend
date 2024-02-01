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
public class CategoryReponseDTO {
	
	@JsonProperty("category_id")
	private Long id;
	@JsonProperty("name")
	private String name;
	
}
