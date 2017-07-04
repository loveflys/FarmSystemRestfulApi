package com.cay.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.cay.Helper.auth.FarmAuth;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.cay.Helper.Spider.getContent;
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
		getContent.init();
        // 初始化数据
		long pid = 0;
		long cid = 0;
		String firstName = "";
		String secondName = "";
		String thirdName = "";
		String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201401/t20140116_501070.html";
		String html = getContent.getContentFromUrl(url);
		Document doc = Jsoup.parse(html);
		Elements content = doc.getElementsByClass("xilan_con").select(".TRS_Editor").select("p");
		for (Element temp : content) {
			String[] data = temp.html().split("&nbsp;");
			if (data.length > 0) {
				Division division = new Division();
				division.setNormal(false);
				if (data.length == 4) {
					pid = Long.parseLong(data[0]);
					firstName = data[data.length-1].trim();					
					division.setParentId(0);
					division.setLevel(1);					
					division.setCompleteName(firstName);
				} else if (data.length == 6) {
					cid = Long.parseLong(data[0]);
					secondName = data[data.length-1].trim();		
					division.setParentId(pid);
					division.setLevel(2);	
					division.setCompleteName(firstName+" "+secondName);
				} else {
					thirdName = data[data.length-1].trim();	
					division.setParentId(cid);
					division.setLevel(3);	
					division.setCompleteName(firstName+" "+secondName+" "+thirdName);
				}
		        division.setDivisionCode(Long.parseLong(data[0]));				
		        division.setName(data[data.length-1].trim());	
		        mongoTemplate.save(division);
			}
		} 
		
		getContent.close();
        
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
    @GetMapping("/get")
    public DivisionEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	DivisionEntity result = new DivisionEntity();
    	Division division = divisionRepository.findById(id);
        result.setDivision(division);
        result.setOk();
        return result;
    }
    
    @ApiOperation("获取区划详情bycode")
    @GetMapping("/getbycode")
    public DivisionEntity getbycode(
    		@RequestParam(value="id", required = true) long id
    ) {
    	DivisionEntity result = new DivisionEntity();
    	Division division = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("divisionCode").is(id)), Division.class);
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
    
    @ApiOperation("分页查询区划")
	@GetMapping("/list")
	public DivisionListEntity list(
            HttpServletRequest request,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
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
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(new Criteria().orOperator(Criteria.where("divisionCode").is(key),Criteria.where("completeName").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)),Criteria.where("name").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE))));
        }
        if (name!=null && name.length()>0) {
        	query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE)));
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
