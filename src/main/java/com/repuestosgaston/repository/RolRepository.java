package com.repuestosgaston.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.model.RolEntity;

@Repository
public interface RolRepository extends CrudRepository<RolEntity, Long>{

	
}
