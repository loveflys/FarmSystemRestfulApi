package com.cay.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cay.Helper.auth.FarmAuth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.entity.ClassEntity;
import com.cay.Model.Classification.entity.ClassListEntity;
import com.cay.Model.Classification.vo.Classification;
import com.cay.repository.ClassRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "分类服务",description="提供分类信息增删改查API")
@RestController
@RequestMapping("/class")
public class ClassController {
    private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ClassRepository classRepository;
	@GetMapping("/set")
    public void save() {
        // 初始化数据
        Classification c1 = new Classification();
        c1.setLevel(1);
//        c1.setCode(1);
        c1.setDescr("生鲜水果类的描述");
        c1.setName("生鲜水果");
        c1.setNutrition("");
        c1.setParentId("");
        mongoTemplate.save(c1);
        
        Classification c2 = new Classification();
        c2.setLevel(2);
//        c2.setCode(2);
        c2.setDescr("苹果的描述");
        c2.setName("苹果");
        c2.setNutrition("");
        c2.setParentId("");
        mongoTemplate.save(c2);
        
        Classification c3 = new Classification();
        c3.setLevel(3);
//        c3.setCode(3);
        c3.setDescr("红富士苹果的描述");
        c3.setName("红富士苹果");
        c3.setNutrition("钠镁铝硅磷");
        c3.setParentId("");
        mongoTemplate.save(c3);
        
    }
	@ApiOperation("新增分类")
	@PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="level", required = true) int level,
            @RequestParam(value="descr", required = false, defaultValue = "") String descr,
            @RequestParam(value="parentId", required = true) String parentId,
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value="mainImg", required = true) String mainImg,
            @RequestParam(value="nutrition", required = false, defaultValue = "") String nutrition
    ) {
        BaseEntity result = new BaseEntity();
        Classification classification = new Classification();
        classification.setLevel(level);
        if (level == 3) {
        	if (mainImg == null || "".equals(mainImg)) {
        		result.setErr("200", "请上传分类主图");
                return result;
        	} else {
        		classification.setMainImg(mainImg);
        	}
        }
        classification.setDescr(descr);
        classification.setName(name);
        classification.setNutrition(nutrition);
        classification.setParentId(parentId);
        mongoTemplate.save(classification);
        result.setOk();
        return result;
    }
    
	@ApiOperation("修改分类")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="level", required = true) int level,
            @RequestParam(value="descr", required = false, defaultValue = "") String descr,
            @RequestParam(value="mainImg", required = false, defaultValue = "") String mainImg,
            @RequestParam(value="parentId", required = true) String parentId,
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value="nutrition", required = false, defaultValue = "") String nutrition
    ) {
    	BaseEntity result = new BaseEntity();
    	Classification classification = classRepository.findById(id);
    	if (classification.getLevel() == 3) {
        	if (mainImg == null || "".equals(mainImg)) {
        		result.setErr("200", "请上传分类主图");
                return result;
        	} else {
        		classification.setMainImg(mainImg);
        	}
        }
    	if (!"".equals(descr)) {
    		System.out.println("descr==>"+descr);
    		classification.setDescr(descr);
    	}
    	if (!"".equals(name)) {
    		classification.setName(name);
    	}
    	if (!"".equals(nutrition)) {
    		classification.setNutrition(nutrition);
    	}
    	if (level>0) {
    		classification.setLevel(level);
    	}
    	if (!"".equals(parentId)) {
    		classification.setParentId(parentId);
    	}
        mongoTemplate.save(classification);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取分类详情")
    @GetMapping("/get")
    public ClassEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	ClassEntity result = new ClassEntity();
    	Classification classification = classRepository.findById(id);
        result.setResult(classification);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除分类")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Classification classification = classRepository.findById(id);
        mongoTemplate.remove(classification);
        result.setOk();
        return result;
    }   
	
	@ApiOperation("分页查询分类")
	@GetMapping("/list")	
    public ClassListEntity list(
            HttpServletRequest request,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
            @RequestParam(value="level", required = false, defaultValue = "0") int level,
            @RequestParam(value="id", required = false, defaultValue = "") String id,
            @RequestParam(value="parentId", required = false, defaultValue = "") String parentId,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "level") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
        ClassListEntity result = new ClassListEntity();
        List<Classification> lists=new ArrayList<Classification>();
        Query query = new Query();
        if (level>0) {
        	query.addCriteria(Criteria.where("level").is(level));  
        }
        if (!"".equals(parentId)) {
        	query.addCriteria(Criteria.where("parentId").is(parentId));
        }
        if (name!=null && name.length()>0) {
        	query.addCriteria(Criteria.where("name").regex(".*?\\" +name+ ".*"));
        } 
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(Criteria.where("name").regex(".*?\\" +key+ ".*"));
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Classification.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                        Classification.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Classification.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            result.setOk();
            result.setList(lists);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}

}
