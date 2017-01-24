package com.cay.Model.Manager.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 管理员
 * @author 陈安一
 *
 */
@Document(collection = "manager")
public class Manager {
	@Id
	private String id;
	/** 
	 * 登陆账户
	 */
	private String login;
	private String password;
	/**
	 * 用户名
	 */
	private String name;
	private String realName;
	/**
	 * 管理员类型
	 * 3-普通管理员 4-超级管理员
	 */
	private int type;
	/**
	 * 注册时间
	 */
	private long createTime;
	/**
	 * 修改时间（审核拒绝重新修改的时间）
	 */
	private long updateTime;
	/**
	 * 性别
	 * 1 -男
	 * 2 -女
	 */
	private int sex;
	private String phone;
	private String avatar;
	/*
	 * 是否被删除
	 * 0 为否  1 为已删除
	 */
	private int isdelete;
	/*
	 * 是否被禁用
	 * 0为否 1 为已禁用
	 */
	private int disabled;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public int getDisabled() {
		return disabled;
	}
	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}
}
