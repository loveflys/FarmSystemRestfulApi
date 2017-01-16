package com.cay.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "信息服务",description="提供信息增删改查API")
@RestController
@RequestMapping("/info")
public class InfoController {

}
