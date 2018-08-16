package com.loyalty.common.spring.validators;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.loyalty.common.general.services.IValidator;

/**
 * 
 * @author eamgmuh
 *
 * @param <T> The type of the object to be validated
 */
public class DefaultBeanValidator <T> implements IValidator<T> {
	
	public List<String> isValid(T object) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		List<String> violationsStrList = violations.stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());
		return violationsStrList;
	}
}
