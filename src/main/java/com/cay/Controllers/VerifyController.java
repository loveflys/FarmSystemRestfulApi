package com.cay.Controllers;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cay.Model.Users.entity.VerifyCodeEntity;
import com.cay.Model.Users.vo.User;
import com.cay.service.UserService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
	@Autowired
	private com.cay.Model.Config.mobileVerifyConfig mobileVerifyConfig;
	@Autowired
	private UserService userService;
	
	@ApiOperation("获取图形验证码")
	@GetMapping("/getimagecode")
	public VerifyCodeEntity getImageCode(HttpServletRequest request) {
		VerifyCodeEntity result = new VerifyCodeEntity();
		String code = ParamUtils.generateNumber(4); 
		result.setImgcode(code);
		//String deviceId = request.getHeader("X-DEVICEID");
		HttpSession session = request.getSession();
        session.setAttribute("validNum", code);
        //redis.opsForValue().set("imgCode_"+deviceId, code);
		return result;
	}
	
	
	@ApiOperation("获取图形验证码")
	@GetMapping("/vrcode/{code}")
	public void getImage(HttpServletRequest request,
			@PathVariable("code") String code,
            HttpServletResponse response) {
		try {
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
	@PostMapping("/getverifycode")
	public BaseEntity getVerifyCode(HttpServletRequest request, 
			@RequestParam("phone") String phone, 
			@RequestParam("imgCode") String imgCode,
			@RequestParam(value="type", required = false, defaultValue="1") int type) {
		BaseEntity result = new BaseEntity();
		if ("".equals(phone)) {
			result.setErr("-101", "手机号码不能为空");
			return result;
		}
		if ("".equals(imgCode)) {
			result.setErr("-102", "图形验证码不能为空");
			return result;
		}
		User temp = userService.findByPhone(phone);
		if(temp != null) {
			result.setErr("-202", "该手机号码已注册");
			return result;
		}
		String code = ParamUtils.generateNumber(4);
		System.out.println("code==>"+code);
		HttpSession session = request.getSession();
		
		String validNum = (String) session.getAttribute("validNum"); //redis.opsForValue().get("imgCode_"+deviceId);
		if (validNum==null||!validNum.toLowerCase().equals(imgCode.toLowerCase())) {
			result.setErr("-103", "图形验证码错误");
			return result;
		}
		
//		session.setAttribute("verifyCode", code);
//		result.setOk();
		TaobaoClient client = new DefaultTaobaoClient(mobileVerifyConfig.getMessageUrl(), mobileVerifyConfig.getAppKey(), mobileVerifyConfig.getAppSecret());
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName(mobileVerifyConfig.getSignName());
		req.setSmsParamString("{\"code\":\""+code+"\",\"time\":\""+mobileVerifyConfig.getValidity()+"分钟\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode(mobileVerifyConfig.getTempId());
		try {
			//验证成功，发送验证码
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
			log.info("发送手机号码."+phone+"==>"+rsp.getBody());
			if (rsp!=null&&rsp.getResult().getErrCode().equals("0")) {
				session.setAttribute("verifyCode", code);
				//redis.opsForValue().set("verifyCode_"+phone, code);
				result.setOk();
			} else {
				result.setErr("-200", rsp.getMsg());
			}
		} catch (ApiException e) {
			result.setErr("-200", e.getErrMsg());
			System.out.print(e.getErrMsg()+"||"+e.getMessage());
			log.info("发送手机号码抛出异常==>"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
