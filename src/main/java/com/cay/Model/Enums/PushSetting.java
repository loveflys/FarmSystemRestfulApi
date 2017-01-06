package com.cay.Model.Enums;

/*
 * Description : 
 * NoPush 0 不接受推送
 * Push 1 接收推送 
 * PushonWifi 2 仅Wifi下接收推送
 */
public enum PushSetting {
	
	NoPush(0),Push(1),PushonWifi(2);
	
	private int value;
	
	private PushSetting(int val) {
		this.value = val;
	}
	
	@Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
