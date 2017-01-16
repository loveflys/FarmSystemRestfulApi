package com.cay.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "农产品服务",description="提供农产品增删改查API")
@RestController
@RequestMapping("/pro")
public class ProductController {

}
