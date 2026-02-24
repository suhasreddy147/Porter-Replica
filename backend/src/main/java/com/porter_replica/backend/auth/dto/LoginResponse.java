package com.porter_replica.backend.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

	private String accessToken;
	private String tokenType = "Bearer";

	public LoginResponse(String accessToken) {
		this.accessToken = accessToken;
	}

}
