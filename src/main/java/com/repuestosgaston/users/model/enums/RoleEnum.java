package com.repuestosgaston.users.model.enums;

public enum RoleEnum {
	
	ADMIN("Administrador"),
    USER("Usuario");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
