package com.porter_replica.backend.auth.session;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.porter_replica.backend.user.User;

@Service
public class SessionService {
	private final UserSessionsRepository repository;

    public SessionService(UserSessionsRepository repository) {
        this.repository = repository;
    }

    public UserSessions createSession(User user, UUID sessionId) {
        UserSessions session = new UserSessions(user, sessionId);
        return repository.save(session);
    }

    public void updateActivity(UUID sessionId) {
        repository.findBySessionId(sessionId)
                .ifPresent(session -> {
                    session.setLastActivityAt(LocalDateTime.now());
                    repository.save(session);
                });
    }

    public void endSession(UUID sessionId) {
        repository.findBySessionId(sessionId)
                .ifPresent(session -> {
                    session.setEndedAt(LocalDateTime.now());
                    repository.save(session);
                });
    }
}
