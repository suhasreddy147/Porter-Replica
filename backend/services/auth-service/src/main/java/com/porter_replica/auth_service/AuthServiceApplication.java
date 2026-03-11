package com.porter_replica.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.porter_replica.auth_service.auth.constants.AuthConstants;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = AuthConstants.AUTH_AUDITOR_AWARE)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
