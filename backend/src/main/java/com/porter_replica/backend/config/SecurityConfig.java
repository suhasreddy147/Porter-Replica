package com.porter_replica.backend.config;

import com.porter_replica.backend.auth.jwt.JwtAuthenticationFilter;
import com.porter_replica.backend.user.Role;

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
            .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
            .requestMatchers("/api/auth/logout").authenticated()
            .requestMatchers("/api/auth/test/customer").hasRole(Role.CUSTOMER.name())
            .requestMatchers("/api/auth/test/driver").hasRole(Role.DRIVER.name())
            .requestMatchers("/api/auth/test/admin").hasRole(Role.ADMIN.name())
            .anyRequest().authenticated()
        )
        .exceptionHandling(exception -> exception
        	    .authenticationEntryPoint(
        	            (request, response, authException) ->
        	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
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
