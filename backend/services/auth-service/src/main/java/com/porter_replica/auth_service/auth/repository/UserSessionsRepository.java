package com.porter_replica.auth_service.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.porter_replica.auth_service.auth.entity.UserSessions;

public interface UserSessionsRepository extends JpaRepository<UserSessions, Long>{
	
	Optional<UserSessions> findBySessionId(UUID sessionId);
	Optional<UserSessions> findBySessionIdAndEndedAtIsNull(UUID sessionId);
	Optional<UserSessions> findBySessionIdAndUserIdAndEndedAtIsNull(UUID sessionId, long userId);
}
