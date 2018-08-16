package com.loyalty.common.general.services;

import java.util.List;

public interface IValidator<T> {

	/**
	 * Validates sent object validity, each implementation of {@code IValidator} should have its own  validation logic
	 * @param object
	 * @return list of violations that made {@code object} invalid
	 */
	List<String> isValid(T object);
}
