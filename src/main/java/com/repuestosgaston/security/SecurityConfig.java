package com.repuestosgaston.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
	
	//Creacion de un usuario de la aplicacion para pruebas
	@Bean
	UserDetailsService userDetailsService () {
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(User.withUsername("cmercado")
				.password("cmercado")
				.roles(null)
				.build());
		return userDetailsManager;
	}
}
