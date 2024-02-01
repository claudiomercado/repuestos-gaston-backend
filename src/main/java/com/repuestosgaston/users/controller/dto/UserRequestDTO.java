package com.repuestosgaston.users.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.shopping_cart.controller.dto.ShoppingCartRequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

	@JsonProperty("email")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	@JsonProperty("username")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String username;
	@JsonProperty("password")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonProperty("surname")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String surname;
	@JsonProperty("dni")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String dni;
	@JsonProperty("birthdate")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDate birthdate;
}
