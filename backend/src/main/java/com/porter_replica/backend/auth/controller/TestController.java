package com.porter_replica.backend.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/auth/test")
public class TestController {

	//test method for JWT token testing, to be removed later
	@GetMapping("/jwttokentest")
	public ResponseEntity<String> me(Authentication authentication, HttpServletRequest request) {
		return ResponseEntity.ok("Authenticated user ID: " + authentication.getPrincipal() + 
				" | Session id: " + ((String) request.getAttribute("sessionId")));
	}

	@GetMapping("/customer")
	public ResponseEntity<String> customerApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("UNKNOWN");
		return ResponseEntity.ok("Authenticated user ID: " + authentication.getPrincipal() + 
				" | Session id: " + ((String) request.getAttribute("sessionId")) + " | Role: " + role);
	}

	@GetMapping("/driver")
	public ResponseEntity<String> driverApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("UNKNOWN");
		return ResponseEntity.ok("Authenticated user ID: " + authentication.getPrincipal() + 
				" | Session id: " + ((String) request.getAttribute("sessionId")) + " | Role: " + role);
	}

	@GetMapping("/admin")
	public ResponseEntity<String> adminApi(Authentication authentication, HttpServletRequest request) {
		String role = authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("UNKNOWN");
		return ResponseEntity.ok("Authenticated user ID: " + authentication.getPrincipal() + 
				" | Session id: " + ((String) request.getAttribute("sessionId")) + " | Role: " + role);
	}

}
