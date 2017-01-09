package com.cay.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MobileHelper {

	public static void main(String[] args) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/sms/sendtemplate.action";
        
        HttpPost httpPost = new HttpPost(url);
        
        RequestHeader header = ParamUtils.getCheckSum();
        httpPost.addHeader("AppKey", header.getAppKey());
        httpPost.addHeader("Nonce", header.getNonce());
        httpPost.addHeader("CurTime", header.getCurTime());
        httpPost.addHeader("CheckSum", header.getCheckSum());
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // 设置请求的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobile", "18669704568"));
        
        try {
            
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            // 打印执行结果
			System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
