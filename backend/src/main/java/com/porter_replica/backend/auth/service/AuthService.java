package com.porter_replica.backend.auth.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.porter_replica.backend.auth.constants.AuthConstants;
import com.porter_replica.backend.auth.dto.LoginRequestDTO;
import com.porter_replica.backend.auth.dto.LoginResponseDTO;
import com.porter_replica.backend.auth.dto.RegisterRequestDTO;
import com.porter_replica.backend.auth.entity.User;
import com.porter_replica.backend.auth.repository.UserRepository;
import com.porter_replica.backend.auth.security.jwt.util.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
		
	private SessionService sessionService; 
	
	private JwtUtil jwtUtil;

	@Transactional
	public void register(RegisterRequestDTO request) {

		if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
			throw new IllegalArgumentException(AuthConstants.MSG_EMAIL_OR_PHONE_IS_REQUIRED);
		}
		
		if (!StringUtils.hasText(request.getPassword())) {
			throw new IllegalArgumentException(AuthConstants.MSG_PASSWORD_IS_REQUIRED);
		}
		
		if (request.getRole() == null) {
			throw new IllegalArgumentException(AuthConstants.MSG_ROLE_IS_REQUIRED);
		}
		
		if (!StringUtils.hasText(request.getName())) {
			throw new IllegalArgumentException(AuthConstants.MSG_NAME_IS_REQUIRED);
		}

		if (StringUtils.hasText(request.getEmail()) &&
				userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException(AuthConstants.MSG_EMAIL_IS_ALREADY_REGISTERED);
		}

		if (StringUtils.hasText(request.getPhone()) &&
				userRepository.findByPhone(request.getPhone()).isPresent()) {
			throw new IllegalArgumentException(AuthConstants.MSG_PHONE_IS_ALREADY_REGISTERED);
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setRole(request.getRole());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User createdUser = userRepository.save(user);
		Authentication auth =
	            SecurityContextHolder.getContext().getAuthentication();
		
		if(auth == null || !auth.isAuthenticated()) {
			createdUser.setCreatedBy(createdUser.getId());
		}
	}
	
	public LoginResponseDTO login(LoginRequestDTO request) {

		if (!StringUtils.hasText(request.getIdentifier()) || !StringUtils.hasText(request.getPassword())) {
	        throw new IllegalArgumentException(AuthConstants.MSG_IDENTIFIER_AND_PASSWORD_ARE_REQUIRED);
	    }
		
	    User user = userRepository.findByEmailOrPhone(request.getIdentifier())
	            .orElseThrow(() -> new IllegalArgumentException(AuthConstants.MSG_INVALID_CREDS));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new IllegalArgumentException(AuthConstants.MSG_INVALID_CREDS);
	    }
	    
	    UUID sessionId = UUID.randomUUID();
	    
	    sessionService.createSession(user, sessionId);
	    String token = jwtUtil.generateToken(user, sessionId);
	    return new LoginResponseDTO(token);
	}

}
