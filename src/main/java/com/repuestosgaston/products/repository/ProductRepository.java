package com.repuestosgaston.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.products.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

	Page<ProductEntity> findAll(Pageable pageable);
	
	@Query(value = "SELECT * FROM Product WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')", nativeQuery = true)
	Page<ProductEntity> filterToName(Pageable pageable, String name);
	
	@Query(value = "SELECT * FROM Product WHERE category_id = :category", nativeQuery = true)
	Page<ProductEntity> filterToCategory(Pageable pageable, Long category);
	
}
