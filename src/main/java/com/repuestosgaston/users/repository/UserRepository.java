package com.repuestosgaston.users.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.users.model.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByUsername (String username); 
}
