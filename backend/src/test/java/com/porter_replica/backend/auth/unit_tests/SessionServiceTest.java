package com.porter_replica.backend.auth.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.porter_replica.backend.auth.entity.User;
import com.porter_replica.backend.auth.entity.UserSessions;
import com.porter_replica.backend.auth.enums.Role;
import com.porter_replica.backend.auth.repository.UserSessionsRepository;
import com.porter_replica.backend.auth.service.SessionService;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

	@Mock
	private UserSessionsRepository userSessionsRepository;

	@InjectMocks
	private SessionService sessionService;

	private User user;

	@BeforeEach
	void setup() {
		user = new User();
		user.setId(1L);
		user.setRole(Role.CUSTOMER);
	}
	
	@Test
	void shouldCreateSession() {

	    UUID sessionId = UUID.randomUUID();

	    when(userSessionsRepository.save(any(UserSessions.class)))
	            .thenAnswer(invocation -> invocation.getArgument(0));

	    UserSessions session = sessionService.createSession(user, sessionId);

	    assertNotNull(session);
	    assertEquals(user, session.getUser());
	    assertEquals(sessionId, session.getSessionId());
	    assertNotNull(session.getStartedAt());
	    assertNotNull(session.getLastActivityAt());

	    verify(userSessionsRepository).save(any(UserSessions.class));
	}
	
	@Test
	void shouldUpdateSessionActivity() {

	    UUID sessionId = UUID.randomUUID();

	    UserSessions session = new UserSessions(user, sessionId);
	    LocalDateTime oldTime = session.getLastActivityAt();

	    when(userSessionsRepository.findBySessionId(sessionId))
	            .thenReturn(Optional.of(session));

	    sessionService.updateActivity(sessionId);
	    
	    assertFalse(session.getLastActivityAt().isBefore(oldTime));

	    verify(userSessionsRepository).save(session);
	}
	
	@Test
	void shouldDoNothingIfSessionNotFoundOnUpdate() {

	    UUID sessionId = UUID.randomUUID();

	    when(userSessionsRepository.findBySessionId(sessionId))
	            .thenReturn(Optional.empty());

	    sessionService.updateActivity(sessionId);

	    verify(userSessionsRepository, never()).save(any());
	}
	
	@Test
	void shouldEndSession() {

	    UUID sessionId = UUID.randomUUID();

	    UserSessions session = new UserSessions(user, sessionId);

	    when(userSessionsRepository.findBySessionIdAndUserIdAndEndedAtIsNull(sessionId, user.getId()))
	            .thenReturn(Optional.of(session));

	    sessionService.endSession(sessionId, user.getId());

	    assertNotNull(session.getEndedAt());

	    verify(userSessionsRepository, times(1)).save(session);
	}
	
	@Test
	void shouldDoNothingIfSessionNotFoundOrSessionAlreadyEnded() {

	    UUID sessionId = UUID.randomUUID();

	    when(userSessionsRepository.findBySessionIdAndUserIdAndEndedAtIsNull(sessionId, user.getId()))
        .thenReturn(Optional.empty());

	    sessionService.endSession(sessionId, user.getId());

	    verify(userSessionsRepository, never()).save(any());
	}

}
