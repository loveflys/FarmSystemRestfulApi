package com.cay.Model.Users.entity;

import com.cay.Model.BaseEntity;
/**
 * 图形验证码
 * @author 陈安一
 *
 */
public class VerifyCodeEntity extends BaseEntity{
	private String imgcode;

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}
}
