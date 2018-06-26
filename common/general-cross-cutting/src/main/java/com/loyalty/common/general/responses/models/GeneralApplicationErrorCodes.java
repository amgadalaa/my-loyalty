package com.loyalty.common.general.responses.models;

public enum GeneralApplicationErrorCodes implements ErrorCode {

	NO_APPLICATION_CODE("0000", "No Application specific error code"),
	FIELD_VALIDATION_FAILED("0001", "Field Validation failed");

	GeneralApplicationErrorCodes(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	private String errorCode;
	private String message;

	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
