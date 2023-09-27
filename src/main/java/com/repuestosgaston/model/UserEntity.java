package com.repuestosgaston.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private  Long id;
	
	@Column
//	@Email
	@NotBlank
	@Size(max = 80)
	private String email;
	
	@Column
	@NotBlank
	@Size(max = 30)
	private String username;
	
	@Column
	@NotBlank
	private String password;
	
	@Column
	@NotBlank
	@Size(min = 3, max = 30)
	private String name;
	
	@Column
	@NotBlank
	@Size(min = 3, max = 30)
	private String surname;
	
	@Column
	@NotBlank
	private String dni;
	
	@Column
	@NotNull
	private LocalDate birthdate;
	
	@Column
	@NotBlank
	private String address;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = RolEntity.class,cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id"))
	private Set<RolEntity> roles;
}
