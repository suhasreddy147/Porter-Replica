package com.porter_replica.backend.auth.dto;

import com.porter_replica.backend.user.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;	

@Getter
@Setter
public class RegisterRequest {

	@NotBlank(message = "Name is required")
	private String name;

	private String email;
	private String phone;

	@NotBlank(message = "Password is required")
	private String password;

	@NotNull(message = "Role is required")
	private Role role;

}
