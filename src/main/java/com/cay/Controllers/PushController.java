package com.cay.Controllers;
import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.auth.FarmAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cay.Model.Config.PushConfig;
import com.cay.Model.Config.PushExtra;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import io.swagger.annotations.ApiOperation;

/**
 * Created by 陈安一 on 2017/1/4.
 */
@RestController
public class PushController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private PushConfig pushConfig;
	//在极光注册上传应用的 appKey 和 masterSecret  
    private static JPushClient jpush = null;  

    @ApiOperation("推送消息")
    @RequestMapping(value="/push", method = RequestMethod.GET)
    @FarmAuth(validate = true)
    public com.cay.Model.BaseEntity push (
    		@RequestParam("msg") String msg,
    		@RequestParam("title") String title,
    		@RequestParam("msgContent") String msgContent,
    		@RequestParam("extra") String extra) {
    	com.cay.Model.BaseEntity base = new com.cay.Model.BaseEntity();
    	jpush = new JPushClient(pushConfig.getMasterSecret(), pushConfig.getAppKey(), null, ClientConfig.getInstance());  
    	List<PushExtra> extralist = JSONArray.parseArray(extra, PushExtra.class);
    	Map<String,String> extras = new HashMap<String,String>();
    	for (PushExtra pushExtra : extralist) {
			extras.put(pushExtra.getExtraKey(), pushExtra.getExtraValue());
		}
        PushPayload payload1 = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                		.setTitle(title)
                		.setMsgContent(msgContent)
                		.addExtras(extras)
                		.build())
                .setNotification(Notification.android(msg, title, extras))
                .build();//PushHelper.PushAll(msg);
        PushPayload payload2 = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                		.setTitle(title)
                		.setMsgContent(msgContent)
                		.addExtras(extras)
                		.build())
                .setNotification(Notification.ios(msg, extras))
                .build();
        try {
            PushResult result1 = jpush.sendPush(payload1);
            PushResult result2 = jpush.sendPush(payload2);
            log.info("极光推送==>Got result - " + result1+"||"+result2);
            base.setOk();
        } catch (APIConnectionException e) {
        	base.setErr("-200", e.getMessage());;
            // Connection error, should retry later
        	log.error("极光推送连接异常==>Connection error, should retry later", e);

        } catch (APIRequestException e) {
        	base.setErr("-200", e.getMessage());;
            // Should review the error, and fix the request
        	log.error("极光推送请求异常==>Should review the error, and fix the request", e);
        	log.info("极光推送请求异常==>HTTP Status: " + e.getStatus());
        	log.info("极光推送请求异常==>Error Code: " + e.getErrorCode());
        	log.info("极光推送请求异常==>Error Message: " + e.getErrorMessage());
        }
    	
    	
        return base;
    }
}
