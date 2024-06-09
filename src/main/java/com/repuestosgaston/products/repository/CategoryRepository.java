package com.repuestosgaston.products.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.products.model.CategoryEntity;
import com.repuestosgaston.products.model.ProductEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
	
	Page<CategoryEntity> findAll(Pageable pageable);
	
	Optional<CategoryEntity> findByName (String name);

	@Transactional
	@Query(value = "SELECT * FROM Category WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')", nativeQuery = true)
	Page<CategoryEntity> filterByName(Pageable pageable, String name);
}
