package com.repuestosgaston.shopping_cart.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.shopping_cart.model.enums.SaleOrderStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderResponseDTO {

	@JsonProperty("sale_order_id")
	private Long id;
	@JsonProperty("number_sale")
	private Integer numberSale;
	@JsonProperty("description")
	private String description;
	@JsonProperty("sale_status")
	private SaleOrderStatusEnum saleStatus;
	@JsonProperty("shopping_cart")
	private ShoppingCartResponseDTO shoppingCart;
}
