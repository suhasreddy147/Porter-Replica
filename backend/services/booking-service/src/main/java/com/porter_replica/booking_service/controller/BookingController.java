package com.porter_replica.booking_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porter_replica.booking_service.constants.BookingConstants;
import com.porter_replica.booking_service.dto.CreateBookingRequestDTO;
import com.porter_replica.booking_service.entity.Bookings;
import com.porter_replica.booking_service.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(BookingConstants.API_BOOKINGS_PARENT_ENDPOINT)
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping(BookingConstants.API_BOOKINGS_CREATE_BOOKING_ENDPOINT)
	public Bookings createBooking(@RequestBody CreateBookingRequestDTO request) {
		return bookingService.createBooking(request);
	}

	@GetMapping(BookingConstants.API_BOOKINGS_QUERY_PARAM_ID)
	public Bookings getBooking(@PathVariable Long id) {
		return bookingService.getBookingByBookingId(id);
	}

	@GetMapping(BookingConstants.API_BOOKINGS_CUSTOMER_ENDPOINT+BookingConstants.API_BOOKINGS_QUERY_PARAM_CUSTOMER_ID)
	public List<Bookings> getCustomerBookings(@PathVariable Long customerId) {
		return bookingService.getBookingsByCustomer(customerId);
	}
}
