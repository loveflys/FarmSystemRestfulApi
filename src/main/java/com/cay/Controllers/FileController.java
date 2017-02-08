package com.cay.Controllers;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.Config.QiniuConfig;
import com.cay.Model.File.entity.FileEntity;
import com.cay.Model.File.entity.QiniuTokenEntity;
import com.qiniu.util.Auth;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;

@Api(value = "文件服务",description="文件相关api")
@RestController
@RequestMapping("/file")
public class FileController {
	 private final String filePath = "E:/images/";
	 @Autowired
	private QiniuConfig qiniuConfig;
	 private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
	 @RequestMapping(value="/upload", method=RequestMethod.GET)
	 @FarmAuth(validate = true)
	 public @ResponseBody String provideUploadInfo() {
	     return "You can upload a file by posting to this same URL.";
	 }
	 
	 @RequestMapping(value="/upload", method=RequestMethod.POST)
	 @FarmAuth(validate = true)
	 public @ResponseBody
	 FileEntity handleFileUpload(
	         @RequestParam("file") MultipartFile file){
		 FileEntity result = new FileEntity();
		 if (!file.isEmpty()) {
		     Date now = new Date();
			 String name = now.getTime() + ".jpg";
			 File dest = new File(filePath + name);
			 String api = "http://localhost:8282/api/file/images/";
			 // 检测是否存在目录
			 if (!dest.getParentFile().exists()) {
				 dest.getParentFile().mkdirs();
			 }
	         try {
				 file.transferTo(dest);
				 result.setOk();
				 result.setUrl(api + name);
	         } catch (Exception e) {
	             result.setErr("-200", e.getMessage());
	         }
	     } else {
			 result.setErr("-200", "文件呢？？？");
	     }
	     return result;
	 }

	@ApiOperation("获取图片")
	@GetMapping("/images/{path}")
	public void getImage(HttpServletRequest request,
						 @PathVariable("path") String path,
						 HttpServletResponse response) {
		try {
			File dest = new File(filePath + path+".jpg");
			RenderedImage image = ImageIO.read(dest);
			// 设置响应类型
			response.setContentType("image/jpeg");
			response.addHeader("Pragma", "No-cache");
			response.addHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0l);
			// 输出图片
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (Exception e) {
			log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
		}
	}
	
	@ApiOperation("获取七牛云token")
	@GetMapping("/getToken")
	public QiniuTokenEntity getToken(HttpServletRequest request,
						 HttpServletResponse response) {
		QiniuTokenEntity result = new QiniuTokenEntity();
		String ACCESS_KEY = qiniuConfig.getACCESS_KEY();
	    String SECRET_KEY = qiniuConfig.getSECRET_KEY();
	  //密钥配置
	    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	    String token = auth.uploadToken(qiniuConfig.getBucket_Name());
	    result.setOk();
	    result.setToken(token);
	    result.setUrl(qiniuConfig.getURL());
		return result;
	}
}
