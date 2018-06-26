package com.loyalty.common.general.responses.models;

public class ErrorResponse extends GeneralResponse {

	private int status;
	private String statusMessage;
	private String applicationErrorCode;
	private String applicationErrorMessage;
	private Object errorDetails;

	public ErrorResponse(int status, String statusMessage, ErrorCode applicationError, Object errorDetails) {
		super();
		this.status = status;
		this.statusMessage = statusMessage;
		this.applicationErrorCode = applicationError.getErrorCode();
		this.applicationErrorMessage = applicationError.getMessage();
		this.errorDetails = errorDetails;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getApplicationErrorMessage() {
		return applicationErrorMessage;
	}

	public void setApplicationErrorMessage(String applicationErrorMessage) {
		this.applicationErrorMessage = applicationErrorMessage;
	}

	public Object getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(Object errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getApplicationErrorCode() {
		return applicationErrorCode;
	}

	public void setApplicationErrorCode(String applicationErrorCode) {
		this.applicationErrorCode = applicationErrorCode;
	}

}
