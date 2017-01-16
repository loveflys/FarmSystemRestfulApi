package com.cay.Model.Location.vo;

import java.math.BigDecimal;

public class Location {
	private BigDecimal longitude;
	private BigDecimal latitude;
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
}
