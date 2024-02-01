package com.repuestosgaston.shopping_cart.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.users.model.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartResponseDTO {

	@JsonProperty("shopping_cart_id")
	private Long id;
	@JsonProperty("total_price")
	private Double totalPrice;
	@JsonProperty("number_cart")
	private Integer numberCart;
	@JsonProperty("products")
	private List<ProductResponseDTO> products;
	@JsonProperty("user")
	private UserEntity user;
}
