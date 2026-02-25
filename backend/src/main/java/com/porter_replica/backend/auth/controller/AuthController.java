package com.porter_replica.backend.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.porter_replica.backend.auth.AuthService;
import com.porter_replica.backend.auth.dto.LoginRequest;
import com.porter_replica.backend.auth.dto.LoginResponse;
import com.porter_replica.backend.auth.dto.RegisterRequest;
import com.porter_replica.backend.auth.security.CustomUserPrincipal;
import com.porter_replica.backend.auth.session.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	
	@Autowired
	private SessionService sessionService;

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
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout( @AuthenticationPrincipal CustomUserPrincipal user){
		
		if (user == null) {
            return ResponseEntity.status(401).build();
        }
		
		sessionService.endSession(
                user.getSessionId(),
                user.getUserId()
        );
		
		return ResponseEntity.ok("Logged out successfully");
	}

}
