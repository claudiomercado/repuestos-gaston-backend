package com.repuestosgaston.products.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.products.model.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
	
	Page<CategoryEntity> findAll(Pageable pageable);
	
	Optional<CategoryEntity> findByName (String name);
}
