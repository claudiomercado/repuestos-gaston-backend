package com.repuestosgaston.shopping_cart.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;

public class SaleOrderRequestDTOToSaleOrderEntity implements Converter<SaleOrderRequestDTO, SaleOrderEntity>{

	@Override
	@Nullable
	public SaleOrderEntity convert(SaleOrderRequestDTO saleOrderRequestDTO) {
		SaleOrderEntity saleOrderEntity = new SaleOrderEntity();
		
//		sale
		
		
		return saleOrderEntity;
	}

}
