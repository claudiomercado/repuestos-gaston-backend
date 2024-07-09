package com.repuestosgaston.products.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductOrderResponseDTO {

    private ProductResponseDTO product;
    private int quantity;
    private double subTotalPrice;
	
}
