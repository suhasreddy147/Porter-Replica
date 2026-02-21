package com.porter_replica.backend.auth.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionsRepository extends JpaRepository<UserSessions, Long>{
	
	Optional<UserSessions> findBySessionId(UUID sessionId);
	Optional<UserSessions> findBySessionIdAndEndedAtIsNull(UUID sessionId);
	
}
