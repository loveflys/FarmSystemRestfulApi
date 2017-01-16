package com.cay.Model.Users.entity;

import com.cay.Model.BaseEntity;
/**
 * 用户登陆
 * @author 陈安一
 *
 */
public class LoginEntity extends BaseEntity{
	private String token;
	private String userid;
	private String name;
	private String avatar;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
