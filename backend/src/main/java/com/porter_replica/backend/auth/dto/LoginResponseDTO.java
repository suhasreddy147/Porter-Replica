package com.porter_replica.backend.auth.dto;

import com.porter_replica.backend.auth.constants.AuthConstants;

import lombok.Getter;

@Getter
public class LoginResponseDTO {

	private String accessToken;
	private String tokenType = AuthConstants.BEARER;

	public LoginResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}

}
