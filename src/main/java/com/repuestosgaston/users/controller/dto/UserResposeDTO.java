package com.repuestosgaston.users.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserResposeDTO {
	
	@JsonProperty
	private  Long id;
	@JsonProperty
	private String email;
	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private String name;
	@JsonProperty
	private String surname;
	@JsonProperty
	private String dni;
	@JsonProperty
	private LocalDate birthdate;
	@JsonProperty
	private String address;
	@JsonProperty	
	private Set<RolResponseDTO> roles;
}
