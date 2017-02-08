package com.cay.Model.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 陈安一 on 2017/1/5.
 */
@Component
@ConfigurationProperties(prefix = "fileupload")
public class QiniuConfig {
    private String ak;
	private String sk;
	private String bname;
	private String url;
	public String getAk() {
		return ak;
	}
	public void setAk(String ak) {
		this.ak = ak;
	}
	public String getSk() {
		return sk;
	}
	public void setSk(String sk) {
		this.sk = sk;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
