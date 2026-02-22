package com.porter_replica.backend.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.porter_replica.backend.auth.dto.LoginRequest;
import com.porter_replica.backend.auth.dto.LoginResponse;
import com.porter_replica.backend.auth.dto.RegisterRequest;
import com.porter_replica.backend.auth.jwt.JwtUtil;
import com.porter_replica.backend.auth.session.UserSessions;
import com.porter_replica.backend.auth.session.UserSessionsRepository;
import com.porter_replica.backend.user.User;
import com.porter_replica.backend.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserSessionsRepository userSessionsRepository;
	
	@Autowired
	JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void register(RegisterRequest request) {

		if (request.getEmail() == null && request.getPhone() == null) {
			throw new IllegalArgumentException("Email or phone is required");
		}

		if (request.getEmail() != null &&
				userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email is already registered");
		}

		if (request.getPhone() != null &&
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

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new IllegalArgumentException("Invalid credentials");
	    }
	    
	    UUID sessionId = UUID.randomUUID();
	    
	    UserSessions session = new UserSessions();
	    session.setUser(user);
	    session.setSessionId(sessionId);
	    session.setStartedAt(LocalDateTime.now());
	    session.setLastActivityAt(LocalDateTime.now());
	    userSessionsRepository.save(session);
	    String token = jwtUtil.generateToken(user, sessionId);
	    return new LoginResponse(token);
	}

}
