package com.cay.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Division.vo.Division;
import com.cay.repository.ClassRepository;

import io.swagger.annotations.Api;

@Api(value = "分类服务",description="提供分类信息增删改查API")
@RestController
@RequestMapping("/class")
public class ClassController {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ClassRepository classRepository;
	@GetMapping("/set")
    public void save() {
        // 初始化数据
        Classification c1 = new Classification();
        c1.setLevel(1);
        c1.setCode(1);
        c1.setDescr("生鲜水果类的描述");
        c1.setName("生鲜水果");
        c1.setNutrition("");
        c1.setParentId(0);
        mongoTemplate.save(c1);
        
        Classification c2 = new Classification();
        c2.setLevel(2);
        c2.setCode(2);
        c2.setDescr("苹果的描述");
        c2.setName("苹果");
        c2.setNutrition("");
        c2.setParentId(1);
        mongoTemplate.save(c2);
        
        Classification c3 = new Classification();
        c3.setLevel(3);
        c3.setCode(3);
        c3.setDescr("红富士苹果的描述");
        c3.setName("红富士苹果");
        c3.setNutrition("钠镁铝硅磷");
        c3.setParentId(2);
        mongoTemplate.save(c3);
        
    }
}
