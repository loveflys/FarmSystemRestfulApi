package com.cay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 陈安一 on 2017/1/5.
 */
@Component
@ConfigurationProperties(prefix = "mongo")
public class MongoConfig {
    private String ip;
    private String port;
    private String user;
    private String user_pw;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getRoot_pw() {
        return root_pw;
    }

    public void setRoot_pw(String root_pw) {
        this.root_pw = root_pw;
    }

    private String root_pw;
}
