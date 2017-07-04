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
    public void save() throws InterruptedException {
		getContent.init();
        // 初始化数据
        Classification c1 = new Classification();
        c1.setLevel(1);
        c1.setDescr("");
        c1.setName("肉禽类");
        c1.setNutrition("");
        c1.setParentId("");
        mongoTemplate.save(c1);
        List<Classification> res = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("肉禽类")), Classification.class);
        if (res.size()> 0) {
        	String parentId = res.get(0).getId();
	        String url = "http://m.meishichina.com/ingredient/category/rql/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("blist_p2").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("s2")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl.replaceAll("http://m.meishichina.com/ingredient", "http://www.meishichina.com/YuanLiao"));
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						System.out.println(data+"===>>>"+imgdoc.html());
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);						
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        
        
        
        
        
        
        Classification c2 = new Classification();
        c2.setLevel(1);
        c2.setDescr("");
        c2.setName("水产品类");
        c2.setNutrition("");
        c2.setParentId("");
        mongoTemplate.save(c2);
        
        List<Classification> res1 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("水产品类")), Classification.class);
        if (res1.size()> 0) {
        	String parentId = res1.get(0).getId();
	        String url = "http://m.meishichina.com/ingredient/category/scl/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("blist_p2").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("s2")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl.replaceAll("http://m.meishichina.com/ingredient", "http://www.meishichina.com/YuanLiao"));
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        Classification c3 = new Classification();
        c3.setLevel(1);
        c3.setDescr("");
        c3.setName("蔬菜类");
        c3.setNutrition("");
        c3.setParentId("");
        mongoTemplate.save(c3);
        
        List<Classification> res2 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("蔬菜类")), Classification.class);
        if (res2.size()> 0) {
        	String parentId = res2.get(0).getId();
	        String url = "http://www.meishichina.com/YuanLiao/category/shucailei/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("category_sub").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("category_sub").select("h3")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl);
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        Classification c4 = new Classification();
        c4.setLevel(1);
        c4.setDescr("");
        c4.setName("果品类");
        c4.setNutrition("");
        c4.setParentId("");
        mongoTemplate.save(c4);
        
        List<Classification> res3 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("果品类")), Classification.class);
        if (res3.size()> 0) {
        	String parentId = res3.get(0).getId();
	        String url = "http://www.meishichina.com/YuanLiao/category/guopinlei/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("category_sub").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("category_sub").select("h3")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl);
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        Classification c5 = new Classification();
        c5.setLevel(1);
        c5.setDescr("");
        c5.setName("米面豆乳");
        c5.setNutrition("");
        c5.setParentId("");
        mongoTemplate.save(c5);
        
        List<Classification> res4 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("米面豆乳")), Classification.class);
        if (res4.size()> 0) {
        	String parentId = res4.get(0).getId();
	        String url = "http://www.meishichina.com/YuanLiao/category/mmdr/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("category_sub").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("category_sub").select("h3")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl);
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        Classification c6 = new Classification();
        c6.setLevel(1);
        c6.setDescr("");
        c6.setName("调味品类");
        c6.setNutrition("");
        c6.setParentId("");
        mongoTemplate.save(c6);
        
        List<Classification> res5 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("调味品类")), Classification.class);
        if (res5.size()> 0) {
        	String parentId = res5.get(0).getId();
	        String url = "http://www.meishichina.com/YuanLiao/category/tiaoweipinl/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("category_sub").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("category_sub").select("h3")) {
				if (!"大家都在做".equals(element.html()) && !"TiaoWeiPin".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl);
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        
        Classification c7 = new Classification();
        c7.setLevel(1);
        c7.setDescr("");
        c7.setName("药食");
        c7.setNutrition("");
        c7.setParentId("");
        mongoTemplate.save(c7);
        
        List<Classification> res6 = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is("药食")), Classification.class);
        if (res6.size()> 0) {
        	String parentId = res6.get(0).getId();
	        String url = "http://www.meishichina.com/YuanLiao/category/yaoshiqita/";
			String html = getContent.getContentFromUrl(url);
			Document doc = Jsoup.parse(html);
			Elements content = doc.getElementsByClass("category_sub").select("ul");
			List<String> secondClass = new ArrayList<String>();
			for (Element element : doc.getElementsByClass("category_sub").select("h3")) {
				if (!"大家都在做".equals(element.html())){					
					secondClass.add(element.html());
					Classification tempClass = new Classification();
					tempClass.setLevel(2);
					tempClass.setDescr("");
					tempClass.setName(element.html());
					tempClass.setNutrition("");
					tempClass.setParentId(parentId);
					mongoTemplate.save(tempClass);
				}
			}
			for (int i=0; i < secondClass.size(); i++) {
				List<Classification> Parents = mongoTemplate.find(new Query().addCriteria(Criteria.where("name").is(secondClass.get(i))), Classification.class);
				Classification Parent = Parents.get(0);
				parentId = Parent.getId();
				for (Element temp : content.get(i).select("li")) {					
					String tempUrl = temp.select("a").attr("href");
					String temphtml = getContent.getContentFromUrl(tempUrl);
					Document tempdoc = Jsoup.parse(temphtml);
					Elements imgdoc = tempdoc.getElementsByClass("collect_dp").select("img");
					String img = imgdoc.attr("src");
					if (img.equals("http://static.meishichina.com/v6/img/blank.gif")) {
						img = imgdoc.attr("data-src");
					}
					String data = temp.select("a").html();
					if (data.length() > 0) {
						Classification tempClass = new Classification();
						tempClass.setDescr("");
						tempClass.setLevel(3);
						tempClass.setMainImg(img);
						tempClass.setName(data);
						tempClass.setNutrition("");
						tempClass.setParentId(parentId);
						mongoTemplate.save(tempClass);
					}
				}
			}
        }
        getContent.close();
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
        	query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)));
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
