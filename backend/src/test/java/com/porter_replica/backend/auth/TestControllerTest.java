package com.porter_replica.backend.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.porter_replica.backend.auth.jwt.JwtUtil;
import com.porter_replica.backend.user.Role;
import com.porter_replica.backend.user.User;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;
    
    private String generateToken(Long userId, Role role) {

        User user = new User();
        user.setId(userId);
        user.setRole(role);

        return jwtUtil.generateToken(user, UUID.randomUUID());
    }
    
    @Test
    void customerShouldAccessCustomerApi() throws Exception {

        String token = generateToken(1L, Role.CUSTOMER);

        mockMvc.perform(get("/api/auth/test/customer")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void customerShouldNotAccessDriverApi() throws Exception {

        String token = generateToken(1L, Role.CUSTOMER);

        mockMvc.perform(get("/api/auth/test/driver")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void customerShouldNotAccessAdminApi() throws Exception {

        String token = generateToken(1L, Role.CUSTOMER);

        mockMvc.perform(get("/api/auth/test/admin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void driverShouldNotAccessAdminApi() throws Exception {

        String token = generateToken(2L, Role.DRIVER);

        mockMvc.perform(get("/api/auth/test/admin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void driverShouldNotAccessCustomerApi() throws Exception {

        String token = generateToken(2L, Role.DRIVER);

        mockMvc.perform(get("/api/auth/test/customer")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void driverShouldAccessDriverApi() throws Exception {

        String token = generateToken(1L, Role.DRIVER);

        mockMvc.perform(get("/api/auth/test/driver")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void adminShouldNotAccessDriverApi() throws Exception {

        String token = generateToken(2L, Role.ADMIN);

        mockMvc.perform(get("/api/auth/test/driver")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void adminShouldNotAccessCustomerApi() throws Exception {

        String token = generateToken(2L, Role.ADMIN);

        mockMvc.perform(get("/api/auth/test/customer")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void driverShouldAccessAdminApi() throws Exception {

        String token = generateToken(1L, Role.ADMIN);

        mockMvc.perform(get("/api/auth/test/admin")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void customerApiShouldReturn401WhenNoToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/customer"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void  customerApiShould401WhenInvalidToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/customer")
                .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void driverApiShouldReturn401WhenNoToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/driver"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void  driverApiShould401WhenInvalidToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/driver")
                .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void adminApiShouldReturn401WhenNoToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/admin"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void  adminApiShould401WhenInvalidToken() throws Exception {

        mockMvc.perform(get("/api/auth/test/admin")
                .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }
}
