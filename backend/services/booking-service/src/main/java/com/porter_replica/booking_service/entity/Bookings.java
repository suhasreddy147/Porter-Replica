package com.porter_replica.booking_service.entity;

import java.time.LocalDateTime;

import com.porter_replica.booking_service.enums.BookingStatusEnum;
import com.porter_replica.booking_service.enums.VehicleTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Bookings extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="booking_id")
	private Long bookingId;

	@Column(name="customer_id")
	private Long customerId;

	@Column(name="pickup_address")
	private String pickupAddress;

	@Column(name="pickup_latitude")
	private Double pickupLatitude;

	@Column(name="pickup_longitude")
	private Double pickupLongitude;

	@Column(name="drop_address")
	private String dropAddress;

	@Column(name="drop_latitude")
	private Double dropLatitude;

	@Column(name="drop_longitude")
	private Double dropLongitude;

	@Column(name="coupon_cd")
	private String couponCd;

	@Column(name="scheduled_at")
	private LocalDateTime scheduledAt;

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private BookingStatusEnum status;

	@Column(name="estimated_distance_km")
	private Double estimatedDistanceKm;

	@Column(name="estimated_duration_minutes")
	private Long estimatedDurationMinutes;

	@Column(name="vehicle_type")
	@Enumerated(EnumType.STRING)
	private VehicleTypeEnum vehicleType;

	@Column(name="city")
	private String city;

	@Column(name="state")
	private String state;

	@Column(name="country")
	private String country;

}
