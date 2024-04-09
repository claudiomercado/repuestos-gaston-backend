package com.repuestosgaston.shopping_cart.model;

import com.repuestosgaston.shopping_cart.model.enums.SaleOrderStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sale_order", uniqueConstraints = @UniqueConstraint(columnNames = {"number_sale"}))
public class SaleOrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "number_sale")
	private Integer numberSale;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "sale_status")
	@Enumerated(EnumType.STRING)
	private SaleOrderStatusEnum saleStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	private ShoppingCartEntity shoppingCart;
	
}
