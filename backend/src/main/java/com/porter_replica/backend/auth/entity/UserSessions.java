package com.porter_replica.backend.auth.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="user_sessions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserSessions extends BaseEntity {

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

	// =====================
	// Constructors
	// =====================

	public UserSessions(User user, UUID sessionId) {
		this.user = user;
		this.sessionId = sessionId;
		this.startedAt = LocalDateTime.now();
		this.lastActivityAt = LocalDateTime.now();
	}

}
