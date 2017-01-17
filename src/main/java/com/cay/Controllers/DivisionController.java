package com.cay.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Model.BaseEntity;
import com.cay.Model.Division.vo.Division;
import com.cay.repository.DivisionRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "区划服务",description="提供区划信息增删改查API")
@RestController
@RequestMapping("/division")
public class DivisionController {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private DivisionRepository divisionRepository;
	@GetMapping("/set")
    public void save() {
        // 初始化数据
        Division division1 = new Division();
        division1.setCompleteName("山东省");
        division1.setDivisionCode(370000);
        division1.setLevel(1);
        division1.setName("山东省");
        division1.setParentId(0);
        mongoTemplate.save(division1);
        
        Division division2 = new Division();
        division2.setCompleteName("山东省 东营市");
        division2.setDivisionCode(370500);
        division2.setLevel(2);
        division2.setName("东营市");
        division2.setParentId(370000);
        mongoTemplate.save(division2);
        
        Division division3 = new Division();
        division3.setCompleteName("山东省 东营市 市辖区");
        division3.setDivisionCode(370501);
        division3.setLevel(3);
        division3.setName("市辖区");
        division3.setParentId(370500);
        mongoTemplate.save(division3);
        
        Division division4 = new Division();
        division4.setCompleteName("山东省 东营市 东营区");
        division4.setDivisionCode(370502);
        division4.setLevel(3);
        division4.setName("东营区");
        division4.setParentId(370500);
        mongoTemplate.save(division4);
        
        Division division5 = new Division();
        division5.setCompleteName("山东省 东营市河口区");
        division5.setDivisionCode(370503);
        division5.setLevel(3);
        division5.setName("河口区");
        division5.setParentId(370500);
        mongoTemplate.save(division5);
        
        Division division6 = new Division();
        division6.setCompleteName("山东省 东营市 垦利县");
        division6.setDivisionCode(370521);
        division6.setLevel(3);
        division6.setName("垦利县");
        division6.setParentId(370500);
        mongoTemplate.save(division6);
        
        Division division7 = new Division();
        division7.setCompleteName("山东省 东营市 利津县");
        division7.setDivisionCode(370522);
        division7.setLevel(3);
        division7.setName("利津县");
        division7.setParentId(370500);
        mongoTemplate.save(division7);
        
        Division division8 = new Division();
        division8.setCompleteName("山东省 东营市 广饶县");
        division8.setDivisionCode(370523);
        division8.setLevel(3);
        division8.setName("广饶县");
        division8.setParentId(370500);
        mongoTemplate.save(division8);
    }
    @ApiOperation("新增区划")
    @PostMapping("/add")
    public BaseEntity add(
            @RequestParam(value="fullName", required = true) String fullName,
            @RequestParam(value="code", required = true) long code,
            @RequestParam(value="level", required = true) int level,
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value="parent", required = true) long parent
    ) {
        BaseEntity result = new BaseEntity();
        Division division = new Division();
        division.setCompleteName(fullName);
        division.setDivisionCode(code);
        division.setLevel(level);
        division.setName(name);
        division.setParentId(parent);
        mongoTemplate.save(division);
        result.setOk();
        return result;
    }
}
