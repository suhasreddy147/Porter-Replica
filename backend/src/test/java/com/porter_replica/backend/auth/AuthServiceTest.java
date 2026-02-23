package com.porter_replica.backend.auth;

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

import com.porter_replica.backend.auth.dto.LoginRequest;
import com.porter_replica.backend.auth.dto.LoginResponse;
import com.porter_replica.backend.auth.dto.RegisterRequest;
import com.porter_replica.backend.auth.jwt.JwtUtil;
import com.porter_replica.backend.auth.session.SessionService;
import com.porter_replica.backend.auth.session.UserSessionsRepository;
import com.porter_replica.backend.user.Role;
import com.porter_replica.backend.user.User;
import com.porter_replica.backend.user.UserRepository;

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
		user.setEmail("test@test.com");
		user.setPassword("encodedPassword");
		user.setRole(Role.CUSTOMER);
	}
	
	// =========================
	// REGISTER TESTS
	// =========================

	@Test
	void shouldRegisterUserWithEmail() {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setName("Test User");
		registerRequest.setEmail("test@email.com");
		registerRequest.setPassword("password123");
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(registerRequest.getEmail()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn("encodedPassword");

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}
	
	@Test
	void shouldRegisterUserWithPhone() {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setName("Test User");
		registerRequest.setPhone("9999999999");
		registerRequest.setPassword("password123");
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByPhone(registerRequest.getPhone()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn("encodedPassword");

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}
	
	@Test
	void shouldRegisterUserWithEmailAndPhone() {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setName("Test User");
		registerRequest.setEmail("test@email.com");
		registerRequest.setPhone("9999999999");
		registerRequest.setPassword("password123");
		registerRequest.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(registerRequest.getEmail()))
		.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any()))
		.thenReturn("encodedPassword");

		authService.register(registerRequest);

		verify(userRepository).save(any(User.class));
	}

	@Test
	void shouldFailWhenEmailAlreadyExists() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@test.com");
		request.setPassword("password123");
		request.setName("Test User");
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(request.getEmail()))
		.thenReturn(Optional.of(user));

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPhoneAlreadyExists() {

		RegisterRequest request = new RegisterRequest();
		request.setPhone("9999999999");
		request.setPassword("password123");
		request.setName("Test User");
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByPhone(request.getPhone()))
		.thenReturn(Optional.of(user));

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPhoneAlreadyExistsButEmailDoesNotExist() {

		RegisterRequest request = new RegisterRequest();
		request.setPhone("9999999999");
		request.setEmail("test@test.com");
		request.setPassword("password123");
		request.setName("Test User");
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

		RegisterRequest request = new RegisterRequest();
		request.setPhone("9999999999");
		request.setEmail("test@test.com");
		request.setPassword("password123");
		request.setName("Test User");
		request.setRole(Role.CUSTOMER);

		when(userRepository.findByEmail(request.getEmail()))
		.thenReturn(Optional.of(user));
		
		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}

	@Test
	void shouldFailWhenEmailAndPhoneMissing() {

		RegisterRequest request = new RegisterRequest();
		request.setPassword("password123");

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenPasswordIsMissing() {

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@test.com");

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}
	
	@Test
	void shouldFailWhenBlank() {

		RegisterRequest request = new RegisterRequest();

		assertThrows(IllegalArgumentException.class,
				() -> authService.register(request));
	}

	// =========================
	// LOGIN TESTS
	// =========================

	@Test
	void shouldLoginSuccessfullyWithEmail() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("test@test.com");
		request.setPassword("password123");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches("password123", "encodedPassword"))
		.thenReturn(true);

		when(jwtUtil.generateToken(any(User.class), any(UUID.class)))
		.thenReturn("mock-token");

		LoginResponse response = authService.login(request);

		assertNotNull(response);
		assertEquals("mock-token", response.getAccessToken());

		verify(sessionService).createSession(eq(user), any(UUID.class));
	}
	
	@Test
	void shouldLoginSuccessfullyWithPhone() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("9999999999");
		request.setPassword("password123");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches("password123", "encodedPassword"))
		.thenReturn(true);

		when(jwtUtil.generateToken(any(User.class), any(UUID.class)))
		.thenReturn("mock-token");

		LoginResponse response = authService.login(request);

		assertNotNull(response);
		assertEquals("mock-token", response.getAccessToken());

		verify(sessionService).createSession(eq(user), any(UUID.class));
	}

	@Test
	void shouldFailLoginWhenPhoneNotFound() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("9999999999");
		request.setPassword("password123");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}
	
	@Test
	void shouldFailLoginWhenEmailNotFound() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("notfound@test.com");
		request.setPassword("password123");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}

	@Test
	void shouldFailLoginWhenPasswordIncorrectForPhone() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("9999999999");
		request.setPassword("wrongPassword");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
		.thenReturn(false);

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}
	
	@Test
	void shouldFailLoginWhenPasswordIncorrectForEmail() {

		LoginRequest request = new LoginRequest();
		request.setIdentifier("test@test.com");
		request.setPassword("wrongPassword");

		when(userRepository.findByEmailOrPhone(request.getIdentifier()))
		.thenReturn(Optional.of(user));

		when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
		.thenReturn(false);

		assertThrows(IllegalArgumentException.class,
				() -> authService.login(request));
	}

}
