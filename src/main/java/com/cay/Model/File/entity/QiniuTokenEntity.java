package com.cay.Model.File.entity;

import com.cay.Model.BaseEntity;
/**
 * 获取七牛token和地址
 * @author 陈安一
 *
 */
public class QiniuTokenEntity extends BaseEntity{
	private String token;
	private String url;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
