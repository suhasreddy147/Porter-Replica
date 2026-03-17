package com.porter_replica.booking_service.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@Column(name = "created_by", updatable = false, nullable = false)
	@CreatedBy
	private Long createdBy;

	@Column(name = "updated_by",  insertable = false)
	@LastModifiedBy
	private Long updatedBy;

	@Column(name = "created_at", updatable = false, nullable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
