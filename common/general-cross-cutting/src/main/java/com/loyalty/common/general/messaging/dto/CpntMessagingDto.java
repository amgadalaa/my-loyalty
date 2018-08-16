package com.loyalty.common.general.messaging.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CpntMessagingDto {

	@NotNull
	@Positive
	private Long systemCPntId;

	@NotNull
	@NotEmpty
	private String cPntName;

	// {"systemCPntId":"123","cPntName":"sds"}
	public CpntMessagingDto(Long systemCPntId, String cPntName) {
		super();
		this.systemCPntId = systemCPntId;
		this.cPntName = cPntName;
	}

	/**
	 * Used for de-serialization purposes 
	 */
	public CpntMessagingDto() {
		super();
	}

	public String getcPntName() {
		return cPntName;
	}

	public void setcPntName(String cPntName) {
		this.cPntName = cPntName;
	}

	public Long getSystemCPntId() {
		return systemCPntId;
	}

	public void setSystemCPntId(Long systemCPntId) {
		this.systemCPntId = systemCPntId;
	}

	@Override
	public String toString() {
		return "CpntMessagingDto [systemCPntId=" + systemCPntId + ", cPntName=" + cPntName + "]";
	}

}
