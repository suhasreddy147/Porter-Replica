package com.porter_replica.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.porter_replica.booking_service.constants.BookingConstants;
import com.porter_replica.booking_service.dto.CreateBookingRequestDTO;
import com.porter_replica.booking_service.entity.Bookings;
import com.porter_replica.booking_service.enums.BookingStatusEnum;
import com.porter_replica.booking_service.respository.BookingsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class BookingService {

	private final BookingsRepository bookingsRepository;

	@Transactional
	public Bookings createBooking(CreateBookingRequestDTO createBookingRequestDTO) {

		if(!StringUtils.hasText(createBookingRequestDTO.getPickupAddress())) {
			throw new IllegalArgumentException(BookingConstants.MSG_PICKUP_ADDRESS_REQUIRED);
		}

		if(!StringUtils.hasText(createBookingRequestDTO.getDropAddress())) {
			throw new IllegalArgumentException(BookingConstants.MSG_DROP_ADDRESS_REQUIRED);
		}

		if(null==createBookingRequestDTO.getCustomerId() || createBookingRequestDTO.getCustomerId()==0) {
			throw new IllegalArgumentException(BookingConstants.MSG_CUSTOMER_ID_REQUIRED);
		}

		if(null==createBookingRequestDTO.getVehicleType()) {
			throw new IllegalArgumentException(BookingConstants.MSG_VEHICLE_TYPE_REQUIRED);
		}

		if(null==createBookingRequestDTO.getPickupLatitude()) {
			throw new IllegalArgumentException(BookingConstants.MSG_PICKUP_LAT_REQUIRED);
		}

		if(null==createBookingRequestDTO.getPickupLongitude()) {
			throw new IllegalArgumentException(BookingConstants.MSG_PICKUP_LNG_REQUIRED);
		}

		if(null==createBookingRequestDTO.getDropLatitude()) {
			throw new IllegalArgumentException(BookingConstants.MSG_DROP_LAT_REQUIRED);
		}

		if(null==createBookingRequestDTO.getDropLongitude()) {
			throw new IllegalArgumentException(BookingConstants.MSG_DROP_LNG_REQUIRED);
		}

		if(!StringUtils.hasText(createBookingRequestDTO.getCity())) {
			throw new IllegalArgumentException(BookingConstants.MSG_CITY_REQUIRED);
		}

		if(!StringUtils.hasText(createBookingRequestDTO.getCountry())) {
			throw new IllegalArgumentException(BookingConstants.MSG_COUNTRY_REQUIRED);
		}

		if(!StringUtils.hasText(createBookingRequestDTO.getState())) {
			throw new IllegalArgumentException(BookingConstants.MSG_STATE_REQUIRED);
		}

		if(null==createBookingRequestDTO.getScheduledDateTime()) {
			throw new IllegalArgumentException(BookingConstants.MSG_SCHEDULED_DATE_TIME_REQUIRED);
		}

		if(null==createBookingRequestDTO.getEstimatedDistanceKms()) {
			throw new IllegalArgumentException(BookingConstants.MSG_ESTIMATED_DISTANCE_REQUIRED);
		}

		if(null==createBookingRequestDTO.getEstimatedDurationMinutes()) {
			throw new IllegalArgumentException(BookingConstants.MSG_ESTIMATED_DURATION_REQUIRED);
		}

		Bookings bookings = Bookings.builder()
				.customerId(createBookingRequestDTO.getCustomerId())
				.pickupAddress(createBookingRequestDTO.getPickupAddress())
				.dropAddress(createBookingRequestDTO.getDropAddress())
				.pickupLatitude(createBookingRequestDTO.getPickupLatitude())
				.pickupLongitude(createBookingRequestDTO.getPickupLongitude())
				.dropLatitude(createBookingRequestDTO.getDropLatitude())
				.dropLongitude(createBookingRequestDTO.getDropLongitude())
				.city(createBookingRequestDTO.getCity())
				.state(createBookingRequestDTO.getState())
				.country(createBookingRequestDTO.getCountry())
				.couponCd(createBookingRequestDTO.getCouponCd())
				.scheduledAt(createBookingRequestDTO.getScheduledDateTime())
				.status(BookingStatusEnum.CREATED)
				.vehicleType(createBookingRequestDTO.getVehicleType())
				.estimatedDistanceKm(createBookingRequestDTO.getEstimatedDistanceKms())
				.estimatedDurationMinutes(createBookingRequestDTO.getEstimatedDurationMinutes())
				.build();

		bookings.setCreatedBy(createBookingRequestDTO.getCustomerId());
		bookings.setCreatedAt(LocalDateTime.now());

		return bookingsRepository.save(bookings);
	}

	public Bookings getBookingByBookingId(Long bookingId) {
		return bookingsRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
	}

	public List<Bookings> getBookingsByCustomer(Long customerId) {
		return bookingsRepository.findByCustomerId(customerId);
	}

}
