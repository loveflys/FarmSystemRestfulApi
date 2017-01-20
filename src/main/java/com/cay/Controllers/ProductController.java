package com.cay.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.entity.ClassEntity;
import com.cay.Model.Classification.entity.ClassListEntity;
import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Product.vo.Product;
import com.cay.repository.ClassRepository;
import com.cay.repository.ProductRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "农产品服务",description="提供农产品增删改查API")
@RestController
@RequestMapping("/pro")
public class ProductController {
	 private final Logger log = Logger.getLogger(this.getClass());
		@Autowired
		private MongoTemplate mongoTemplate;
		@Autowired
		private ProductRepository productRepository;
		
		@GetMapping("/set")
	    public void init() {
			List<String> imgs = new ArrayList<String>();
	        imgs.add("http://m.yuan.cn/content/images/200.png");
			// 初始化数据
	        Product p1 = new Product();
	        p1.setClassification(3);
	        p1.setDeleted(false);
	        p1.setFavNum(0);
	        p1.setImgs(imgs);
	        p1.setIs_off_shelve(false);
	        p1.setMarketid("5880dbe85f8d5813b06ca971");
	        p1.setOldprice(new BigDecimal(0));
	        p1.setOwner("");
	        p1.setPrice(new BigDecimal(8.88));
	        p1.setProName("沂源红富士苹果");
	        p1.setStock(19);
	        p1.setWeight(0);
	        mongoTemplate.save(p1);
	        
	        Product p2 = new Product();
	        p2.setClassification(3);
	        p2.setDeleted(false);
	        p2.setFavNum(0);
	        p2.setImgs(imgs);
	        p2.setIs_off_shelve(false);
	        p2.setMarketid("5880dbe85f8d5813b06ca971");
	        p2.setOldprice(new BigDecimal(0));
	        p2.setOwner("");
	        p2.setPrice(new BigDecimal(12.88));
	        p2.setProName("山东青苹果");
	        p2.setStock(59);
	        p2.setWeight(0);
	        mongoTemplate.save(p2);
	        
	        Product p3 = new Product();
	        p3.setClassification(3);
	        p3.setDeleted(false);
	        p3.setFavNum(0);
	        p3.setImgs(imgs);
	        p3.setIs_off_shelve(false);
	        p3.setMarketid("5880dbe85f8d5813b06ca971");
	        p3.setOldprice(new BigDecimal(0));
	        p3.setOwner("");
	        p3.setPrice(new BigDecimal(18.88));
	        p3.setProName("临沂红富士苹果");
	        p3.setStock(29);
	        p3.setWeight(0);
	        mongoTemplate.save(p3);
	        
	    }
		@ApiOperation("新增产品")
		@PostMapping("/add")
	    public BaseEntity add(
	            @RequestParam(value="classification", required = true) long classification,
	            @RequestParam(value="imgs", required = true) List<String> imgs,
	            @RequestParam(value="descr", required = false, defaultValue = "") String descr,
	            @RequestParam(value="parentId", required = true) long parentId,
	            @RequestParam(value="marketid", required = true) String marketid,
	            @RequestParam(value="owner", required = true) String owner,
	            @RequestParam(value="proname", required = true) String proname
	    ) {
	        BaseEntity result = new BaseEntity();
	        Classification classification = new Classification();
	        classification.setLevel(level);
	        classification.setCode(code);
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
	    public BaseEntity update(
	    		@RequestParam(value="id", required = true) String id,
	    		@RequestParam(value="level", required = true) int level,
	            @RequestParam(value="code", required = true) long code,
	            @RequestParam(value="descr", required = false, defaultValue = "") String descr,
	            @RequestParam(value="parentId", required = true) long parentId,
	            @RequestParam(value="name", required = true) String name,
	            @RequestParam(value="nutrition", required = false, defaultValue = "") String nutrition
	    ) {
	    	BaseEntity result = new BaseEntity();
	    	Classification classification = classRepository.findById(id);
	    	if (!"".equals(descr)) {
	    		classification.setDescr(descr);
	    	}
	    	if (!"".equals(name)) {
	    		classification.setName(name);
	    	}
	    	if (!"".equals(nutrition)) {
	    		classification.setNutrition(nutrition);
	    	}
	    	if (code>0) {
	    		classification.setCode(code);
	    	}
	    	if (level>0) {
	    		classification.setLevel(level);
	    	}
	    	if (parentId>0) {
	    		classification.setParentId(parentId);
	    	}
	        mongoTemplate.save(classification);
	        result.setOk();
	        return result;
	    }    
	    
	    @ApiOperation("获取分类详情")
	    @GetMapping("/get/{id}")
	    public ClassEntity get(
	    		@PathVariable(value="id", required = true) String id
	    ) {
	    	ClassEntity result = new ClassEntity();
	    	Classification classification = classRepository.findById(id);
	        result.setResult(classification);
	        result.setOk();
	        return result;
	    }
	    
	    @ApiOperation("删除分类")
	    @PostMapping("/del")
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
	            @RequestParam(value="level", required = false, defaultValue = "0") int level,
	            @RequestParam(value="code", required = false, defaultValue = "0") long code,
	            @RequestParam(value="parentId", required = false, defaultValue = "0") long parentId,
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
	        if (code>0) {
	        	query.addCriteria(Criteria.where("code").is(code));
	        }
	        if (parentId>0) {
	        	query.addCriteria(Criteria.where("parentId").is(parentId));
	        }
	        if (name!=null && name.length()>0) {
	        	query.addCriteria(Criteria.where("name").regex(".*?\\" +name+ ".*"));
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
