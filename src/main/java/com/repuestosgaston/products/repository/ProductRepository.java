package com.repuestosgaston.products.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.users.model.UserEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

	Page<ProductEntity> findAll(Pageable pageable);
	
	@Transactional
	@Query(value = "SELECT * FROM Product WHERE LOWER(name) LIKE CONCAT('%', LOWER(:name), '%')", nativeQuery = true)
	Page<ProductEntity> filterByName(Pageable pageable, String name);
	
	@Transactional
	@Query(value = "SELECT * FROM Product WHERE category_id = :category", nativeQuery = true)
	Page<ProductEntity> filterByCategory(Pageable pageable, Long category);
	
	@Transactional
	@Query(value = "SELECT * FROM Product WHERE bar_code = :barCode", nativeQuery = true)
	Page<ProductEntity> findByBarCodeWithPageable (Pageable pageable, Integer barCode);
	
	@Transactional
	@Query(value = "SELECT * FROM Product WHERE bar_code = :barCode", nativeQuery = true)
	Optional<ProductEntity> findByBarCode (Integer barCode);
	
	@Transactional
	@Query(value = "SELECT * FROM Product WHERE stock <= 5", nativeQuery = true)
	Page<ProductEntity> findByLowStock (Pageable pageable);

}
