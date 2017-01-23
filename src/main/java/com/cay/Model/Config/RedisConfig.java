package com.cay.Model.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 陈安一 on 2017/1/5.
 */
@Component
@ConfigurationProperties(prefix = "redisconfig")
public class RedisConfig {
    private String ip;
    private String pwd;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
