package com.devsuperior.dsclient.controller.exception;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.devsuperior.dsclient.services.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ControllerExeptionsHandler extends ResponseEntityExceptionHandler {
	MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ex.getRootCause();
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}
		StandardError body = null;
		return handleExceptionInternal(ex, body, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String url = ex.getRequestURL();
		String detail = String.format("The resource '%s' you tried to access is non-existent.", url);
		StandardError body = createStandarError(status.value(), "resource not found", detail, url).build();
		
		return handleExceptionInternal(ex, body, headers, status, request);
	}
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if(ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers,
				 status, request);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String detail = String.format("One or more fields are invalid. Please fill in correctly and try again.");
		String path = getPath(request);
		BindingResult bindingResult = ex.getBindingResult();
		List<StandardError.Field> fields = bindingResult.getFieldErrors().stream()
				.map(fildError -> {
					String message = messageSource.getMessage(fildError, LocaleContextHolder.getLocale());
					return StandardError.Field.builder()
							.name(fildError.getField())
							.userMessage(message).build();
				}).collect(Collectors.toList());
		
		StandardError body = createStandarError(status.value(), "Invalid fields", detail, path).fields(fields).build();
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String param = ex.getParameter().getParameterName();
		String value = ex.getValue().toString();
		String type = ex.getRequiredType().getSimpleName();
		String path = getPath(request);
		String detail = String.format("The URL parameter '%s' was given the value '%s' which is of an invalid type. Correct and enter a value compatible with type '%s'.", param,value,type);
		StandardError body = createStandarError(status.value(), "Resource not found", detail, path).build();
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String prop = ex.getPath().stream().map(x -> x.getFieldName()).collect(Collectors.joining("."));
		String path = getPath(request);
		String details = String.format("Property '%s' does not exist. Correct or remove this property and try again.",
				prop);
		StandardError body = createStandarError(status.value(), "Non-existent property", details,
				path).build();
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String prop = ex.getPath().stream().map(x -> x.getFieldName()).collect(Collectors.joining("."));
		String detail = String.format(
				"the '%s' property received a value that is of an invalid type. Correct and enter a value compatible with type '%s'",
				prop, ex.getTargetType().getSimpleName());
		String path = getPath(request);

		StandardError body = createStandarError(status.value(), "Invalid format", detail, path).build();
		return handleExceptionInternal(ex, body, headers, status, request);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest httpRequest) {
		StandardError err = createStandarError(HttpStatus.NOT_FOUND.value(), "Resource not found", ex.getMessage(),
				httpRequest.getRequestURI()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = StandardError.builder().error(status.getReasonPhrase()).status(status.value()).build();
		} else if (body instanceof String) {
			body = StandardError.builder().timestamp(Instant.now()).error((String) body).status(status.value()).build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private StandardError.StandardErrorBuilder createStandarError(Integer status, String error, String message,
			String path) {
		return StandardError.builder().status(status).error(error).message(message).path(path);
	}
	
	private String getPath(WebRequest request) {
		return request.getDescription(false).substring(4);
	}
}
