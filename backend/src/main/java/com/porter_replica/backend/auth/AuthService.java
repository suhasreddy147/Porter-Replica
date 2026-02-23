package com.porter_replica.backend.auth;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.porter_replica.backend.auth.dto.LoginRequest;
import com.porter_replica.backend.auth.dto.LoginResponse;
import com.porter_replica.backend.auth.dto.RegisterRequest;
import com.porter_replica.backend.auth.jwt.JwtUtil;
import com.porter_replica.backend.auth.session.SessionService;
import com.porter_replica.backend.user.User;
import com.porter_replica.backend.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
		
	private SessionService sessionService; 
	
	JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository,
			BCryptPasswordEncoder passwordEncoder, 
			SessionService sessionService, 
			JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.sessionService = sessionService;
	}

	public void register(RegisterRequest request) {

		if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
			throw new IllegalArgumentException("Email or phone is required");
		}
		
		if (!StringUtils.hasText(request.getPassword())) {
			throw new IllegalArgumentException("Password is required");
		}
		
		if (request.getRole() == null) {
			throw new IllegalArgumentException("Role is required");
		}
		
		if (!StringUtils.hasText(request.getName())) {
			throw new IllegalArgumentException("Name is required");
		}
		
		if (!StringUtils.hasText(request.getPassword())) {
			throw new IllegalArgumentException("Password is required");
		}

		if (StringUtils.hasText(request.getEmail()) &&
				userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email is already registered");
		}

		if (StringUtils.hasText(request.getPhone()) &&
				userRepository.findByPhone(request.getPhone()).isPresent()) {
			throw new IllegalArgumentException("Phone is already registered");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setRole(request.getRole());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(user);
	}
	
	public LoginResponse login(LoginRequest request) {

		if (!StringUtils.hasText(request.getIdentifier()) || !StringUtils.hasText(request.getPassword())) {
	        throw new IllegalArgumentException("Identifier and password are required");
	    }
		
	    User user = userRepository.findByEmailOrPhone(request.getIdentifier())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new IllegalArgumentException("Invalid credentials");
	    }
	    
	    UUID sessionId = UUID.randomUUID();
	    
	    sessionService.createSession(user, sessionId);
	    String token = jwtUtil.generateToken(user, sessionId);
	    return new LoginResponse(token);
	}

}
