package com.repuestosgaston.products.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
	private byte[] image;
	
	@Column(name = "amount", nullable = true)
	private Integer amount;

	@Column(name = "sub_total_price", nullable = true)
	private Double sub_total_price;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private CategoryEntity category;
	
}
