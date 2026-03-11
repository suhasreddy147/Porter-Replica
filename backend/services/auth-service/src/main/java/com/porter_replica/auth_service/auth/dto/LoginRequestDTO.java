package com.porter_replica.auth_service.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

	@NotBlank
	private String identifier;
	
	@NotBlank
	private String password;
	
}
