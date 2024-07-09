package com.repuestosgaston.shopping_cart.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.shopping_cart.converter.ProductSaleResponseDTO;
import com.repuestosgaston.shopping_cart.model.ProductSaleEntity;
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

	@JsonProperty("id")
	private Long id;
	@JsonProperty("number_sale")
	private Integer numberSale;
	@JsonProperty("name")
	private String name;
	@JsonProperty("surname")
	private String surname;
	@JsonProperty("dni")
	private String dni;
	@JsonProperty("sale_status")
	private SaleOrderStatusEnum saleStatus;
	@JsonProperty("products")
	private List<ProductSaleResponseDTO> products;
}
