package com.cay.Controllers;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.PushHelper;
import com.cay.Model.Config.MongoConfig;
import com.cay.Model.Config.PushConfig;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import io.swagger.annotations.ApiOperation;

/**
 * Created by 陈安一 on 2017/1/4.
 */
@RestController
public class TestController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private PushConfig pushConfig;
	//在极光注册上传应用的 appKey 和 masterSecret  
    private static JPushClient jpush = null;  
    
    @Autowired
    private MongoConfig mongoConfig;

    @ApiOperation("推送消息")
    @RequestMapping(value="/push", method = RequestMethod.GET)
    public com.cay.Model.BaseEntity push (@RequestParam("msg") String msg) {
    	com.cay.Model.BaseEntity base = new com.cay.Model.BaseEntity();
    	jpush = new JPushClient(pushConfig.getMasterSecret(), pushConfig.getAppKey(), null, ClientConfig.getInstance());  
        PushPayload payload = PushHelper.PushAll(msg);
        try {
            PushResult result = jpush.sendPush(payload);
            log.info("极光推送==>Got result - " + result);
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

    @RequestMapping(value="/getMongoConfig", method = RequestMethod.GET)
    public MongoConfig GetMongoConfig () {
        return mongoConfig;
    }
}
