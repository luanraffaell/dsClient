package com.devsuperior.dsclient.controller.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dsclient.services.exception.EntityNotFoundException;

@ControllerAdvice
public class ControllerExeptionsHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest httpRequest) {
		StandardError err = StandardError.builder()
			.status(HttpStatus.NOT_FOUND.value())
			.message(ex.getMessage())
			.timestamp(Instant.now())
			.path(httpRequest.getRequestURI())
			.error("Resource not found")
			.build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
