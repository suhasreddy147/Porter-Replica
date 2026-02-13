package com.porter_replica.backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porter_replica.backend.auth.dto.LoginRequest;
import com.porter_replica.backend.auth.dto.LoginResponse;
import com.porter_replica.backend.auth.dto.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(
			@Valid @RequestBody RegisterRequest request) {

		authService.register(request);
		return ResponseEntity.ok("User registered successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest request) {

		return ResponseEntity.ok(authService.login(request));
	}
	
	//test method for JWT token testing, to be removed later
	@GetMapping("/me")
	public ResponseEntity<String> me(Authentication authentication) {
	    return ResponseEntity.ok("Authenticated user ID: " + authentication.getPrincipal());
	}

}
