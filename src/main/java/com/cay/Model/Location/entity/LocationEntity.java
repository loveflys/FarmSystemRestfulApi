package com.cay.Model.Location.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Location.vo.Location;
/**
 * 定位信息
 * @author 陈安一
 *
 */
public class LocationEntity extends BaseEntity{
	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
