package com.loyalty.pointingsystem.lusrs.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddPointsRequest {

	@NotNull(message = "plese put message")
	@Positive
	private Double points;

	@NotNull
	private Long cashierPntID;

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Long getCashierPntID() {
		return cashierPntID;
	}

	public void setCashierPntID(Long cashierPntID) {
		this.cashierPntID = cashierPntID;
	}

	@Override
	public String toString() {
		return "AddPointsRequest [points=" + points + ", cashierPntID=" + cashierPntID + "]";
	}

}
