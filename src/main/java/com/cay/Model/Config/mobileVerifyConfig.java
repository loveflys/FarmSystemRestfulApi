package com.cay.Model.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 陈安一 on 2017/1/10.
 */
@Component
@ConfigurationProperties(prefix = "mobileverify")
public class mobileVerifyConfig {
    public String getMessageUrl() {
		return messageUrl;
	}
	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	private String messageUrl;
    private String appKey;
    private String appSecret;
    private String tempId;
    private String validity;
	private String signName;
}
