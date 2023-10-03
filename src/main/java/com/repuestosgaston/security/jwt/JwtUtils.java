package com.repuestosgaston.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
	
	//Clase de utilidad para firmar token
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	@Value("${jwt.time.expiration}")
	private String timeExpiration;

	//Generar un token
	public String generateAccesToken(String username) {
		return Jwts.builder()
			   .setSubject(username)
			   .setIssuedAt(new Date(System.currentTimeMillis()))
			   .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
			   .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
			   .compact();
	}

	//Validar Token de acceso
	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSignatureKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return true;
		} catch (Exception e) {
			log.error("Token invalido, error: ".concat(e.getMessage()));
			return false;
		}
	}
	
	//Obtener un claim especifico 'Username'
	public String getUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	//Obtener un solo claim
	public <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
		Claims claims = extractAllClaims(token);
		return claimsFunction.apply(claims);
	}
	
	
	//Obtener todos los claims del token
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignatureKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	
	//Obtener firma del token
	public Key getSignatureKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}

