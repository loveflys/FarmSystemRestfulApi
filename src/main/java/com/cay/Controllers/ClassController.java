package com.cay.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "分类服务",description="提供分类信息增删改查API")
@RestController
@RequestMapping("/class")
public class ClassController {

}
