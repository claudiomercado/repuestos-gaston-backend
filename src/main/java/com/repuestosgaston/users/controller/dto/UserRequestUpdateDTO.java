package com.repuestosgaston.users.controller.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestUpdateDTO {

	@JsonProperty("password")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonProperty("surname")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String surname;
	@JsonProperty("birthdate")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDate birthdate;
}
