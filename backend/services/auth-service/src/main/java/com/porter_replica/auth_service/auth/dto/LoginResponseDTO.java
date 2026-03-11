package com.porter_replica.auth_service.auth.dto;

import com.porter_replica.auth_service.auth.constants.AuthConstants;

import lombok.Getter;

@Getter
public class LoginResponseDTO {

	private String accessToken;
	private String tokenType = AuthConstants.BEARER;

	public LoginResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}

}
