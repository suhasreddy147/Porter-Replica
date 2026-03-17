package com.porter_replica.booking_service.constants;

public class BookingConstants {
	public static final String MSG_500 = "Something went wrong. Please try again.";
	public static final String MSG_INVALID_ROLE = "Invalid role value";
	public static final String MSG_INVALID_CREDS = "Invalid credentials";
	public static final String MSG_INVALID_REQ_BODY = "Invalid request body";
	public static final String MSG_PICKUP_ADDRESS_REQUIRED = "Pickup Address is required";
	public static final String MSG_DROP_ADDRESS_REQUIRED = "Drop Address is required";
	public static final String MSG_CUSTOMER_ID_REQUIRED = "Customer ID is required";
	public static final String MSG_VEHICLE_TYPE_REQUIRED = "Vehicle Type is required";
	public static final String MSG_PICKUP_LAT_REQUIRED = "Pickup Latitude is required";
	public static final String MSG_PICKUP_LNG_REQUIRED = "Pickup Longitude is required";
	public static final String MSG_DROP_LAT_REQUIRED = "Drop Latitude is required";
	public static final String MSG_DROP_LNG_REQUIRED = "Drop Longitude is required";
	public static final String MSG_CITY_REQUIRED = "City is required";
	public static final String MSG_COUNTRY_REQUIRED = "Country is required";
	public static final String MSG_STATE_REQUIRED = "State is required";
	public static final String MSG_SCHEDULED_DATE_TIME_REQUIRED = "Scheduled Date and Time is required";
	public static final String MSG_ESTIMATED_DURATION_REQUIRED = "Estimated Duration in minutes is required";
	public static final String MSG_ESTIMATED_DISTANCE_REQUIRED = "Estimated Distance in kms is required";
	public static final String API_BOOKINGS_PARENT_ENDPOINT = "/api/bookings";
	public static final String API_BOOKINGS_QUERY_PARAM_ID = "/{id}";
	public static final String API_BOOKINGS_QUERY_PARAM_CUSTOMER_ID = "/{customerId}";
	public static final String API_BOOKINGS_CUSTOMER_ENDPOINT = "/customer";
	public static final String API_BOOKINGS_CREATE_BOOKING_ENDPOINT = "/createBooking";
	public static final String TEST_CITY = "TEST CITY";
	public static final String TEST_STATE = "TEST STATE";
	public static final String TEST_COUNTRY = "TEST COUNTRY";
	public static final String TEST_DROP_ADDRESS = "TEST DROP ADDRESS";
	public static final String TEST_PICKUP_ADDRESS = "TEST PICKUP ADDRESS";
	public static final String TEST_COUPON_CD = "TEST COUPON CODE";
	public static final String TEST_DB_ERROR = "DB Error";
}
