package com.cay.Model.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 陈安一 on 2017/1/5.
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {
    private String ACCESS_KEY;
	private String SECRET_KEY;
	private String Bucket_Name;
	private String URL;
	public String getACCESS_KEY() {
		return ACCESS_KEY;
	}
	public void setACCESS_KEY(String aCCESS_KEY) {
		ACCESS_KEY = aCCESS_KEY;
	}
	public String getSECRET_KEY() {
		return SECRET_KEY;
	}
	public void setSECRET_KEY(String sECRET_KEY) {
		SECRET_KEY = sECRET_KEY;
	}
	public String getBucket_Name() {
		return Bucket_Name;
	}
	public void setBucket_Name(String bucket_Name) {
		Bucket_Name = bucket_Name;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
}
