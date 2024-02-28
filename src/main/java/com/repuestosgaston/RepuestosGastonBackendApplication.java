package com.repuestosgaston;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;
import com.repuestosgaston.users.repository.UserRepository;


@SpringBootApplication
public class RepuestosGastonBackendApplication {

	private static Logger logger = LoggerFactory.getLogger(RepuestosGastonBackendApplication.class);
	
	public static void main(String[] args) {
		try {
			SpringApplication.run(RepuestosGastonBackendApplication.class, args);
		} catch (Exception e) {
			logger.error(""+e.getMessage());
		}
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/login")
						.allowedOrigins("http://localhost:4200")
						.allowedMethods("/*")
						.allowedHeaders("/*");
			}
		};
	}
}
