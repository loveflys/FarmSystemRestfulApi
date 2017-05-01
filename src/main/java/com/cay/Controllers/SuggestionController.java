package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cay.Helper.auth.FarmAuth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
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
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Suggestion.entity.SuggestionEntity;
import com.cay.Model.Suggestion.entity.SuggestionListEntity;
import com.cay.Model.Suggestion.vo.Suggestion;
import com.cay.repository.SuggestionRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "反馈服务",description="提供反馈信息增删改查API")
@RestController
@RequestMapping("/suggestion")
public class SuggestionController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SuggestionRepository suggestionRepository;
	
    @ApiOperation("提交意见")
    @PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="content", required = true) String content,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="ipAddr", required = false, defaultValue = "") String ipAddr,
            @RequestParam(value="deviceId", required = false, defaultValue = "") String deviceId,
            @RequestParam(value="systemType", required = false, defaultValue = "") String systemType,
            @RequestParam(value="systemVersion", required = false, defaultValue = "") String systemVersion,
            @RequestParam(value="deviceModel", required = false, defaultValue = "") String deviceModel,
            @RequestParam(value="deviceName", required = false, defaultValue = "") String deviceName,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat
    ) {
        BaseEntity result = new BaseEntity();
        Suggestion suggestion = new Suggestion();
        suggestion.setContent(content);
        suggestion.setUserId(userId);
        suggestion.setDeviceId(deviceId);
        suggestion.setIpAddr(ipAddr);
        suggestion.setLocation(new Location(lon,lat));
        suggestion.setSystemType(systemType);
        suggestion.setSendTime(new Date().getTime());
        suggestion.setDeviceModel(deviceModel);
        suggestion.setDeviceName(deviceName);
        suggestion.setSystemVersion(systemVersion);
        mongoTemplate.save(suggestion);
        result.setOk();
        return result;
    }
    
    @ApiOperation("获取反馈意见")
    @GetMapping("/get")
    public SuggestionEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	SuggestionEntity result = new SuggestionEntity();
    	Suggestion suggestion = suggestionRepository.findById(id);
        result.setSuggestion(suggestion);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除反馈意见")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Suggestion suggestion = suggestionRepository.findById(id);
        mongoTemplate.remove(suggestion);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("分页查询反馈意见")
	@GetMapping("/list")
	public SuggestionListEntity list(
            HttpServletRequest request,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="ipAddr", required = false, defaultValue = "") String ipAddr,
            @RequestParam(value="deviceId", required = false, defaultValue = "") String deviceId,
            @RequestParam(value="systemType", required = false, defaultValue = "") String systemType,
            @RequestParam(value="systemVersion", required = false, defaultValue = "") String systemVersion,
            @RequestParam(value="deviceModel", required = false, defaultValue = "") String deviceModel,
            @RequestParam(value="deviceName", required = false, defaultValue = "") String deviceName,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "2") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "sendTime") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	SuggestionListEntity result = new SuggestionListEntity();
        List<Suggestion> lists=new ArrayList<Suggestion>();
        Query query = new Query();
        if (!"".equals(userId)) {
        	query.addCriteria(Criteria.where("userId").is(userId));  
        }
        if (!"".equals(key)) {
        	query.addCriteria(Criteria.where("content").regex(".*?\\" +key+ ".*"));  
        }
        if (!"".equals(ipAddr)) {
        	query.addCriteria(Criteria.where("ipAddr").is(ipAddr));  
        }
        if (!"".equals(deviceId)) {
        	query.addCriteria(Criteria.where("deviceId").is(deviceId));  
        }
        if (!"".equals(systemType)) {
        	query.addCriteria(Criteria.where("systemType").is(systemType));  
        }
        if (!"".equals(systemVersion)) {
        	query.addCriteria(Criteria.where("systemVersion").is(systemVersion));  
        }
        if (!"".equals(deviceModel)) {
        	query.addCriteria(Criteria.where("deviceModel").is(deviceModel));  
        }
        if (!"".equals(deviceName)) {
        	query.addCriteria(Criteria.where("deviceName").is(deviceName));  
        }
        if (lon>0&&lat>0) {
        	query.addCriteria(Criteria.where("location").near(new Point(lon,lat)).maxDistance(500));  
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Suggestion.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Suggestion.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Suggestion.class);
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
