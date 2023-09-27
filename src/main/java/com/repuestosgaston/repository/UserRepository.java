package com.repuestosgaston.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.model.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByUsername (String username); 
}
