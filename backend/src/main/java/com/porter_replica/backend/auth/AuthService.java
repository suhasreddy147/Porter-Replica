package com.porter_replica.backend.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.porter_replica.backend.auth.dto.RegisterRequest;
import com.porter_replica.backend.user.User;
import com.porter_replica.backend.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

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
			throw new IllegalArgumentException("Email already registered");
		}

		if (request.getPhone() != null &&
				userRepository.findByPhone(request.getPhone()).isPresent()) {
			throw new IllegalArgumentException("Phone already registered");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setRole(request.getRole());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(user);
	}
}
