package com.cay.Helper;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="mobileverify")
public class MobileConfig {
	private String appKey;

	private String appSecret;
	
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
}
