package com.porter_replica.booking_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.porter_replica.booking_service.constants.BookingConstants;
import com.porter_replica.booking_service.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Invalid JSON errors
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidJson(
			HttpMessageNotReadableException ex) {

		String message = BookingConstants.MSG_INVALID_REQ_BODY;

		return ResponseEntity.badRequest()
				.body(new ErrorResponseDTO(400, message));
	}

	// DTO validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
			MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();

		return ResponseEntity.badRequest()
				.body(new ErrorResponseDTO(400, message));
	}

	// Business validation errors
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
			IllegalArgumentException ex) {

		return ResponseEntity.badRequest()
				.body(new ErrorResponseDTO(400, ex.getMessage()));
	}

	// Fallback (unexpected errors)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponseDTO(
						500,
						BookingConstants.MSG_500
						));
	}
}
