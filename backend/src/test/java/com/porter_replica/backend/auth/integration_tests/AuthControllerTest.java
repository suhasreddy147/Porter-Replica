package com.porter_replica.backend.auth.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.porter_replica.backend.auth.constants.AuthConstants;
import com.porter_replica.backend.auth.entity.User;
import com.porter_replica.backend.auth.entity.UserSessions;
import com.porter_replica.backend.auth.enums.Role;
import com.porter_replica.backend.auth.repository.UserSessionsRepository;
import com.porter_replica.backend.auth.security.jwt.util.JwtUtil;

import io.jsonwebtoken.Claims;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	static class TestUser{
		String email;
		String password;
		String phone;

		public TestUser(String email, String password, String phone) {
			this.email = email;
			this.password = password;
			this.phone = phone;
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserSessionsRepository userSessionsRepository;

	@Autowired
	private JwtUtil jwtUtil;

	// =======================
	// Registration tests
	// =======================

	private TestUser registerTestUser() throws Exception {

		String email = AuthConstants.TEST_UNDERSCORE+UUID.randomUUID()+AuthConstants.TEST_EMAIL_DOMAIN;
		String password = AuthConstants.TEST_PASSWORD;
		String phone = AuthConstants.TEST_PHONE_EXT+ UUID.randomUUID().toString()
		        .replaceAll(AuthConstants.PHONE_PATTERN_MATCHER, AuthConstants.BLANK_STRING)
		        .substring(0, 10);

		String requestBody = """
				{
				  "name": "Test User",
				  "email": "%s",
				  "phone": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(email, phone);

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(content().string(AuthConstants.MSG_USER_REG_SUCCESSFULLY));

		return new TestUser(email, password, phone);
	}

	@Test
	void shouldRegisterUserSuccessfullyWithEmail() throws Exception {

		String uniqueEmail = AuthConstants.TEST_UNDERSCORE+UUID.randomUUID()+AuthConstants.TEST_EMAIL_DOMAIN;
		String requestBody = """
				{
				  "name": "JUnit Register",
				  "email": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(uniqueEmail);

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(content().string(AuthConstants.MSG_USER_REG_SUCCESSFULLY));
	}
	
	@Test
	void shouldRegisterUserSuccessfullyWithPhone() throws Exception {

		String uniquePhone =  AuthConstants.TEST_PHONE_EXT+ UUID.randomUUID().toString()
		        .replaceAll(AuthConstants.PHONE_PATTERN_MATCHER, AuthConstants.BLANK_STRING)
		        .substring(0, 10);
		String requestBody = """
				{
				  "name": "JUnit Register",
				  "phone": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(uniquePhone);

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(content().string(AuthConstants.MSG_USER_REG_SUCCESSFULLY));
	}
	
	@Test
	void shouldRegisterUserSuccessfullyWithEmailAndPhone() throws Exception {

		String uniquePhone =  AuthConstants.TEST_PHONE_EXT+ UUID.randomUUID().toString()
		        .replaceAll(AuthConstants.PHONE_PATTERN_MATCHER, AuthConstants.BLANK_STRING)
		        .substring(0, 10);
		String uniqueEmail = AuthConstants.TEST_UNDERSCORE+UUID.randomUUID()+AuthConstants.TEST_EMAIL_DOMAIN;
		String requestBody = """
				{
				  "name": "JUnit Register",
				  "phone": "%s",
				  "email": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(uniquePhone,uniqueEmail);

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(content().string(AuthConstants.MSG_USER_REG_SUCCESSFULLY));
	}


	@Test
	void shouldFailWhenNameIsMissing() throws Exception {

		String requestBody = """
				{
				  "email": "noname@test.com",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_NAME_IS_REQUIRED));
	}

	@Test
	void shouldFailWhenNameIsBlank() throws Exception {

		String requestBody = """
				{
				  "name": "",
				  "email": "blank@test.com",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_NAME_IS_REQUIRED));
	}

	@Test
	void shouldFailWhenPasswordIsMissing() throws Exception {

		String requestBody = """
				{
				  "name": "No Password",
				  "email": "nopassword@test.com",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_PASSWORD_IS_REQUIRED));
	}
	
	void shouldFailWhenPasswordIsBlank() throws Exception {

		String requestBody = """
				{
				  "name": "No Password",
				  "email": "nopassword@test.com",
				  "password": "",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_PASSWORD_IS_REQUIRED));
	}

	@Test
	void shouldFailWhenEmailAndPhoneMissing() throws Exception {

		String requestBody = """
				{
				  "name": "No Contact",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_EMAIL_OR_PHONE_IS_REQUIRED));
	}
	
	@Test
	void shouldFailWhenEmailIsBlank() throws Exception {

		String requestBody = """
				{
				  "name": "No Contact",
				  "email": "",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_EMAIL_OR_PHONE_IS_REQUIRED));
	}
	
	@Test
	void shouldFailWhenPhoneIsBlank() throws Exception {

		String requestBody = """
				{
				  "name": "No Contact",
				  "phone": "",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_EMAIL_OR_PHONE_IS_REQUIRED));
	}

	@Test
	void shouldFailForDuplicateEmail() throws Exception {

		TestUser testUser = registerTestUser();

		String requestBody = """
				{
				  "name": "Duplicate Email",
				  "email": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(testUser.email);

		// Second call (duplicate)
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_EMAIL_IS_ALREADY_REGISTERED));
	}
	
	@Test
	void shouldFailForDuplicatePhone() throws Exception {

		TestUser testUser = registerTestUser();

		String requestBody = """
				{
				  "name": "Duplicate Phone",
				  "phone": "%s",
				  "password": "password123",
				  "role": "CUSTOMER"
				}
				""".formatted(testUser.phone);

		// Second call (duplicate)
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_PHONE_IS_ALREADY_REGISTERED));
	}

	@Test
	void shouldFailForInvalidRole() throws Exception {

		String requestBody = """
				{
				  "name": "Invalid Role",
				  "email": "invalidrole@test.com",
				  "password": "password123",
				  "role": "INVALID"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldFailForBlankRole() throws Exception {

		String requestBody = """
				{
				  "name": "Invalid Role",
				  "email": "invalidrole@test.com",
				  "password": "password123",
				  "role": ""
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldFailWForMissingRole() throws Exception {

		String requestBody = """
				{
				  "name": "Invalid Role",
				  "email": "invalidrole@test.com",
				  "password": "password123"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest());
	}

	@Test
	void shouldFailForMalformedJson() throws Exception {

		String requestBody = """
				{
				  "name": "Bad JSON",
				  "email": "badjson@test.com",
				  "password": "password123",
				  "role": "CUSTOMER"
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest());
	}

	// =======================
	// Login tests
	// =======================

	@Test
	void shouldLoginSuccessfullyAndReturnJwtForEmail() throws Exception {

		TestUser testUser = registerTestUser();

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		//Now login with the newly registered user
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(jsonPath(AuthConstants.JSON_ACCESS_TOKEN_KEY).exists())
		.andExpect(jsonPath(AuthConstants.JSON_TOKEN_TYPE_KEY).value(AuthConstants.BEARER));
	}
	
	@Test
	void shouldLoginSuccessfullyAndReturnJwtForPhone() throws Exception {

		TestUser testUser = registerTestUser();

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.phone, testUser.password);

		//Now login with the newly registered user
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(jsonPath(AuthConstants.JSON_ACCESS_TOKEN_KEY).exists())
		.andExpect(jsonPath(AuthConstants.JSON_TOKEN_TYPE_KEY).value(AuthConstants.BEARER));
	}

	@Test
	void shouldFailLoginWithInvalidPassword() throws Exception {

		TestUser testUser = registerTestUser(); 	

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "wrongpassword"
				}
				""".formatted(testUser.email);

		//Now login with wrong password
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_INVALID_CREDS));
	}

	@Test
	void shouldFailLoginForNonExistentUser() throws Exception {

		String requestBody = """
				{
				  "identifier": "nouser@test.com",
				  "password": "password123"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(AuthConstants.JSON_MESSAGE_KEY).value(AuthConstants.MSG_INVALID_CREDS));
	}

	@Test
	void shouldFailLoginWhenPasswordMissing() throws Exception {

		String requestBody = """
				{
				  "identifier": "loginuser@test.com"
				}
				""";

		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isBadRequest());
	}

	// =======================
	// JWT security tests
	// =======================

	@Test
	void shouldRejectAccessToProtectedEndpointWithoutToken() throws Exception {

		mockMvc.perform(get(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_JWT_TOKEN_TEST_ENDPOINT))
		.andExpect(status().isUnauthorized());
	}

	@Test
	void shouldAllowAccessToProtectedEndpointWithValidToken() throws Exception {

		TestUser testUser = registerTestUser(); 

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		String response = mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = objectMapper.readTree(response)
				.get(AuthConstants.ACCESS_TOKEN).asString();

		// Step 2: Call protected end point with token
		mockMvc.perform(get(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_JWT_TOKEN_TEST_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk())
		.andExpect(content().string(
				org.hamcrest.Matchers.containsString(AuthConstants.MSG_AUTHENTICATED_USER_ID)
				));
	}
	
	// =======================
		// Integration tests
		// =======================

	@Test
	void shouldCreateSessionOnLogin() throws Exception {

		TestUser testUser = registerTestUser(); 

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		String response = mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Extract token
		String token = objectMapper.readTree(response).get(AuthConstants.ACCESS_TOKEN).asString();

		// Extract sessionId from JWT
		Claims claims = jwtUtil.extractAllClaims(token);
		String sessionId = claims.get(AuthConstants.SID_LOWER_CASE, String.class);

		assertNotNull(sessionId);

		// Verify DB
		Optional<UserSessions> sessionOpt =
				userSessionsRepository.findBySessionId(UUID.fromString(sessionId));

		assertTrue(sessionOpt.isPresent());

		UserSessions session = sessionOpt.get();

		assertNotNull(session.getStartedAt());
		assertNotNull(session.getLastActivityAt());
		assertNull(session.getEndedAt());
	}


	@Test
	void shouldUpdateSessionActivityOnApiCall() throws Exception {

		TestUser testUser = registerTestUser(); 

		String requestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		String response = mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = objectMapper.readTree(response).get(AuthConstants.ACCESS_TOKEN).asString();

		Claims claims = jwtUtil.extractAllClaims(token);
		UUID sessionId = UUID.fromString(claims.get(AuthConstants.SID_LOWER_CASE, String.class));

		UserSessions before = userSessionsRepository.findBySessionId(sessionId).get();
		LocalDateTime beforeTime = before.getLastActivityAt();

		// Step 2: Call protected API
		mockMvc.perform(get(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_JWT_TOKEN_TEST_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk());

		// Step 3: Fetch again
		UserSessions after = userSessionsRepository.findBySessionId(sessionId).get();
		LocalDateTime afterTime = after.getLastActivityAt();

		assertTrue(afterTime.isAfter(beforeTime));
	}

	@Test
	void shouldExtractClaimsFromToken() {

		UUID sessionId = UUID.randomUUID();
		User user = new User();
		user.setId(1L);
		user.setRole(Role.CUSTOMER);
		String token = jwtUtil.generateToken(user, sessionId);

		Claims claims = jwtUtil.extractAllClaims(token);

		assertEquals(user.getId().toString(), claims.getSubject());
		assertEquals(Role.CUSTOMER.name(), claims.get(AuthConstants.ROLE_LOWER_CASE));
		assertEquals(sessionId.toString(), claims.get(AuthConstants.SID_LOWER_CASE));
	}
	
	// =======================
	// Logout tests
	// =======================
	
	@Test
	void shouldLogoutSuccessfully() throws Exception {
		
		TestUser testUser = registerTestUser();
		
		//Login now
		String loginRequestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		String response = mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestBody))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = objectMapper.readTree(response).get(AuthConstants.ACCESS_TOKEN).asString();
		Claims claims = jwtUtil.extractAllClaims(token);
		UUID sessionId = UUID.fromString(claims.get(AuthConstants.SID_LOWER_CASE, String.class));
		long userId = Long.parseLong(claims.getSubject());
		
		//perform logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk());
		
		// Verify DB updated
	    UserSessions updated = userSessionsRepository
	            .findBySessionIdAndUserIdAndEndedAtIsNull(sessionId, userId)
	            .orElse(null);

	    assert(updated == null); // session should now be ended
		
	}
	
	@Test
	void shouldFailForNoJWT() throws Exception {
		
		//perform logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT))
		.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	void shouldFailForInvalidJWT() throws Exception {
		
		//perform logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.INVALID_TOKEN))
		.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	void shouldHandleMultipleLogoutCalls() throws Exception {

		TestUser testUser = registerTestUser();

		//Login now
		String loginRequestBody = """
				{
				  "identifier": "%s",
				  "password": "%s"
				}
				""".formatted(testUser.email, testUser.password);

		String response = mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestBody))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = objectMapper.readTree(response).get(AuthConstants.ACCESS_TOKEN).asString();
		Claims claims = jwtUtil.extractAllClaims(token);
		UUID sessionId = UUID.fromString(claims.get(AuthConstants.SID_LOWER_CASE, String.class));
		long userId = Long.parseLong(claims.getSubject());

		//perform first logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk());

		//perform second logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk());

		//perform third logout and receive 200
		mockMvc.perform(post(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
				.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + AuthConstants.BLANK_SPACE_SEPARATOR + token))
		.andExpect(status().isOk());

		// Verify DB updated
		UserSessions updated = userSessionsRepository
				.findBySessionIdAndUserIdAndEndedAtIsNull(sessionId, userId)
				.orElse(null);

		assert(updated == null); // session should now be ended

	}

}
