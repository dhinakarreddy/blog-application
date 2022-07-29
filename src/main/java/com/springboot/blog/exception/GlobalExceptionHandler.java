package com.springboot.blog.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.springboot.blog.payload.ErrorDetails;

// This class extends ResponseEntityExceptionHandler only to handle validation errors
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> resourceNotFound(ResourceNotFoundException exception, WebRequest webRequest) {
		ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BlogApiException.class)
	public ResponseEntity<ErrorDetails> badRequest(BlogApiException exception, WebRequest webRequest) {
		ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> badRequest(Exception exception, WebRequest webRequest) {
		ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// You will need to override this below method to handle validation errors
	/*
	 * 1st approach - you need to extend the class 'ResponseEntityExceptionHandler'
	 * and override 'handleMethodArgumentNotValid'
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldname = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldname, message);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// 2nd approach
	/* @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> invalidRequestPayload(MethodArgumentNotValidException exception,
			WebRequest webRequest) {
		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldname = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldname, message);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	} */

}
