package com.cay.Helper.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.BaseEntity;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.PrintWriter;

/**
 * 自定义拦截器1
 */
public class MyInterceptor implements HandlerInterceptor {
	private final Logger log = Logger.getLogger(this.getClass());
	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            {
    	boolean results = true;
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){        	
            FarmAuth auth = ((HandlerMethod) handler).getMethodAnnotation(FarmAuth.class);            
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"||请求参数==>"+JSON.toJSONString(request.getParameterMap()));
            String userId = request.getHeader("X-USERID");
            jedisPoolConfig.setMaxIdle(8);
            jedisPoolConfig.setMaxWait(-1);
            JedisPool pool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
            Jedis jedis = pool.getResource();
        	//没有声明需要权限,或者声明不验证权限
            if(auth == null || auth.validate() == false) {
            	if (jedis.isConnected()) {
                    if (jedis.exists("token_" + userId)) {
                        //延长至3天
                        jedis.expire("token_" + userId, 60*60*24*3);
                    }
            	}
            	results = true;
            } else{
            	if (jedis.isConnected()) {
                    if (!jedis.exists("token_"+userId)) {
                        response.setHeader("Content-type","application/json;charset=UTF-8");//向浏览器发送一个响应头，设置浏览器的解码方式为UTF-8
                        BaseEntity result = new BaseEntity();
                        result.setErr("-888", "token失效，请登录后再试。");
                        PrintWriter out = null;
                        try {
                            out = response.getWriter();
                            out.append(JSONObject.toJSON(result).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                out.close();
                            }
                        }
                        results = false;
                    } else {
                        //延长至3天
                        jedis.expire("token_" + userId, 60*60*24*3);
                        results = true;
                    }
            	} else {
            		results = false;
            	}
            }          
        }
        return results;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }

}