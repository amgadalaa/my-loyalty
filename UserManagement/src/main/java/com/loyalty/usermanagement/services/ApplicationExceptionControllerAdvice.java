package com.loyalty.usermanagement.services;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.loyalty.common.general.exceptions.ResourceAlreadyExists;
import com.loyalty.common.general.exceptions.ResourceNotExistException;
import com.loyalty.common.general.exceptions.ValidationException;
import com.loyalty.common.general.responses.models.ErrorResponse;
import com.loyalty.common.general.responses.models.GeneralApplicationErrorCodes;
import com.loyalty.common.spring.BeansPriorities;

@ControllerAdvice
@Order(value = BeansPriorities.HIGH)
public class ApplicationExceptionControllerAdvice {

	@ExceptionHandler({ ResourceNotExistException.class })
	public final ResponseEntity<Object> handleNotFoundException(ResourceNotExistException ex) {
		return generateErrorResponse(HttpStatus.NOT_FOUND, ex);
	}

	@ExceptionHandler({ ResourceAlreadyExists.class })
	public final ResponseEntity<Object> handleAlreadyExistsException(ResourceAlreadyExists ex) {
		return generateErrorResponse(HttpStatus.CONFLICT, ex);
	}

	@ExceptionHandler({ ValidationException.class })
	public final ResponseEntity<Object> handleValidationException(ValidationException ex) {
		return generateErrorResponse(HttpStatus.BAD_REQUEST, ex);
	}

	private ResponseEntity<Object> generateErrorResponse(HttpStatus status, Exception ex) {
		ErrorResponse customError = new ErrorResponse(status.value(), status.getReasonPhrase(),
				GeneralApplicationErrorCodes.NO_APPLICATION_CODE, ex.getMessage());
		return ResponseEntity.status(status).body(customError);
	}

}
