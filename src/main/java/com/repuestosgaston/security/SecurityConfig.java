package com.repuestosgaston.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//Configuracion de la Seguridad de la aplicacion
	
	//Configuracion filtros de cadena
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
				.csrf(config -> config.disable())
				.authorizeHttpRequests(auth ->{
					auth.requestMatchers("/helloSecured").permitAll();
					auth.anyRequest().authenticated();
				})
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.httpBasic()
				.and()
				.build();
	}
	
	//Crea un usuario de aplicacion (para pruebas)
	@Bean
	UserDetailsService userDetailsService () {
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(User.withUsername("cmercado")
				.password("cmercado")
				.roles()
				.build());
		return userDetailsManager;
	}
	
	//Crea o no una forma de encriptacion 
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	//Crea un administrador de la autenticacion
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(userDetailsService())
			.passwordEncoder(passwordEncoder)
			.and()
			.build();
	}
}
