package com.repuestosgaston.users.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repuestosgaston.users.model.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolResponseDTO {
	
	@JsonProperty("rol_id")
	private Long id;
	@JsonProperty("name")
	private String name;
	
}
