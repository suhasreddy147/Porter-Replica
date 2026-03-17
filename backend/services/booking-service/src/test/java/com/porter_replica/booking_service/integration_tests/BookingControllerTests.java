package com.porter_replica.booking_service.integration_tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.porter_replica.booking_service.constants.BookingConstants;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTests {

	@Autowired
	private MockMvc mockMvc;

	// =======================
	// Create Booking tests
	// =======================
	
//	@Test
//	void shouldCreateBookingSuccessfullyWithStatusAsCreated() throws Exception {
//		Long uniqueCustomerId = UUID.randomUUID();
//		String requestBody = """
//				{
//				  "customerId": %s,
//				  "pickupAddress": "%s",
//				  "pickupLatitude": "password123",
//				  "pickupLongitude": "CUSTOMER",
//				  "dropAddress": "JUnit Register",
//				  "dropLatitude": "%s",
//				  "dropLongitude": "password123",
//				  "city": "CUSTOMER",
//				  "state": "JUnit Register",
//				  "country": "%s",
//				  "vehicleType": "password123",
//				  "estimatedDistanceKms": "CUSTOMER",
//				  "estimatedDurationMinutes": "",
//				  "scheduledDateTime": ""
//				}
//				""".formatted(uniqueEmail);
//
//		mockMvc.perform(post(BookingConstants.API_BOOKINGS_PARENT_ENDPOINT + BookingConstants.API_BOOKINGS_CREATE_BOOKING_ENDPOINT)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(requestBody))
//		.andExpect(status().isOk())
//		.andExpect(content().string(BookingConstants.MSG_USER_REG_SUCCESSFULLY));
//	}

}
