package com.repuestosgaston.shopping_cart.model;

import java.util.List;

import com.repuestosgaston.products.model.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name = "shopping_cart", uniqueConstraints = @UniqueConstraint(columnNames = {"number_cart"}))
public class ShoppingCartEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Column(name="number_cart")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer numberCart;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "shopping_cart_product",
	    joinColumns =@JoinColumn(name = "id_shopping_cart"),
		inverseJoinColumns = @JoinColumn(name = "id_product")
	)
	private List<ProductEntity> products;
	
}
