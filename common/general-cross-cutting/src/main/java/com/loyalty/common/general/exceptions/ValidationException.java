package com.loyalty.common.general.exceptions;

import java.util.List;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 5708467988667298739L;

	private List<String> listOfViolations;

	public ValidationException(String message, List<String> listOfViolations) {
		super(message);
		this.listOfViolations = listOfViolations;
	}

	public List<String> getListOfViolations() {
		return listOfViolations;
	}

}
