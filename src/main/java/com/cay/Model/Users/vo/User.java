package com.cay.Model.Users.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cay.Model.Location.vo.Location;
/**
 * 用户
 * @author 陈安一
 *
 */
@Document(collection = "user")
public class User {
	@Id
	private String id;
	private String password;
	/**
	 * 用户名或者商铺/摊位名
	 */
	private String name;
	private String realName;
	/**
	 * 用户类型
	 * 1-用户 2-商户
	 */
	private int type;
	/**
	 * 手持身份证照片
	 */
	private String identityImg;
	/**
	 * 摊位/商户 正面照
	 */
	private String shopImg;
	/**
	 * 商铺/摊位地点（经纬度）
	 */
	private Location shopLocation;
	/**
	 * 所属市场id
	 */
	private String marketid;
	/**
	 * 审核状态
	 * 0-未审核
	 * 1-待审核
	 * 2-审核通过
	 * 3-审核不通过
	 */
	private int status;
	/**
	 * 审核不通过原因
	 */
	private String reason;
	/**
	 * 注册时间
	 */
	private long createTime;
	/**
	 * 修改时间（审核拒绝重新修改的时间）
	 */
	private long updateTime;
	/**
	 * 审核时间
	 */
	private long verifyTime;
	/**
	 * 性别
	 * 1 -男
	 * 2 -女
	 */
	private int sex;
	private String address;
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
	/*
	 * 推送设置
	 * 0: 不接受 1：接收  2：仅在wifi下接收
	 */
	private int pushsetting;	
	
	
	
	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
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





	public String getIdentityImg() {
		return identityImg;
	}





	public void setIdentityImg(String identityImg) {
		this.identityImg = identityImg;
	}





	public String getShopImg() {
		return shopImg;
	}





	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}





	public Location getShopLocation() {
		return shopLocation;
	}





	public void setShopLocation(Location shopLocation) {
		this.shopLocation = shopLocation;
	}





	public String getMarketid() {
		return marketid;
	}





	public void setMarketid(String marketid) {
		this.marketid = marketid;
	}





	public int getStatus() {
		return status;
	}





	public void setStatus(int status) {
		this.status = status;
	}





	public String getReason() {
		return reason;
	}





	public void setReason(String reason) {
		this.reason = reason;
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





	public long getVerifyTime() {
		return verifyTime;
	}





	public void setVerifyTime(long verifyTime) {
		this.verifyTime = verifyTime;
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





	
	
	
	public User () {
		
	}
}
