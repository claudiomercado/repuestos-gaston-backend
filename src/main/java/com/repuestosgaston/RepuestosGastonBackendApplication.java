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
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.repuestosgaston.shopping_cart.model.ShoppingCartEntity;
import com.repuestosgaston.shopping_cart.service.ShoppingCartService;
import com.repuestosgaston.users.controller.dto.UserRequestCreateDTO;
import com.repuestosgaston.users.model.RoleEntity;
import com.repuestosgaston.users.model.UserEntity;
import com.repuestosgaston.users.model.enums.RoleEnum;
import com.repuestosgaston.users.repository.RolRepository;
import com.repuestosgaston.users.repository.UserRepository;
import com.repuestosgaston.users.service.RolService;
import com.repuestosgaston.users.service.UserService;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.CorsFilter;


@SpringBootApplication
public class RepuestosGastonBackendApplication {
	private static Logger logger = LoggerFactory.getLogger(RepuestosGastonBackendApplication.class);
	
	public static void main(String[] args) {
			SpringApplication.run(RepuestosGastonBackendApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner initData(UserService userService , UserRepository userRepository, RolService rolService,RolRepository rolRepository,PasswordEncoder passwordEncoder,ShoppingCartService shoppingCartService) {
		 return args -> {
			 UserRequestCreateDTO userRequestDTO = UserRequestCreateDTO.builder()
					 .email("admin@gmail.com")
	                 .username("admin")
	                 .password("admin")
	                 .name("Gastón")
	                 .surname("Mercado")
	                 .dni("39.393.935")
	                 .birthdate(LocalDate.parse("1996-05-14"))
	                 .build();
			 userService.createAdmin(userRequestDTO);

//		 return args -> {
//		        Set<RoleEntity> roles = rolService.createRoleAdmin();
//		        roles.stream().forEach(x -> rolRepository.save(x));
//		        
//		        
//	        	UserEntity adminUser = UserEntity.builder()
//	                    .email("admin@gmail.com")
//	                    .username("admin")
//	                    .password(passwordEncoder.encode("admin"))
//	                    .name("Gastón")
//	                    .surname("Mercado")
//	                    .dni("39.393.935")
//	                    .birthdate(LocalDate.parse("1996-05-14"))
//	                    .cart(shoppingCartService.createShoppingCart())
//	                    .roles(roles)
//	                    .build();	
//	            userRepository.save(adminUser);
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
