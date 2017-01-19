package com.cay.Controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;

@Api(value = "文件服务",description="文件相关api")
@RestController
@RequestMapping("/file")
public class FileController {
	 @RequestMapping(value="/upload", method=RequestMethod.GET)
	 public @ResponseBody String provideUploadInfo() {
	     return "You can upload a file by posting to this same URL.";
	 }
	 
	 @RequestMapping(value="/upload", method=RequestMethod.POST)
	 public @ResponseBody String handleFileUpload(
			 @RequestParam("name") String name,
			 HttpServletRequest request, 
	         @RequestParam("file") MultipartFile file){
		 if (!file.isEmpty()) {
		     String fileFullName = file.getOriginalFilename();
		     int index = fileFullName.lastIndexOf(".");
		     String fileName = fileFullName.substring(0, index-1);
		     String fileEndName = fileFullName.substring(index);
		     Date now = new Date();
		     
		     System.out.println(fileName+"||||"+fileEndName);
	         try {
	             byte[] bytes = file.getBytes();
	             String rootPath = request.getSession().getServletContext().getRealPath("/images");
	             System.out.println("rootPath==>"+rootPath);
	             BufferedOutputStream stream =
	             new BufferedOutputStream(new FileOutputStream(new File(rootPath+"\\"+now.getTime() + fileEndName)));
	             stream.write(bytes);
	             stream.close();
	             return "You successfully uploaded " + name + " into " + name + "-uploaded !";
	         } catch (Exception e) {
	             return "You failed to upload " + name + " => " + e.getMessage();
	         }
	     } else {
	         return "You failed to upload " + name + " because the file was empty.";
	     }
	 }
}
