package com.cay.Model.Division.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Division.vo.Division;

public class DivisionEntity extends BaseEntity {
	private Division division;

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}
}
