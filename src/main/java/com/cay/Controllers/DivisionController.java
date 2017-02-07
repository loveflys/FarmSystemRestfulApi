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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Division.entity.DivisionEntity;
import com.cay.Model.Division.entity.DivisionListEntity;
import com.cay.Model.Division.vo.Division;
import com.cay.repository.DivisionRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "区划服务",description="提供区划信息增删改查API")
@RestController
@RequestMapping("/division")
public class DivisionController {
	private final Logger log = Logger.getLogger(this.getClass());
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
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="fullName", required = true) String fullName,
            @RequestParam(value="code", required = true) long code,
            @RequestParam(value="level", required = true) int level,
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value="parentId", required = true) long parent
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
    
    @ApiOperation("修改区划")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="fullName", required = false, defaultValue = "") String fullName,
            @RequestParam(value="code", required = false, defaultValue = "0") long code,
            @RequestParam(value="level", required = false, defaultValue = "0") int level,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="parentId", required = false, defaultValue = "0") long parent
    ) {
    	BaseEntity result = new BaseEntity();
    	Division division = divisionRepository.findById(id);
    	if (!"".equals(fullName)) {
    		division.setCompleteName(fullName);
    	}
    	if (!"".equals(name)) {
    		division.setName(name);
    	}
    	if (code>0) {
    		division.setDivisionCode(code);
    	}
    	if (level>0) {
    		division.setLevel(level);
    	}
    	if (parent>0) {
    		division.setParentId(parent);
    	}
        mongoTemplate.save(division);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取区划详情")
    @PostMapping("/get")
    public DivisionEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	DivisionEntity result = new DivisionEntity();
    	Division division = divisionRepository.findById(id);
        result.setDivision(division);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除区划")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Division division = divisionRepository.findById(id);
        mongoTemplate.remove(division);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("分页查询分类")
	@PostMapping("/list")
	public DivisionListEntity list(
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
    	DivisionListEntity result = new DivisionListEntity();
        List<Division> lists=new ArrayList<Division>();
        Query query = new Query();
        if (level>0) {
        	query.addCriteria(Criteria.where("level").is(level));  
        }
        if (code>0) {
        	query.addCriteria(Criteria.where("divisionCode").is(code));
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
            	System.out.println("每页数据量==>"+pagesize);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Division.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Division.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Division.class);
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
