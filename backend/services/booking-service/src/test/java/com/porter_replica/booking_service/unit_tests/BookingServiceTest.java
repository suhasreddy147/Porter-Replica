package com.porter_replica.booking_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.porter_replica.booking_service.constants.BookingConstants;
import com.porter_replica.booking_service.dto.CreateBookingRequestDTO;
import com.porter_replica.booking_service.entity.Bookings;
import com.porter_replica.booking_service.enums.BookingStatusEnum;
import com.porter_replica.booking_service.enums.VehicleTypeEnum;
import com.porter_replica.booking_service.respository.BookingsRepository;
import com.porter_replica.booking_service.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	private BookingsRepository bookingsRepository;
	
	@InjectMocks
	private BookingService bookingService;
	
	private CreateBookingRequestDTO createValidRequest() {
	    return CreateBookingRequestDTO.builder()
				.city(BookingConstants.TEST_CITY)
				.state(BookingConstants.TEST_STATE)
				.country(BookingConstants.TEST_COUNTRY)
				.customerId(1L)
				.dropAddress(BookingConstants.TEST_DROP_ADDRESS)
				.pickupAddress(BookingConstants.TEST_PICKUP_ADDRESS)
				.dropLatitude(1.1)
				.dropLongitude(2.1)
				.pickupLatitude(3.1)
				.pickupLongitude(4.1)
				.estimatedDistanceKms(1.1)
				.estimatedDurationMinutes(10L)
				.scheduledDateTime(LocalDateTime.now())
				.couponCd(BookingConstants.TEST_COUPON_CD)
				.vehicleType(VehicleTypeEnum.AUTO)
				.build();
	}

	@Test
	void shouldCreateBookingSuccessfullyWithStatusAsCreated() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		
		when(bookingsRepository.save(any(Bookings.class))).thenAnswer(invocation -> {
			Bookings bookings = invocation.getArgument(0);
			bookings.setBookingId(1L);
			return bookings;
		});

		bookingService.createBooking(createBookingRequest);

		ArgumentCaptor<Bookings> argumentCaptor = ArgumentCaptor.forClass(Bookings.class);
		verify(bookingsRepository, times(1)).save(argumentCaptor.capture());
		Bookings createdBooking = argumentCaptor.getValue();
		
		assertEquals(1L, createdBooking.getCreatedBy());
		assertNull(createdBooking.getUpdatedBy());
		assertNull(createdBooking.getUpdatedAt());
		
		assertEquals(createBookingRequest.getCustomerId(), createdBooking.getCustomerId());
		assertEquals(createBookingRequest.getPickupAddress(), createdBooking.getPickupAddress());
		assertEquals(createBookingRequest.getDropAddress(), createdBooking.getDropAddress());
		assertEquals(createBookingRequest.getDropLatitude(), createdBooking.getDropLatitude());
		assertEquals(createBookingRequest.getDropLongitude(), createdBooking.getDropLongitude());
		assertEquals(createBookingRequest.getPickupLatitude(), createdBooking.getPickupLatitude());
		assertEquals(createBookingRequest.getPickupLongitude(), createdBooking.getPickupLongitude());
		assertEquals(createBookingRequest.getCity(), createdBooking.getCity());
		assertEquals(createBookingRequest.getState(), createdBooking.getState());
		assertEquals(createBookingRequest.getCountry(), createdBooking.getCountry());
		assertEquals(createBookingRequest.getEstimatedDistanceKms(), createdBooking.getEstimatedDistanceKm());
		assertEquals(createBookingRequest.getEstimatedDurationMinutes(), createdBooking.getEstimatedDurationMinutes());
		assertEquals(createBookingRequest.getScheduledDateTime(), createdBooking.getScheduledAt());
		assertEquals(createBookingRequest.getCouponCd(), createdBooking.getCouponCd());
		assertEquals(BookingStatusEnum.CREATED,createdBooking.getStatus());

	}
	
	@Test
	void shouldCreateBookingSuccessfullyWithStatusAsCreatedWhenCouponCdIsNotPresent() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setCouponCd(null);
		
		when(bookingsRepository.save(any(Bookings.class))).thenAnswer(invocation -> {
			Bookings bookings = invocation.getArgument(0);
			bookings.setBookingId(1L);
			return bookings;
		});

		bookingService.createBooking(createBookingRequest);

		ArgumentCaptor<Bookings> argumentCaptor = ArgumentCaptor.forClass(Bookings.class);
		verify(bookingsRepository, times(1)).save(argumentCaptor.capture());
		Bookings createdBooking = argumentCaptor.getValue();
		
		assertEquals(1L, createdBooking.getCreatedBy());
		assertNull(createdBooking.getUpdatedBy());
		assertNull(createdBooking.getUpdatedAt());
		
		assertEquals(createBookingRequest.getCustomerId(), createdBooking.getCustomerId());
		assertEquals(createBookingRequest.getPickupAddress(), createdBooking.getPickupAddress());
		assertEquals(createBookingRequest.getDropAddress(), createdBooking.getDropAddress());
		assertEquals(createBookingRequest.getDropLatitude(), createdBooking.getDropLatitude());
		assertEquals(createBookingRequest.getDropLongitude(), createdBooking.getDropLongitude());
		assertEquals(createBookingRequest.getPickupLatitude(), createdBooking.getPickupLatitude());
		assertEquals(createBookingRequest.getPickupLongitude(), createdBooking.getPickupLongitude());
		assertEquals(createBookingRequest.getCity(), createdBooking.getCity());
		assertEquals(createBookingRequest.getState(), createdBooking.getState());
		assertEquals(createBookingRequest.getCountry(), createdBooking.getCountry());
		assertEquals(createBookingRequest.getEstimatedDistanceKms(), createdBooking.getEstimatedDistanceKm());
		assertEquals(createBookingRequest.getEstimatedDurationMinutes(), createdBooking.getEstimatedDurationMinutes());
		assertEquals(createBookingRequest.getScheduledDateTime(), createdBooking.getScheduledAt());
		assertNull(createBookingRequest.getCouponCd());
		assertEquals(BookingStatusEnum.CREATED,createdBooking.getStatus());

	}
	
	@Test
	void shouldFailWhenCustomerIdIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setCustomerId(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_CUSTOMER_ID_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenDropAddressIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setDropAddress(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_DROP_ADDRESS_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenPickupAddressIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setPickupAddress(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_PICKUP_ADDRESS_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenDropLatitudeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setDropLatitude(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_DROP_LAT_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenDropLongitudeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setDropLongitude(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_DROP_LNG_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenPickupLatitudeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setPickupLatitude(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_PICKUP_LAT_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenPickupLongitudeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setPickupLongitude(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals( BookingConstants.MSG_PICKUP_LNG_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenEstimatedDurationIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setEstimatedDurationMinutes(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals( BookingConstants.MSG_ESTIMATED_DURATION_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenEstimatedDistanceIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setEstimatedDistanceKms(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_ESTIMATED_DISTANCE_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenScheduledDateTimeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setScheduledDateTime(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_SCHEDULED_DATE_TIME_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenCountryIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setCountry(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_COUNTRY_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenStateIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setState(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_STATE_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenCityIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setCity(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_CITY_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldFailWhenVehicleTypeIsMissingAndThrowAppropriateMessage() {
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		createBookingRequest.setVehicleType(null);
		
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.MSG_VEHICLE_TYPE_REQUIRED, illegalArgumentException.getMessage());

	}
	
	@Test
	void shouldThrowExceptionWhenDatabaseFails() {
		
		CreateBookingRequestDTO createBookingRequest = createValidRequest();
		
		when(bookingsRepository.save(any())).thenThrow(new RuntimeException(BookingConstants.TEST_DB_ERROR));
		
		RuntimeException runtimeException = assertThrows(RuntimeException.class, 
				() -> bookingService.createBooking(createBookingRequest));
		
		assertEquals(BookingConstants.TEST_DB_ERROR, runtimeException.getMessage());
		
	}

}
