package com.porter_replica.backend.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    // =======================
    // Registration tests
    // =======================
    
    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        String requestBody = """
            {
              "name": "JUnit User",
              "email": "junit1@test.com",
              "password": "password123",
              "role": "CUSTOMER"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string("User registered successfully"));
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

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Name is required"));
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

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Name is required"));
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

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Password is required"));
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

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Email or phone is required"));
    }

    @Test
    void shouldFailForDuplicateEmail() throws Exception {

        String requestBody = """
            {
              "name": "Duplicate",
              "email": "duplicate@test.com",
              "password": "password123",
              "role": "CUSTOMER"
            }
            """;

        // First call (success)
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        // Second call (duplicate)
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Email is already registered"));
    }
    
    @Test
    void shouldFailForInvalidRole() throws Exception {

        String requestBody = """
            {
              "name": "Invalid Role",
              "email": "invalidrole@test.com",
              "password": "password123",
              "role": "ADMIN"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
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

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }
    
 // =======================
 // Login tests
 // =======================
    
    @Test
    void shouldLoginSuccessfullyAndReturnJwt() throws Exception {

    	String requestBodyOne  =  """
                {
                "name": "Login User",
                "email": "login@test.com",
                "password": "password123",
                "role": "CUSTOMER"
              }
              """;
    	
        String requestBodyTwo = """
            {
              "email": "login@test.com",
              "password": "password123"
            }
            """;
        
        //First register the user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyOne))
            .andExpect(status().isOk())
            .andExpect(content().string("User registered successfully"));

        //Now login with the newly registered user
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyTwo))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
    
    @Test
    void shouldFailLoginWithInvalidPassword() throws Exception {

        String requestBody = """
            {
              "email": "junit1@test.com",
              "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
    
    @Test
    void shouldFailLoginForNonExistentUser() throws Exception {

        String requestBody = """
            {
              "email": "nouser@test.com",
              "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void shouldFailLoginWhenPasswordMissing() throws Exception {

        String requestBody = """
            {
              "email": "loginuser@test.com"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }
    
 // =======================
 // JWT security tests
 // =======================
    
    @Test
    void shouldRejectAccessToProtectedEndpointWithoutToken() throws Exception {

        mockMvc.perform(get("/api/auth/me"))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    void shouldAllowAccessToProtectedEndpointWithValidToken() throws Exception {

        // Step 1: Login to get token
        String loginRequest = """
            {
              "email": "junit1@test.com",
              "password": "password123"
            }
            """;

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String token = objectMapper.readTree(response)
                .get("accessToken")
                .asText();

        // Step 2: Call protected endpoint with token
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString("Authenticated user ID")
            ));
    }

}
