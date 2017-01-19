package com.cay.Model.Location.vo;

/**
 * 定位信息
 * @author 陈安一
 *
 */
public class Location {
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public Location (double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
