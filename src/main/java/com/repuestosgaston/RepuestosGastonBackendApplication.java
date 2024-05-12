package com.repuestosgaston;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
import com.repuestosgaston.users.repository.RolRepository;
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
	    public CommandLineRunner initData(UserRepository userRepository, RolRepository rolRepository,PasswordEncoder passwordEncoder) {
	        return args -> {
	        	UserEntity adminUser = UserEntity.builder()
	                    .email("admin@gmail.com")
	                    .username("admin")
	                    .password(passwordEncoder.encode("admin"))
	                    .name("Gastón")
	                    .surname("Mercado")
	                    .dni("39.393.935")
	                    .birthdate(LocalDate.parse("1996-05-14"))
	                    .build();	
	        	userRepository.save(adminUser);
	        	
	        	
	        	RoleEntity rol = new RoleEntity();
	        	Set<RoleEntity> roles = new HashSet<>();
	        	rol.setId((long) 2);
	        	rol.setName(RoleEnum.ADMIN);
	        	roles.add(rol);
	        	adminUser.setRoles(roles);
	        	/*Set<RoleEntity> roles = new HashSet<>();
	        	roles.add(rol);
	    		RoleEntity rolEntity = new RoleEntity();*/
	    		/*roles.add(rolEntity);
	        	adminUser.setRoles(roles);
	    		rolRepository.save(rol);*/
	            userRepository.save(adminUser);
	        };
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
