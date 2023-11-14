package com.repuestosgaston.products.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	
}
