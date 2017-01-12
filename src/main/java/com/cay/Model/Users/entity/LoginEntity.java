package com.cay.Model.Users.entity;

import com.cay.Model.BaseEntity;

public class LoginEntity extends BaseEntity{
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
