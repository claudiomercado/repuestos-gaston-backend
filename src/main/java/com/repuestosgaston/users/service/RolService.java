package com.repuestosgaston.users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;
import com.repuestosgaston.users.repository.RolRepository;

@Service
public class RolService {

	private final RolRepository rolRepository;
	
	public RolService(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}

	@Transactional
	public RoleEntity createRoleUser(){
		RoleEntity rol = new RoleEntity();
    	rol.setId((long) 1);
    	rol.setName(RoleEnum.USER);
	    return rolRepository.save(rol);
	}
	
	@Transactional
	public RoleEntity createRoleAdmin(){
		RoleEntity rol = new RoleEntity();
    	rol.setId((long) 2);
    	rol.setName(RoleEnum.ADMIN);
	    return rolRepository.save(rol);
	}
}
