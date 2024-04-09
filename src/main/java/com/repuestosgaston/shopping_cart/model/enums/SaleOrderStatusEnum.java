package com.repuestosgaston.shopping_cart.model.enums;

public enum SaleOrderStatusEnum {

	PENDING_PAYMENT("Pendiente de pago"), PAID("Pagado"), REJECTED("Rechazado");
	
	private final String name;
	
	SaleOrderStatusEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
