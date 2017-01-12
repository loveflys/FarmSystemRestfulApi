package com.cay.Model.Users.entity;

import com.cay.Model.BaseEntity;

public class VerifyCodeEntity extends BaseEntity{
	private String imgcode;

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}
}
