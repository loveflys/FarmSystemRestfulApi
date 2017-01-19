package com.cay.Helper;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class PushHelper {	
	/*
	 * 快捷地构建推送对象：所有平台，所有设备，内容为 SEND_MSG 的通知。
	 */
	public static PushPayload PushAll(String msg) {
        return PushPayload.alertAll(msg);
    }
	
	/**
	 * 构建推送对象：所有平台，推送目标是别名为 "target"，通知内容为 SEND_MSG
	 * @Param: target 推送目标
	 * @Param: msg 内容
	 */
	public static PushPayload PushTarget(String target, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(target))
                .setNotification(Notification.alert(msg))
                .build();
    }
	
	/**
	 *@Param: plantform 发送平台 枚举值
	 *@Param: target 推送目标
	 *@Param: title 标题
	 *@Param: msg 内容
	 */
	public static PushPayload PushAndroidWithTitle(Platform plantform, String msg, String target, String title) {
        return PushPayload.newBuilder()
                .setPlatform(plantform)
                .setAudience(Audience.tag(target))
                .setNotification(Notification.android(msg, title, null))
                .build();
    }
}
