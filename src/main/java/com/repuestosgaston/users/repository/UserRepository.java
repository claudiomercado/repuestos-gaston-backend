package com.repuestosgaston.users.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.repuestosgaston.users.model.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByUsername (String username);
	
	Optional<UserEntity> findByEmail (String email);
	
	Page<UserEntity> findByDni (Pageable pageable, String dni);
	
	Page<UserEntity> findAll(Pageable pageable);
}
