package com.repuestosgaston.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.repuestosgaston.configuration.security.filter.JwtAuthenticationFilter;
import com.repuestosgaston.configuration.security.filter.JwtAuthorizationFilter;
import com.repuestosgaston.configuration.security.jwt.JwtUtils;
import com.repuestosgaston.users.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;


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
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/v1/users/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/v1/category/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/v1/product/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/v1/product/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/v1/product/filter/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/v1/auth/logout").permitAll();
                    auth.requestMatchers(HttpMethod.PATCH, "/v1/orders/updateStatus/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> login.loginPage("/login").permitAll())
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(detailsServiceImpl)
			.passwordEncoder(passwordEncoder)
			.and()
			.build();
	}
	
	@Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        };
    }
}
