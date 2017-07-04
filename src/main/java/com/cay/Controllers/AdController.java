package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
import com.cay.Model.Advertisement.entity.AdEntity;
import com.cay.Model.Advertisement.entity.AdListEntity;
import com.cay.Model.Advertisement.vo.Advertisement;
import com.cay.repository.AdRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "广告服务",description="提供广告增删改查API")
@RestController
@RequestMapping("/ad")
public class AdController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private AdRepository adRepository;
	
	@GetMapping("/set")    
	public void save() {
        // 初始化数据
        Advertisement advertisement1 = new Advertisement();
        advertisement1.setContent("<p>广告内容11111</p>");
        advertisement1.setCreateTime(new Date().getTime());
        advertisement1.setDeleted(false);
        advertisement1.setTitle("文字广告1-无点击");
        advertisement1.setType(2);
        advertisement1.setImg("");
        advertisement1.setPushed(false);
        advertisement1.setResponseType(1);
        advertisement1.setShowType(1);
        advertisement1.setUrl("");
        mongoTemplate.save(advertisement1);        
        
        
        Advertisement advertisement2 = new Advertisement();
        advertisement2.setContent("<p>广告内容11111</p>");
        advertisement2.setCreateTime(new Date().getTime());
        advertisement2.setDeleted(false);
        advertisement2.setTitle("文字广告2-超链接");
        advertisement2.setType(2);
        advertisement2.setImg("");
        advertisement2.setPushed(false);
        advertisement2.setResponseType(2);
        advertisement2.setShowType(1);
        advertisement2.setUrl("http://baidu.com");
        mongoTemplate.save(advertisement2);     
        
        
        Advertisement advertisement3 = new Advertisement();
        advertisement3.setContent("<p>广告内容11111</p>");
        advertisement3.setCreateTime(new Date().getTime());
        advertisement3.setDeleted(false);
        advertisement3.setTitle("文字广告3-进入详情");
        advertisement3.setType(2);
        advertisement3.setImg("");
        advertisement3.setPushed(false);
        advertisement3.setResponseType(3);
        advertisement3.setShowType(1);
        advertisement3.setUrl("");
        mongoTemplate.save(advertisement3);     
        
        Advertisement advertisement4 = new Advertisement();
        advertisement4.setContent("");
        advertisement4.setCreateTime(new Date().getTime());
        advertisement4.setDeleted(false);
        advertisement4.setTitle("图片广告1-无");
        advertisement4.setType(1);
        advertisement4.setImg("http://m.yuan.cn/content/images/200.png");
        advertisement4.setPushed(false);
        advertisement4.setResponseType(1);
        advertisement4.setShowType(1);
        advertisement4.setUrl("");
        mongoTemplate.save(advertisement4); 
        
        Advertisement advertisement5 = new Advertisement();
        advertisement5.setContent("");
        advertisement5.setCreateTime(new Date().getTime());
        advertisement5.setDeleted(false);
        advertisement5.setTitle("图片广告2-超链接");
        advertisement5.setType(1);
        advertisement5.setImg("http://m.yuan.cn/content/images/200.png");
        advertisement5.setPushed(false);
        advertisement5.setResponseType(2);
        advertisement5.setShowType(1);
        advertisement5.setUrl("http://baidu.com");
        mongoTemplate.save(advertisement5); 
        
        Advertisement advertisement6 = new Advertisement();
        advertisement6.setContent("<p>广告内容11111</p>");
        advertisement6.setCreateTime(new Date().getTime());
        advertisement6.setDeleted(false);
        advertisement6.setTitle("图片广告3-进入详情");
        advertisement6.setType(1);
        advertisement6.setImg("http://m.yuan.cn/content/images/200.png");
        advertisement6.setPushed(false);
        advertisement6.setResponseType(3);
        advertisement6.setShowType(1);
        advertisement6.setUrl("");
        mongoTemplate.save(advertisement6);  
    }
    
	@ApiOperation("新增广告")
    @PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="content", required = true) String content,
            @RequestParam(value="title", required = true) String title,
            @RequestParam(value="type", required = true) int type,
            @RequestParam(value="responseType", required = true) int responseType,
            @RequestParam(value="showType", required = true) int showType,
            @RequestParam(value="img", required = false, defaultValue = "") String img,
            @RequestParam(value="url", required = false, defaultValue = "") String url,
            @RequestParam(value="showStartTime", required = false, defaultValue = "0") long showStartTime,
            @RequestParam(value="showEndTime", required = false, defaultValue = "0") long showEndTime
    ) {
    	BaseEntity result = new BaseEntity();
    	if (type == 1 && "".equals(img)) {
    		result.setErr("-200", "请设置图片地址");
    	}
    	if (type == 2 && "".equals(title)) {
    		result.setErr("-201", "请设置广告标题");
    	}
    	if (showType == 2 && (showStartTime == 0 || showEndTime == 0) ) {
    		result.setErr("-202", "请设置展示时间");
    	}
    	if (responseType == 2 && "".equals(url)) {
    		result.setErr("-203", "请设置超链接");
    	}
    	if (responseType == 3 && "".equals(content)) {
    		result.setErr("-204", "请设置广告内容");
    	}
    	Advertisement advertisement = new Advertisement();
        advertisement.setContent(content);
        advertisement.setCreateTime(new Date().getTime());
        advertisement.setDeleted(false);
        advertisement.setTitle(title);
        advertisement.setType(type);
        advertisement.setImg(img);
        advertisement.setPushed(false);
        advertisement.setResponseType(responseType);
        advertisement.setShowType(showType);
        advertisement.setUrl(url);
        advertisement.setShowStartTime(showStartTime);
        advertisement.setShowEndTime(showEndTime);
        mongoTemplate.save(advertisement);
        result.setOk();
        return result;
    }
    
    @ApiOperation("修改信息")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="content", required = false, defaultValue = "0") String content,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="type", required = false, defaultValue = "-1") int type,
            @RequestParam(value="deleted", required = false, defaultValue = "-1") int deleted,
            @RequestParam(value="responseType", required = false, defaultValue = "-1") int responseType,
            @RequestParam(value="showType", required = false, defaultValue = "-1") int showType,
            @RequestParam(value="pushed", required = false, defaultValue = "-1") int pushed,
            @RequestParam(value="img", required = false, defaultValue = "") String img,
            @RequestParam(value="url", required = false, defaultValue = "") String url,
            @RequestParam(value="showStartTime", required = false, defaultValue = "0") long showStartTime,
            @RequestParam(value="showEndTime", required = false, defaultValue = "0") long showEndTime
    ) {
    	BaseEntity result = new BaseEntity();
    	Advertisement ad = adRepository.findById(id);
    	if (!"".equals(content)) {
    		ad.setContent(content);
    	}    	
    	if (!"".equals(title)) {
    		ad.setTitle(title);
    	}
    	if (!"".equals(img)) {
    		ad.setImg(img);
    	}
    	if (pushed > -1) {
    		if (pushed == 1) {
    			ad.setPushed(true);
    			ad.setPushTime(new Date().getTime());
    		} else {
    			ad.setPushed(false);
    		}
    	}
    	if (deleted > 0) {
    		if (deleted == 1) {
    			ad.setDeleted(true);
    			ad.setDeleteTime(new Date().getTime());
    		} else {
    			ad.setDeleted(false);
    		}
    	}
    	if (!"".equals(url)) {
    		ad.setUrl(url);
    	}
    	if (type > -1) {
    		ad.setType(type);
    	}
    	if (showType > -1) {
    		ad.setShowType(type);
    	}
    	if (responseType > -1) {
    		ad.setResponseType(responseType);
    	}
    	if (showStartTime > 0) {
    		ad.setShowStartTime(showStartTime);
    	}
    	if (showEndTime > 0) {
    		ad.setShowEndTime(showEndTime);
    	}
        mongoTemplate.save(ad);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取广告")
    @GetMapping("/get")
    public AdEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	AdEntity result = new AdEntity();
    	Advertisement ad = adRepository.findById(id);
        result.setAd(ad);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除广告")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Advertisement ad = adRepository.findById(id);
        mongoTemplate.remove(ad);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("分页查询分类")
    @GetMapping("/list")
	public AdListEntity list(
            HttpServletRequest request,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="pushed", required = false, defaultValue = "0") int pushed,
            @RequestParam(value="responseType", required = false, defaultValue = "0") int responseType,
            @RequestParam(value="showType", required = false, defaultValue = "0") int showType,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	AdListEntity result = new AdListEntity();
        List<Advertisement> lists = new ArrayList<Advertisement>();
        Query query = new Query();
        if (pushed == 1) {
        	query.addCriteria(Criteria.where("pushed").is(true));  
        } else if (pushed == 2) {
        	query.addCriteria(Criteria.where("pushed").is(false)); 
        }
        if (type>0) {
        	query.addCriteria(Criteria.where("type").is(type));  
        }
        if (responseType>0) {
        	query.addCriteria(Criteria.where("responseType").is(responseType));  
        }
        if (showType>0) {
        	query.addCriteria(Criteria.where("showType").is(showType));  
        }
        if (title!=null && title.length()>0) {
        	query.addCriteria(Criteria.where("title").regex(Pattern.compile("^.*"+title+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        query.addCriteria(Criteria.where("deleted").is(false));  
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Advertisement.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Advertisement.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Advertisement.class);
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
