package com.repuestosgaston.users.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository ;
	
	//Carga el usuario de la aplicacion
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username)
								.orElseThrow(()-> new UsernameNotFoundException("El usuario "+username + " no existe."));
	
		Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
								.stream().map(rol -> new SimpleGrantedAuthority("ROLE_".concat(rol.getName().name())))
								.collect(Collectors.toSet());
		
		return new User(userEntity.getUsername(),
				userEntity.getPassword(),
				true,true,true,true,authorities);
	}

}
