package com.porter_replica.auth_service.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.dto.LoginRequestDTO;
import com.porter_replica.auth_service.auth.dto.LoginResponseDTO;
import com.porter_replica.auth_service.auth.dto.RegisterRequestDTO;
import com.porter_replica.auth_service.auth.security.principal.CustomUserPrincipal;
import com.porter_replica.auth_service.auth.service.AuthService;
import com.porter_replica.auth_service.auth.service.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AuthConstants.API_AUTH_PARENT_ENDPOINT)
@CrossOrigin(origins = "*")
public class AuthController {
	
	private final AuthService authService;
	
	@Autowired
	private SessionService sessionService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(AuthConstants.API_REGISTER_ENDPOINT)
	public ResponseEntity<?> register(
			@Valid @RequestBody RegisterRequestDTO request) {

		authService.register(request);
		return ResponseEntity.ok(AuthConstants.MSG_USER_REG_SUCCESSFULLY);
	}
	
	@PostMapping(AuthConstants.API_LOGIN_ENDPOINT)
	public ResponseEntity<LoginResponseDTO> login(
			@Valid @RequestBody LoginRequestDTO request) {

		return ResponseEntity.ok(authService.login(request));
	}
	
	@PostMapping(AuthConstants.API_LOGOUT_ENDPOINT)
	public ResponseEntity<?> logout( @AuthenticationPrincipal CustomUserPrincipal user){
		
		if (user == null) {
            return ResponseEntity.status(401).build();
        }
		
		sessionService.endSession(
                user.getSessionId(),
                user.getUserId()
        );
		
		return ResponseEntity.ok(AuthConstants.MSG_LOGGED_OUT_SUCCESSFULLY);
	}

}
