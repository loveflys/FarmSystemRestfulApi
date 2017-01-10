package com.cay.Controllers;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Helper.ImgValidate.VerificationCodeImgUtil;
import com.cay.Model.BaseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "验证相关",description="提供手机号码验证，图形验证码等功能")
@RestController
@RequestMapping("/verify")
public class VerifyController {
	private final Logger log = Logger.getLogger(this.getClass());
	@ApiOperation("获取图形验证码")
	@GetMapping("/getimagecode")
	public void getImageCode(HttpServletRequest request,
            HttpServletResponse response) {
		try {  
			HttpSession session = request.getSession();
            String code = ParamUtils.generateString(4);  
            session.setAttribute("validNum", code);
            // 生成验证码图片  
            VerificationCodeImgUtil verificationCodeImgUtil = VerificationCodeImgUtil.getInstance(72, 50);  
            verificationCodeImgUtil.initVerificationCode(code);  
            // 设置响应类型  
            response.setContentType("image/jpeg");  
            response.addHeader("Pragma", "No-cache");  
            response.addHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expire", 0l);  
            // 输出图片  
            ImageIO.write(verificationCodeImgUtil.getImage(), "JPEG", response.getOutputStream());  
            System.out.println(code);  
        } catch (Exception e) {  
        	log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
        }  
	}
	
	@ApiOperation("获取手机验证码")
	@GetMapping("/getverifycode")
	public BaseEntity getVerifyCode(HttpServletRequest request, @RequestParam("phone") String phone, @RequestParam("imgCode") String imgCode) {
		
		BaseEntity result = new BaseEntity();
		HttpSession session = request.getSession();
		String validNum = (String) session.getAttribute("validNum");
		if (validNum!=null&&validNum.toLowerCase().equals(imgCode.toLowerCase())) {
			//验证成功，发送验证码
			result.setOk();
		} else {
			result.setErr("-200", "验证码错误，请重新输入。");
		}
		return result;
	}
}
