package com.porter_replica.backend.auth;

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

}
