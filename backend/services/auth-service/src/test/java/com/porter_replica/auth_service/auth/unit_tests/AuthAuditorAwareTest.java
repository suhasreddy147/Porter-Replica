package com.porter_replica.auth_service.auth.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.porter_replica.auth_service.auth.audit.AuthAuditorAware;
import com.porter_replica.auth_service.auth.security.principal.CustomUserPrincipal;

@ExtendWith(MockitoExtension.class)
class AuthAuditorAwareTest {

	private final AuthAuditorAware authAuditorAware = new AuthAuditorAware();

	@Mock
	private Authentication authentication;

	@Mock
	private CustomUserPrincipal customUserPrincipal;

	@AfterEach
	void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void shouldReturnUserIdWhenAuthenticated() {

		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getPrincipal()).thenReturn(customUserPrincipal);
		when(customUserPrincipal.getUserId()).thenReturn(1L);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		Optional<Long> auditor = authAuditorAware.getCurrentAuditor();

		assertTrue(auditor.isPresent());
		assertEquals(1L, auditor.get());
	}
	
	@Test
    void shouldReturnEmptyWhenNoAuthentication() {

        SecurityContextHolder.clearContext();

        Optional<Long> auditor = authAuditorAware.getCurrentAuditor();

        assertTrue(auditor.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNotAuthenticated() {

        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<Long> auditor = authAuditorAware.getCurrentAuditor();

        assertTrue(auditor.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenPrincipalIsNotCustomUser() {

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<Long> auditor = authAuditorAware.getCurrentAuditor();

        assertTrue(auditor.isEmpty());
    }

}
