package com.porter_replica.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.porter_replica.backend.auth.constants.AuthConstants;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = AuthConstants.AUTH_AUDITOR_AWARE)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
