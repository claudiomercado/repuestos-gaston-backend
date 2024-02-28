package com.repuestosgaston.configuration.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.repuestosgaston.configuration.security.filter.JwtAuthenticationFilter;
import com.repuestosgaston.configuration.security.filter.JwtAuthorizationFilter;
import com.repuestosgaston.configuration.security.jwt.JwtUtils;
import com.repuestosgaston.users.service.UserDetailsServiceImpl;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
//Configuracion de la Seguridad de la aplicacion
	
	@Autowired
	private UserDetailsServiceImpl detailsServiceImpl;
	
	@Autowired
	JwtAuthorizationFilter authorizationFilter;
	
	@Autowired
	JwtUtils jwtUtils;
	
	//Configuracion filtros de cadena
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception{
		
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		authenticationFilter.setAuthenticationManager(authenticationManager);
		
		return httpSecurity
//				.csrf(config -> config.disable())
				.csrf().disable()
				.authorizeHttpRequests(auth ->{
					auth.requestMatchers(HttpMethod.POST,"/v1/user/createUser").permitAll();
					auth.requestMatchers(HttpMethod.GET,"/v1/product/getAll").permitAll();
					auth.requestMatchers(HttpMethod.GET,"/v1/product/getById/**").permitAll();
					auth.requestMatchers(HttpMethod.POST,"/login").permitAll();
					auth.requestMatchers(HttpMethod.POST,"/logout").permitAll();
					auth.anyRequest().authenticated();
					try {
						auth.and().cors().configurationSource(corsConfigurationSource());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				})
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.addFilter(authenticationFilter)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	//Crea o no una forma de encriptacion 
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Crea un administrador de la autenticacion
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(detailsServiceImpl)
			.passwordEncoder(passwordEncoder)
			.and()
			.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type","Authorization", "Access-Control-Allow-Origin"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>( new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
			
		return bean;
	}
}
