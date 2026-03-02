package com.porter_replica.backend.auth.security.config;

import com.porter_replica.backend.auth.constants.AuthConstants;
import com.porter_replica.backend.auth.enums.Role;
import com.porter_replica.backend.auth.security.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_REGISTER_ENDPOINT,
            		AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGIN_ENDPOINT)
            .permitAll()
            .requestMatchers(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_LOGOUT_ENDPOINT)
            .authenticated()
            .requestMatchers(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_CUSTOMER_ENDPOINT)
            .hasRole(Role.CUSTOMER.name())
            .requestMatchers(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_DRIVER_ENDPOINT)
            .hasRole(Role.DRIVER.name())
            .requestMatchers(AuthConstants.API_AUTH_PARENT_ENDPOINT + AuthConstants.API_TEST_ENDPOINT + AuthConstants.API_ADMIN_ENDPOINT)
            .hasRole(Role.ADMIN.name())
            .anyRequest().authenticated()
        )
        .exceptionHandling(exception -> exception
        	    .authenticationEntryPoint(
        	            (request, response, authException) ->
        	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, AuthConstants.UNAUTHORIZED)
        	        )
        	    )
        .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

		return http.build();
	}
	
	
}
