package com.porter_replica.auth_service.auth.dto;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;	

@Getter
@Setter
public class RegisterRequestDTO {

	@NotBlank(message = AuthConstants.MSG_NAME_IS_REQUIRED)
	private String name;

	private String email;
	private String phone;

	@NotBlank(message = AuthConstants.MSG_PASSWORD_IS_REQUIRED)
	private String password;

	@NotNull(message = AuthConstants.MSG_ROLE_IS_REQUIRED)
	private Role role;

}
