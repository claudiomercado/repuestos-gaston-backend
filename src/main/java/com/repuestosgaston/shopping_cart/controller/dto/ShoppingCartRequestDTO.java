package com.repuestosgaston.shopping_cart.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.products.controller.dto.ProductRequestDTO;
import com.repuestosgaston.users.model.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartRequestDTO {
	
	@JsonProperty("total_price")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double totalPrice;
	@JsonProperty("number_cart")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer numberCart;
	@JsonProperty("products")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ProductRequestDTO> products;
	@JsonProperty("user")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private UserEntity user;
}
