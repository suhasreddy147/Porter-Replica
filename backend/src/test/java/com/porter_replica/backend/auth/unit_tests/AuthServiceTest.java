package com.porter_replica.backend.auth.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.porter_replica.backend.auth.constants.AuthConstants;
import com.porter_replica.backend.auth.dto.LoginRequestDTO;
import com.porter_replica.backend.auth.dto.LoginResponseDTO;
import com.porter_replica.backend.auth.dto.RegisterRequestDTO;
import com.porter_replica.backend.auth.entity.User;
import com.porter_replica.backend.auth.enums.Role;
import com.porter_replica.backend.auth.repository.UserRepository;
import com.porter_replica.backend.auth.repository.UserSessionsRepository;
import com.porter_replica.backend.auth.security.jwt.util.JwtUtil;
import com.porter_replica.backend.auth.service.AuthService;
import com.porter_replica.backend.auth.service.SessionService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserSessionsRepository userSessionsRepository;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private SessionService sessionService;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	private User user;

	@BeforeEach
	void setup() {
		user = new User();
		user.setId(1L);
		user.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		user.setPassword(AuthConstants.TEST_ENCODED_PASWORD);
		user.setRole(Role.CUSTOMER);
	}
	
	// =========================
	// REGISTER TESTS
	// =========================

	@Test
	void shouldRegisterUserWithEmail() {
		RegisterRequestDTO registerRequest = new RegisterRequestDTO();
		registerRequest.setName(AuthConstants.TEST_USER_NAME);
		registerRequest.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		registerRequest.setPassword(AuthConstants.TEST_PASSWORD);
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(registerRequest.getEmail()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn(AuthConstants.TEST_ENCODED_PASWORD);

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}
	
	@Test
	void shouldRegisterUserWithPhone() {
		RegisterRequestDTO registerRequest = new RegisterRequestDTO();
		registerRequest.setName(AuthConstants.TEST_USER_NAME);
		registerRequest.setPhone(AuthConstants.TEST_PHONE_NUMBER);
		registerRequest.setPassword(AuthConstants.TEST_PASSWORD);
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByPhone(registerRequest.getPhone()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn(AuthConstants.TEST_ENCODED_PASWORD);

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}
	
	@Test
	void shouldRegisterUserWithEmailAndPhone() {
		RegisterRequestDTO registerRequest = new RegisterRequestDTO();
		registerRequest.setName(AuthConstants.TEST_USER_NAME);
		registerRequest.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		registerRequest.setPhone(AuthConstants.TEST_PHONE_NUMBER);
		registerRequest.setPassword(AuthConstants.TEST_PASSWORD);
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(registerRequest.getEmail()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn(AuthConstants.TEST_ENCODED_PASWORD);

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}

	@Test
	void shouldFailWhenEmailAlreadyExists() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_PASSWORD);
		request.setName(AuthConstants.TEST_USER_NAME);
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(request.getEmail()))
		.thenReturn(Optional.of(user));

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPhoneAlreadyExists() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPhone(AuthConstants.TEST_PHONE_NUMBER);
		request.setPassword(AuthConstants.TEST_PASSWORD);
		request.setName(AuthConstants.TEST_USER_NAME);
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByPhone(request.getPhone()))
		.thenReturn(Optional.of(user));

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPhoneAlreadyExistsButEmailDoesNotExist() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPhone(AuthConstants.TEST_PHONE_NUMBER);
		request.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_PASSWORD);
		request.setName(AuthConstants.TEST_USER_NAME);
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByPhone(request.getPhone()))
		.thenReturn(Optional.of(user));
		
		when(userRepository.findByEmail(request.getEmail()))
		.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenEmailAlreadyExistsButPhoneDoesNotExist() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPhone(AuthConstants.TEST_PHONE_NUMBER);
		request.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_PASSWORD);
		request.setName(AuthConstants.TEST_USER_NAME);
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(request.getEmail()))
		.thenReturn(Optional.of(user));
		
		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}

	@Test
	void shouldFailWhenEmailAndPhoneMissing() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setPassword(AuthConstants.TEST_PASSWORD);

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPasswordIsMissing() {

		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setEmail(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenBlank() {

		RegisterRequestDTO request = new RegisterRequestDTO();

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}

	// =========================
	// LOGIN TESTS
	// =========================

	@Test
	void shouldLoginSuccessfullyWithEmail() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_PASSWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches(AuthConstants.TEST_PASSWORD, AuthConstants.TEST_ENCODED_PASWORD))
		.thenReturn(true);

		when(jwtUtil.generateToken(any(User.class), any(UUID.class)))
		.thenReturn(AuthConstants.MOCK_TOKEN);

		LoginResponseDTO response = authService.login(request);

		assertNotNull(response);
		assertEquals(AuthConstants.MOCK_TOKEN, response.getAccessToken());

		verify(sessionService).createSession(eq(user), any(UUID.class));
	}
	
	@Test
	void shouldLoginSuccessfullyWithPhone() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_PHONE_NUMBER);
		request.setPassword(AuthConstants.TEST_PASSWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches(AuthConstants.TEST_PASSWORD, AuthConstants.TEST_ENCODED_PASWORD))
		.thenReturn(true);

		when(jwtUtil.generateToken(any(User.class), any(UUID.class)))
		.thenReturn(AuthConstants.MOCK_TOKEN);

		LoginResponseDTO response = authService.login(request);

		assertNotNull(response);
		assertEquals(AuthConstants.MOCK_TOKEN, response.getAccessToken());

		verify(sessionService).createSession(eq(user), any(UUID.class));
	}

	@Test
	void shouldFailLoginWhenPhoneNotFound() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_PHONE_NUMBER);
		request.setPassword(AuthConstants.TEST_PASSWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}
	
	@Test
	void shouldFailLoginWhenEmailNotFound() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_PASSWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}

	@Test
	void shouldFailLoginWhenPasswordIncorrectForPhone() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_PHONE_NUMBER);
		request.setPassword(AuthConstants.TEST_WRONG_PASWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches(AuthConstants.TEST_WRONG_PASWORD, AuthConstants.TEST_ENCODED_PASWORD))
		.thenReturn(false);

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}
	
	@Test
	void shouldFailLoginWhenPasswordIncorrectForEmail() {

		LoginRequestDTO request = new LoginRequestDTO();
		request.setIdentifier(AuthConstants.TEST_EMAIL_USERNAME + AuthConstants.TEST_EMAIL_DOMAIN);
		request.setPassword(AuthConstants.TEST_WRONG_PASWORD);

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches(AuthConstants.TEST_WRONG_PASWORD, AuthConstants.TEST_ENCODED_PASWORD))
		.thenReturn(false);

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}

}
