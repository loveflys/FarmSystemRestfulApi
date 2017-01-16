package com.cay.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "区划服务",description="提供区划信息增删改查API")
@RestController
@RequestMapping("/division")
public class DivisionController {

}
