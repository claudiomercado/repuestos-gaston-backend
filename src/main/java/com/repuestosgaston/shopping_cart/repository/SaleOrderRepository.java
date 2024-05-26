package com.repuestosgaston.shopping_cart.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrderEntity, Long>{
	Page<SaleOrderEntity> findAll(Pageable pageable);
	
	Optional<SaleOrderEntity> findByNumberSale(Integer numberSale);
}
