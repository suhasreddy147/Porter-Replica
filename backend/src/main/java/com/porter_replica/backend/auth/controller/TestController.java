package com.porter_replica.backend.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.porter_replica.backend.auth.constants.AuthConstants;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(AuthConstants.API_AUTH_PARENT_ENDPOINT+AuthConstants.API_TEST_ENDPOINT)
public class TestController {

	//test method for JWT token testing, to be removed later
	@GetMapping(AuthConstants.API_JWT_TOKEN_TEST_ENDPOINT)
	public ResponseEntity<String> me(Authentication authentication, HttpServletRequest request) {
		return ResponseEntity.ok(AuthConstants.AUTHENTICATED_USER_ID_COLON + authentication.getPrincipal() + 
				AuthConstants.PIPE_SEPARATOR + AuthConstants.SESSION_ID_COLON + ((String) request.getAttribute(AuthConstants.SESSION_ID)));
	}

	@GetMapping(AuthConstants.API_CUSTOMER_ENDPOINT)
	public ResponseEntity<String> customerApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse(AuthConstants.UNKNOWN);
		return ResponseEntity.ok(AuthConstants.AUTHENTICATED_USER_ID_COLON + authentication.getPrincipal() + 
				AuthConstants.PIPE_SEPARATOR + AuthConstants.SESSION_ID_COLON + ((String) request.getAttribute(AuthConstants.SESSION_ID)) + AuthConstants.PIPE_SEPARATOR +AuthConstants.ROLE_COLON + role);
	}

	@GetMapping(AuthConstants.API_DRIVER_ENDPOINT)
	public ResponseEntity<String> driverApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse(AuthConstants.UNKNOWN);
		return ResponseEntity.ok(AuthConstants.AUTHENTICATED_USER_ID_COLON + authentication.getPrincipal() + 
				AuthConstants.PIPE_SEPARATOR + AuthConstants.SESSION_ID_COLON + ((String) request.getAttribute(AuthConstants.SESSION_ID)) + AuthConstants.PIPE_SEPARATOR +AuthConstants.ROLE_COLON + role);
	}

	@GetMapping(AuthConstants.API_ADMIN_ENDPOINT)
	public ResponseEntity<String> adminApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse(AuthConstants.UNKNOWN);
		return ResponseEntity.ok(AuthConstants.AUTHENTICATED_USER_ID_COLON + authentication.getPrincipal() + 
				AuthConstants.PIPE_SEPARATOR + AuthConstants.SESSION_ID_COLON + ((String) request.getAttribute(AuthConstants.SESSION_ID)) + AuthConstants.PIPE_SEPARATOR +AuthConstants.ROLE_COLON + role);
	}

}
