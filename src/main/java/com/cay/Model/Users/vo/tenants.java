package com.cay.Model.Users.vo;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tenants")
public class tenants {
	@Id
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public ArrayList<Label> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
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
	public int getPushsetting() {
		return pushsetting;
	}
	public void setPushsetting(int pushsetting) {
		this.pushsetting = pushsetting;
	}
	private String username;
	private int sex;
	private String address;
	private String phone;
	private String avatar;
	private ArrayList<Label> labels;
	private Long createtime;
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
	/*
	 * 推送设置
	 * 0: 不接受 1：接收  2：仅在wifi下接收
	 */
	private int pushsetting;	
	public tenants () {
		
	}
 	public tenants (String username, String password, int sex, String address, String phone, String avatar, ArrayList<Label> labels, Long createtime, int isdelete, int disabled, int pushsetting) {
		this.username = username;
		this.password = password;
		this.sex = sex;
		this.pushsetting = pushsetting;
		//if (sex == 0) {
		//	this.sex = Sex.Woman;
		//} else {
		//	this.sex = Sex.Man;
		//}
		this.address = address;
		this.phone = phone;
		this.avatar = avatar;
		this.labels = labels;
		this.createtime = createtime;
		this.isdelete = isdelete;
		this.disabled = disabled;
		//switch (pushsetting) {
		//case 0:
		//	this.pushsetting = PushSetting.NoPush;
		//	break;
		//case 1:
		//	this.pushsetting = PushSetting.Push;
		//	break;
		//case 2:
		//	this.pushsetting = PushSetting.PushonWifi;
		//	break;
		//default:
		//	this.pushsetting = PushSetting.Push;
		//	break;
		//}
	}
	 @Override
	public String toString() {
	  return "User{" +
	   "id=" + id +
	   ", name='" + username + '\'' +
	   ", age=" + avatar +
	  '}';
	  }
}
