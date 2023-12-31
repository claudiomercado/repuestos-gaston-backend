package com.repuestosgaston.products.model;

import java.util.List;

import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"bar_code"}))
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "price")
	private Double price;
	@Column(name = "stock")
	private String stock;
	@Column(name = "bar_code")
	private Integer barCode;
	@Column(name = "image")
	@Lob
	private byte[] image;
	@ManyToOne(fetch = FetchType.EAGER)
	private CategoryEntity category;
	@ManyToMany(mappedBy = "products")
	private List<ShoppingCartEntity> shoppingCarts;
	
}
