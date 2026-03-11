package com.porter_replica.auth_service.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// Invalid JSON errors
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidJson(
	        HttpMessageNotReadableException ex) {

	    String message = AuthConstants.MSG_INVALID_REQ_BODY;

	    // Optional: more specific message for enum errors
	    if (ex.getMessage() != null && ex.getMessage().contains(AuthConstants.ROLE_CAPITALIZED_CASE)) {
	        message = AuthConstants.MSG_INVALID_ROLE;
	    }

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
						AuthConstants.MSG_500
						));
	}
}
