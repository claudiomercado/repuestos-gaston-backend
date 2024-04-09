package com.repuestosgaston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
