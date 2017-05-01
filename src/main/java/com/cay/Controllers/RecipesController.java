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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Favorite.vo.favorite;
import com.cay.Model.Manager.vo.Manager;
import com.cay.Model.Recipes.entity.RecipesEntity;
import com.cay.Model.Recipes.entity.RecipesListEntity;
import com.cay.Model.Recipes.vo.Material;
import com.cay.Model.Recipes.vo.Recipes;
import com.cay.Model.Users.vo.User;
import com.cay.repository.ManagerRepository;
import com.cay.repository.RecipesRepository;
import com.cay.repository.UserRepository;

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
	private UserRepository userRepository;
	@Autowired
	private ManagerRepository managerRepository;
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
        r1.setAuthor("588799fc5f8d581d983f0c13");
        r1.setAuthorName("系统管理员");
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
        r2.setAuthor("588799fc5f8d581d983f0c13");
        r2.setAuthorName("系统管理员");
        r2.setViewNum(0);
        r2.setWeight(0);
        mongoTemplate.save(r2);
        
    }
	
	@ApiOperation("新增食谱")
	@PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
    		HttpServletRequest request,
            @RequestParam(value="title", required = true) String title,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="method", required = true) String method,
            @RequestParam(value="imgs", required = true) String imgarray,
            @RequestParam(value="materials", required = true) String materialarray
    ) {
		BaseEntity result = new BaseEntity();
		Recipes recipes = new Recipes();
		List<String> imgs = JSON.parseArray(imgarray, String.class);
		List<Material> materials = JSON.parseArray(materialarray, Material.class);
		if (type == 1) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user == null) {
	        	result.setErr("-200", "查询不到当前用户信息");
	        	return result;
	        }
	        recipes.setAuthor(user.getId());
	        recipes.setAuthorName(user.getName());
		} else {
			Manager manager = managerRepository.findById(request.getHeader("X-USERID"));
	        if (manager == null) {
	        	result.setErr("-200", "查询不到当前管理员信息");
	        	return result;
	        }
	        recipes.setAuthor(manager.getId());
	        recipes.setAuthorName(manager.getName());
		}
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
    		@RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="method", required = false, defaultValue = "") String method,
            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="materials", required = false, defaultValue = "[]") String materialarray,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="weight", required = false, defaultValue = "-1") int weight,
            @RequestParam(value="collectnum", required = false, defaultValue = "-1") long collectnum,
            @RequestParam(value="viewnum", required = false, defaultValue = "-1") long viewnum,
            @RequestParam(value="deleted", required = false, defaultValue = "false") Boolean deleted
    ) {
    	BaseEntity result = new BaseEntity();
    	Recipes recipes = recipesRepository.findById(id);
    	List<String> imgs = JSON.parseArray(imgarray, String.class);
		List<Material> materials = JSON.parseArray(materialarray, Material.class);
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
	
	@ApiOperation("审核食谱")
    @PostMapping("/check")
	@FarmAuth(validate = true)
	public BaseEntity check(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="reason", required = false, defaultValue = "") String reason,
    		@RequestParam(value="status", required = false, defaultValue = "true") boolean status
    ) {
    	BaseEntity result = new BaseEntity();
    	Recipes recipes = recipesRepository.findById(id);
    	
    	if (recipes.getDeleted()) {
    		result.setErr("-200", "食谱已被删除");
			return result;
    	}
    	
    	if (recipes.getStatus() == 1) {
    		result.setErr("-200", "已审核通过，请勿重复审核。");
			return result;
    	}
    	
    	if (recipes.getStatus() == 0 && !status) {
			//审核不通过
			if (reason == null || "".equals(reason)) {
				result.setErr("-200", "拒绝理由不能为空");
				return result;
			} else {
				recipes.setStatus(2);
				recipes.setReason(reason);
			}
		} else {
			recipes.setStatus(1);
		}
    	recipes.setUpdateTime(new Date().getTime());
        mongoTemplate.save(recipes);
        result.setOk();
        return result;
    }
    
    @ApiOperation("获取食谱详情")
    @GetMapping("/get")
    public RecipesEntity get(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = true) String id
    ) {
    	RecipesEntity result = new RecipesEntity();
    	Recipes recipes = recipesRepository.findById(id);
    	String userid = "";
    	if (!"".equals(request.getHeader("X-USERID"))) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user != null) {
	        	userid = user.getId();
	        	Query query = new Query();
	            query.addCriteria(Criteria.where("favUserId").is(userid));
	            query.addCriteria(Criteria.where("favType").is(3));
	            query.addCriteria(Criteria.where("favId").is(id));
	            
	            favorite fav = mongoTemplate.findOne(query, favorite.class);
	        	if (fav != null) {
	        		recipes.setFav(1);
	        	} else {
	        		recipes.setFav(2);
	        	}
	        }
		}
    	recipes.setViewNum(recipes.getViewNum() + 1);
    	mongoTemplate.save(recipes);
        result.setRecipes(recipes);
        result.setOk();
        return result;
    }
    
    @ApiOperation("收藏/取消收藏 食谱")
    @PostMapping("/fav")
    @FarmAuth(validate = true)
    public BaseEntity fav(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="op", required = false, defaultValue = "0") int op
    ) {
    	BaseEntity result = new BaseEntity();
    	String userid = "";
    	if (!"".equals(request.getHeader("X-USERID"))) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user != null) {
	        	userid = user.getId();
	        } else {
	        	result.setErr("-200", "请先登录后再试");
	        }
		} else {
			result.setErr("-200", "请先登录后再试");
		}
    	Recipes recipes = recipesRepository.findById(id);
    	if (recipes == null || recipes.getDeleted() || recipes.getStatus() != 1) {
    		result.setErr("-200", "食谱出错");   
    		return result;
    	} else {
    		Query query = new Query();
	        query.addCriteria(Criteria.where("favUserId").is(userid));
	        query.addCriteria(Criteria.where("favType").is(3));
	        query.addCriteria(Criteria.where("favId").is(id));
    		if (op == 1) {
				favorite temp = mongoTemplate.findOne(query, favorite.class);
				if (temp != null) {
					temp.setFavTime(new Date().getTime());
					recipes.setFavNum(recipes.getFavNum()+1);
					mongoTemplate.save(temp);
					mongoTemplate.save(recipes);
				} else {
					favorite fav = new favorite();
					fav.setFavType(3);
					fav.setFavId(id);
					fav.setFavTime(new Date().getTime());
					fav.setFavUserId(userid);
					recipes.setFavNum(recipes.getFavNum()+1);
					mongoTemplate.save(fav);
					mongoTemplate.save(recipes);
				}
        	} else if (op == 2) {    	        
    	        mongoTemplate.remove(query,
    	        		favorite.class);
    	        recipes.setFavNum(recipes.getFavNum()-1);
    	        mongoTemplate.save(recipes);
        	}    
    	}    
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
            @RequestParam(value="key", required = false, defaultValue = "") String key,
            @RequestParam(value="author", required = false, defaultValue = "") String author,
            @RequestParam(value="cate", required = false, defaultValue = "") String cate,
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
        if (!"".equals(cate)) {
        	query.addCriteria(Criteria.where("materials").elemMatch(Criteria.where("id").is(cate)));
        } 
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(new Criteria().orOperator(Criteria.where("authorName").regex(".*?\\" +key+ ".*"),Criteria.where("title").regex(".*?\\" +key+ ".*")));
        }
        if (status>-1) {
        	query.addCriteria(Criteria.where("status").is(status));
        }
        if (!"".equals(author)) {
        	query.addCriteria(Criteria.where("author").is(author));
        } 
        if (!"".equals(title)) {
        	query.addCriteria(Criteria.where("title").regex(".*?\\" +title+ ".*"));
        } 
        query.addCriteria(Criteria.where("deleted").is(false));
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
	
	@ApiOperation("分页查询收藏的食谱")
	@GetMapping("/listfav")
    public RecipesListEntity listFav(
            HttpServletRequest request,
            @RequestParam(value="favUserId", required = true) String favUserId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "level") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
		RecipesListEntity result = new RecipesListEntity();
        List<favorite> lists=new ArrayList<favorite>();
        List<Recipes> list=new ArrayList<Recipes>();
        Query query = new Query();
        query.addCriteria(Criteria.where("favUserId").is(favUserId));
        query.addCriteria(Criteria.where("favType").is(3));
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, favorite.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		favorite.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, favorite.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            for (favorite fav : lists) {
				if (fav != null && fav.getFavType() == 3) {
					Recipes temp = recipesRepository.findById(fav.getFavId());
					list.add(temp);
				}
			}
            result.setOk();
            result.setList(list);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
}
