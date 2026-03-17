package com.porter_replica.booking_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="driver_assignments_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DriverAssignmentsHistory extends BaseHistoryEntity{

	@Id
	@Column(name="driver_assignment_id")
	private Long driverAssignmentId;

	@Column(name="booking_id")
	private Long bookingId;

	@Column(name="driver_id")
	private Long driverId;

	@Column(name="assigned_at")
	private LocalDateTime assignedAt;

	@Column(name="status")
	private String status;

	@Column(name="reason")
	private String reason;

}
