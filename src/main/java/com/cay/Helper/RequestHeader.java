package com.cay.Helper;

public class RequestHeader {
	private String AppKey;
	public String getAppKey() {
		return AppKey;
	}
	public void setAppKey(String appKey) {
		AppKey = appKey;
	}
	public String getNonce() {
		return Nonce;
	}
	public void setNonce(String nonce) {
		Nonce = nonce;
	}
	public String getCurTime() {
		return CurTime;
	}
	public void setCurTime(String curTime) {
		CurTime = curTime;
	}
	public String getCheckSum() {
		return CheckSum;
	}
	public void setCheckSum(String checkSum) {
		CheckSum = checkSum;
	}
	private String Nonce;
	private String CurTime;
	private String CheckSum;
}
