package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Recipes.entity.RecipesEntity;
import com.cay.Model.Recipes.entity.RecipesListEntity;
import com.cay.Model.Recipes.vo.Material;
import com.cay.Model.Recipes.vo.Recipes;
import com.cay.repository.RecipesRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "食谱服务",description="提供食谱增删改查API")
@RestController
@RequestMapping("/recipes")
public class RecipesController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private RecipesRepository recipesRepository;
	
	@GetMapping("/set")
    public void init() {
		List<String> imgs = new ArrayList<String>();
		List<Material> materials = new ArrayList<Material>();
		materials.add(new Material("土豆", "200g"));
		materials.add(new Material("鸡蛋", "2个"));
        imgs.add("http://m.yuan.cn/content/images/200.png");
		// 初始化数据
        Recipes r1 = new Recipes();
        r1.setCollectNum(0);
        r1.setCreateTime(new Date().getTime());
        r1.setDeleted(false);
        r1.setImgs(imgs);
        r1.setMaterials(materials);
        r1.setMethod("这个菜谱的做法是。。。。。。");
        r1.setStatus(0);
        r1.setTitle("土豆煎蛋饼111");
        r1.setViewNum(0);
        r1.setWeight(0);
        mongoTemplate.save(r1);
        
        Recipes r2 = new Recipes();
        r2.setCollectNum(0);
        r2.setCreateTime(new Date().getTime());
        r2.setDeleted(false);
        r2.setImgs(imgs);
        r2.setMaterials(materials);
        r2.setMethod("这个菜谱的做法222是。。。。。。");
        r2.setStatus(0);
        r2.setTitle("土豆煎蛋饼222");
        r2.setViewNum(0);
        r2.setWeight(0);
        mongoTemplate.save(r2);
        
    }
	
	@ApiOperation("新增食谱")
	@PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="title", required = true) String title,
            @RequestParam(value="method", required = true) String method,
            @RequestParam(value="imgs", required = true) String imgarray,
            @RequestParam(value="materials", required = true) String materialarray
    ) {
		BaseEntity result = new BaseEntity();
		Recipes recipes = new Recipes();
		List<String> imgs = JSONArray.parseArray(imgarray, String.class);
		List<Material> materials = JSONArray.parseArray(materialarray, Material.class);
		recipes.setCollectNum(0);
		recipes.setCreateTime(new Date().getTime());
		recipes.setDeleted(false);
		recipes.setImgs(imgs);
		recipes.setMaterials(materials);
		recipes.setMethod(method);
		recipes.setStatus(0);
		recipes.setTitle(title);
		recipes.setViewNum(0);
		recipes.setWeight(0);
        mongoTemplate.save(recipes);
        result.setOk();
        return result;
    }
    
	@ApiOperation("修改食谱")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="title", required = true) String title,
            @RequestParam(value="method", required = true) String method,
            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="materials", required = true) String materialarray,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="weight", required = false, defaultValue = "-1") int weight,
            @RequestParam(value="collectnum", required = false, defaultValue = "-1") long collectnum,
            @RequestParam(value="viewnum", required = false, defaultValue = "-1") long viewnum,
            @RequestParam(value="deleted", required = false, defaultValue = "false") Boolean deleted
    ) {
    	BaseEntity result = new BaseEntity();
    	Recipes recipes = recipesRepository.findById(id);
    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
		List<Material> materials = JSONArray.parseArray(materialarray, Material.class);
    	if (deleted) {
    		recipes.setDeleted(deleted);
    	}
    	if (imgs.size()>0) {
    		recipes.setImgs(imgs);
    	}
    	if (materials.size()>0) {
    		recipes.setMaterials(materials);
    	}
    	if (!"".equals(title)) {
    		recipes.setTitle(title);
    	}
    	if (!"".equals(method)) {
    		recipes.setMethod(method);
    	}
    	if (status > -1 && status != recipes.getStatus()) {
    		recipes.setStatus(status);
    	}
    	if (weight > -1 && weight != recipes.getWeight()) {
    		recipes.setWeight(weight);
    	}
    	if (collectnum>-1 && collectnum != recipes.getCollectNum()) {
    		recipes.setCollectNum(collectnum);
    	}
    	if (viewnum>-1 && viewnum != recipes.getViewNum()) {
    		recipes.setViewNum(viewnum);
    	}
        mongoTemplate.save(recipes);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取食谱详情")
    @GetMapping("/get/{id}")
    public RecipesEntity get(
    		@PathVariable(value="id", required = true) String id
    ) {
    	RecipesEntity result = new RecipesEntity();
    	Recipes recipes = recipesRepository.findById(id);
        result.setRecipes(recipes);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除食谱")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Recipes recipes = recipesRepository.findById(id);
        mongoTemplate.remove(recipes);
        result.setOk();
        return result;
    }   
	
	@ApiOperation("分页查询食谱")
	@GetMapping("/list")
    public RecipesListEntity list(
            HttpServletRequest request,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "level") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
		RecipesListEntity result = new RecipesListEntity();
        List<Recipes> lists=new ArrayList<Recipes>();
        Query query = new Query();
        if (status>-1) {
        	query.addCriteria(Criteria.where("status").is(status));
        }
        if (!"".equals(title)) {
        	query.addCriteria(Criteria.where("title").regex(".*?\\" +title+ ".*"));
        } 
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Recipes.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Recipes.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Recipes.class);
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