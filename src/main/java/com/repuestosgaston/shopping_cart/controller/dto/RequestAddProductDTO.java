package com.repuestosgaston.shopping_cart.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAddProductDTO {
	
	@JsonProperty("idProduct")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long idProduct;
	@JsonProperty("amount")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer amount;

}
