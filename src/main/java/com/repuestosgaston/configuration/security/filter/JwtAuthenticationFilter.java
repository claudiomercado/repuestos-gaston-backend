package com.repuestosgaston.configuration.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repuestosgaston.configuration.security.jwt.JwtUtils;
import com.repuestosgaston.users.model.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private JwtUtils jwtUtils;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils= jwtUtils;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
												HttpServletResponse response) throws AuthenticationException {
		
		UserEntity userEntity = null;
		String username = "";
		String password = "";
		try {
			userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
			username = userEntity.getUsername();
			password = userEntity.getPassword();
			
		} catch (Exception e) {
			log.error("Error en la autenticacion ".concat(e.getMessage()));
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
		
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		
		User user = (User) authResult.getPrincipal();
		List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
		
		String token = jwtUtils.generateAccesToken(user.getUsername(),roles);
		
		response.addHeader("Authorization", token);
		
		Map<String, Object> httpResponse = new HashMap<>();
		httpResponse.put("token", token);
		httpResponse.put("Message", "Autenticacion Correcta");
		httpResponse.put("Username", user.getUsername());
		httpResponse.put("Roles", roles);
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().flush();
		
		super.successfulAuthentication(request, response, chain, authResult);
	}

	
	
}
