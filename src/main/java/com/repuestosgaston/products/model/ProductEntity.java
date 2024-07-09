package com.repuestosgaston.products.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.repuestosgaston.shopping_cart.model.ProductSaleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "product", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"bar_code"}),
	    @UniqueConstraint(columnNames = {"name"})
	})
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
	private Integer stock;
	
	@Column(name = "bar_code")
	private Integer barCode;
	
	@Column(name = "image")
	@Lob
	private String image;
	
	@Column(name = "amount", nullable = true)
	private Integer amount;

	@Column(name = "sub_total_price", nullable = true)
	private Double sub_total_price;
	
	@Column(name = "product_id_stripe")
	private String productIdStripe;
	
	@Column(name = "price_id_stripe")
	private String priceIdStripe;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private CategoryEntity category;
	
	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<ProductSaleEntity> productSales = new ArrayList<>();
	
	public void setAmountZero() {
		amount = 0;
	}
	
	public void setSubTotalPriceZero() {
		sub_total_price = 0.0;
	}
}
