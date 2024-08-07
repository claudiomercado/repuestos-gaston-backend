package com.repuestosgaston.shopping_cart.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.shopping_cart.model.enums.SaleOrderStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleOrderStatusRequestDTO {

	@JsonProperty("sale_status")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private SaleOrderStatusEnum saleStatus;
}

