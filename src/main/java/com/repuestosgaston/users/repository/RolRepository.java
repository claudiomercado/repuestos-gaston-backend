package com.repuestosgaston.users.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;


@Repository
public interface RolRepository extends CrudRepository<RoleEntity, Long>{
    RoleEntity findByName(RoleEnum name);

	
}
