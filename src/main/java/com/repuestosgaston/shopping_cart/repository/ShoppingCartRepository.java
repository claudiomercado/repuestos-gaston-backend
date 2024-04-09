package com.repuestosgaston.shopping_cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long>{
	
	Page<ShoppingCartEntity> findAll(Pageable pageable);
}
