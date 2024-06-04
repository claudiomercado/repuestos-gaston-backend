package com.repuestosgaston.users.controller.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseAdminDTO {
	
	@JsonProperty("id")
	private Long id;
	@JsonProperty("email")
	private String email;
	@JsonProperty("username")
	private String username;
	@JsonProperty("name")
	private String name;
	@JsonProperty("surname")
	private String surname;
	@JsonProperty("dni")
	private String dni;
	@JsonProperty("birthdate")
	private LocalDate birthdate;
}
