package com.loyalty.pointingsystem.services;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.loyalty.common.general.responses.models.ErrorCode;
import com.loyalty.common.general.responses.models.ErrorResponse;
import com.loyalty.common.general.responses.models.GeneralApplicationErrorCodes;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return handleExceptionInternal(ex, ex.getBindingResult().getAllErrors(), headers, status, request,
				GeneralApplicationErrorCodes.FIELD_VALIDATION_FAILED);
	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, body, headers, status, request,
				GeneralApplicationErrorCodes.NO_APPLICATION_CODE);

	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request, ErrorCode applicationError) {
		if (body == null) {
			body = ex.getMessage();
		}
		int httpStatusCode = status.value();
		ErrorResponse customError = new ErrorResponse(httpStatusCode, status.getReasonPhrase(), applicationError, body);

		if (httpStatusCode >= 500) {
			log.error("Severe exception occured while serving request:{}, EX:", ex.getMessage(), ex);
		} else {
			log.debug("Warning exception occured while serving request:{}, EX:", ex.getMessage(), ex);
		}

		return new ResponseEntity<>(customError, headers, status);

	}

}
