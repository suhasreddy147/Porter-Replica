package com.porter_replica.booking_service.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.porter_replica.booking_service.enums.VehicleTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingRequestDTO {

	private Long customerId;

	private String pickupAddress;
	private Double pickupLatitude;
	private Double pickupLongitude;

	private String dropAddress;
	private Double dropLatitude;
	private Double dropLongitude;

	private String city;
	private String state;
	private String country;

	private String couponCd;

	private VehicleTypeEnum vehicleType;

	private Double estimatedDistanceKms;

	private Long estimatedDurationMinutes;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime scheduledDateTime;

}
