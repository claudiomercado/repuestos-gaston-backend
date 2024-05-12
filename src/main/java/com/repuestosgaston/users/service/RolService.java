package com.repuestosgaston.users.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;

@Service
public class RolService {

	public Set<RoleEntity> createRoleUser(){
		RoleEntity rol = new RoleEntity();
    	Set<RoleEntity> roles = new HashSet<>();
    	rol.setId((long) 1);
    	rol.setName(RoleEnum.USER);
    	roles.add(rol);
	    return roles;
	}
}
