package com.porter_replica.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// Invalid JSON errors
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidJson(
	        HttpMessageNotReadableException ex) {

	    String message = "Invalid request body";

	    // Optional: more specific message for enum errors
	    if (ex.getMessage() != null && ex.getMessage().contains("Role")) {
	        message = "Invalid role value";
	    }

	    return ResponseEntity.badRequest()
	            .body(new ErrorResponse(400, message));
	}
	
	// DTO validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(
			MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();

		return ResponseEntity.badRequest()
				.body(new ErrorResponse(400, message));
	}

	// Business validation errors
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(
			IllegalArgumentException ex) {

		return ResponseEntity.badRequest()
				.body(new ErrorResponse(400, ex.getMessage()));
	}

	// Fallback (unexpected errors)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(
						500,
						"Something went wrong. Please try again."
						));
	}
}
