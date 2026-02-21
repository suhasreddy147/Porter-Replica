package com.porter_replica.backend.auth.session;

import java.time.LocalDateTime;
import java.util.UUID;

import com.porter_replica.backend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_sessions")
public class UserSessions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@Column(name="session_id", nullable=false, unique=true)
	private UUID sessionId;

	@Column(name = "started_at", nullable = false)
	private LocalDateTime startedAt;

	@Column(name = "last_activity_at", nullable = false)
	private LocalDateTime lastActivityAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@Column(name = "client_type")
	private String clientType;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	// =====================
	// Constructors
	// =====================

	public UserSessions() {}

	public UserSessions(User user, UUID sessionId) {
		this.user = user;
		this.sessionId = sessionId;
		this.startedAt = LocalDateTime.now();
		this.lastActivityAt = LocalDateTime.now();
		this.createdAt = LocalDateTime.now();
	}
	
	//===========================
	// Getters and Setters
	//===========================
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UUID getSessionId() {
		return sessionId;
	}

	public void setSessionId(UUID sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getLastActivityAt() {
		return lastActivityAt;
	}

	public void setLastActivityAt(LocalDateTime lastActivityAt) {
		this.lastActivityAt = lastActivityAt;
	}

	public LocalDateTime getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(LocalDateTime endedAt) {
		this.endedAt = endedAt;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
