package com.cay.Model.Config;

/**
 * Created by 陈安一 on 2017/2/11.
 */
public class PushExtra {
    private String extraKey;
	private String extraValue;
	public String getExtraKey() {
		return extraKey;
	}
	public PushExtra() {
		
	}
	public PushExtra(String extraKey, String extraValue) {
		super();
		this.extraKey = extraKey;
		this.extraValue = extraValue;
	}
	public void setExtraKey(String extraKey) {
		this.extraKey = extraKey;
	}
	public String getExtraValue() {
		return extraValue;
	}
	public void setExtraValue(String extraValue) {
		this.extraValue = extraValue;
	}
}
