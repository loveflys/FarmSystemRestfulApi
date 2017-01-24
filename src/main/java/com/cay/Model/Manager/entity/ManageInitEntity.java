package com.cay.Model.Manager.entity;

import com.cay.Model.BaseEntity;

public class ManageInitEntity extends BaseEntity{
	private String login;
	private String pwd;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
