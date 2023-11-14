package com.repuestosgaston.users.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.users.model.RoleEntity;


@Repository
public interface RolRepository extends CrudRepository<RoleEntity, Long>{

	
}
