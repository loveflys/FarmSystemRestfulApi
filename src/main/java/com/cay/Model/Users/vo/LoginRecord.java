package com.cay.Model.Users.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
public class LoginRecord {
	@Id
	private String id;
	private String token;	
	private String phone;
	/**
	 * 登录设备唯一标识
	 */
	private String deviceId;
	/**
	 * 操作类型
	 * 1、注册并登录
	 * 2、登录
	 * 3、退出登录
	 */
	private int operate; 
	private long op_time;
	/**
	 * 登录身份：
	 * 1、用户
	 * 2、商户
	 * 3、管理员
	 */
	private int login_identity;
	private String location;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getOperate() {
		return operate;
	}
	public void setOperate(int operate) {
		this.operate = operate;
	}
	public long getOp_time() {
		return op_time;
	}
	public void setOp_time(long op_time) {
		this.op_time = op_time;
	}
	public int getLogin_identity() {
		return login_identity;
	}
	public void setLogin_identity(int login_identity) {
		this.login_identity = login_identity;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
