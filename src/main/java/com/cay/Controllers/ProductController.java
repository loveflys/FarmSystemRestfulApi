package com.cay.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
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
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Product.entity.ProductEntity;
import com.cay.Model.Product.entity.ProductListEntity;
import com.cay.Model.Product.vo.Product;
import com.cay.Model.Users.vo.User;
import com.cay.repository.ProductRepository;
import com.cay.repository.UserRepository;

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
		private UserRepository userRepository;
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
	        p1.setShopLocation(new Location(118.668089, 37.449626));
	        p1.setWeight(0);
	        mongoTemplate.save(p1);
	        
	        Product p2 = new Product();
	        p2.setClassification(3);
	        p2.setDeleted(false);
	        p2.setFavNum(0);
	        p2.setImgs(imgs);
	        p2.setIs_off_shelve(false);
	        p2.setMarketid("5880dbe85f8d5813b06ca971");
	        p2.setShopLocation(new Location(118.668089, 37.449626));
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
	        p3.setShopLocation(new Location(118.668089, 37.449626));
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
	            @RequestParam(value="imgs", required = true) String imgarray,
	            @RequestParam(value="price", required = true) double price,
	            @RequestParam(value="marketid", required = true) String marketid,
	            @RequestParam(value="owner", required = true) String owner,
	            @RequestParam(value="proname", required = true) String proname,
	            @RequestParam(value="stock", required = true) int stock,
	            @RequestParam(value="weight", required = true) int weight
	    ) {
	        BaseEntity result = new BaseEntity();
	        List<String> imgs = JSONArray.parseArray(imgarray, String.class);
	        User user = userRepository.findById(owner);
	        if (user == null || user.getShopLocation() == null) {
	        	result.setErr("-200", "查询不到商户信息or商户地址信息为空");
	        }
	        Product product = new Product();
	        product.setShopLocation(user.getShopLocation());
	        product.setClassification(classification);
	        product.setDeleted(false);
	        product.setFavNum(0);
	        product.setImgs(imgs);
	        product.setIs_off_shelve(false);
	        product.setMarketid(marketid);
	        product.setOldprice(new BigDecimal(0));
	        product.setPrice(new BigDecimal(price));
	        product.setOwner(owner);
	        product.setStock(stock);
	        product.setWeight(weight);
	        mongoTemplate.save(product);
	        result.setOk();
	        return result;
	    }
	    
		@ApiOperation("修改产品")
	    @PostMapping("/update")
	    public BaseEntity update(
	    		@RequestParam(value="id", required = true) String id,
	    		@RequestParam(value="classCode", required = false, defaultValue = "0") long classCode,
	            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
	            @RequestParam(value="price", required = false, defaultValue = "0") double price,
	            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
	            @RequestParam(value="owner", required = false, defaultValue = "") String owner,
	            @RequestParam(value="proName", required = false, defaultValue = "") String proName,
	            @RequestParam(value="stock", required = false, defaultValue = "0") int stock,
	            @RequestParam(value="weight", required = false, defaultValue = "0") int weight,
	            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
	            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
	            @RequestParam(value="deleted", required = false, defaultValue = "false") Boolean deleted
	    ) {
	    	BaseEntity result = new BaseEntity();
	    	Product product = productRepository.findById(id);
	    	if (deleted) {
	    		product.setDeleted(deleted);
	    	}
	    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
	    	if (imgs.size()>0) {
	    		product.setImgs(imgs);
	    	}
	    	if (!"".equals(marketid)) {
	    		product.setMarketid(marketid);
	    	}
	    	if (!"".equals(owner)) {
	    		product.setOwner(owner);
	    	}
	    	if (!"".equals(proName)) {
	    		product.setProName(proName);
	    	}
	    	if (classCode > 0) {
	    		product.setClassification(classCode);
	    	}
	    	if (lon > 0 && lat > 0) {
	    		product.setShopLocation(new Location(lon,lat));
	    	}
	    	if (price>0) {
	    		BigDecimal oldprice = product.getPrice();
	    		product.setOldprice(oldprice);
	    		product.setPrice(new BigDecimal(price));
	    	}
	    	if (stock>0) {
	    		product.setStock(stock);
	    	}
	    	if (weight>0) {
	    		product.setWeight(weight);
	    	}
	        mongoTemplate.save(product);
	        result.setOk();
	        return result;
	    }    
	    
	    @ApiOperation("获取产品详情")
	    @GetMapping("/get/{id}")
	    public ProductEntity get(
	    		@PathVariable(value="id", required = true) String id
	    ) {
	    	ProductEntity result = new ProductEntity();
	    	Product product = productRepository.findById(id);
	        result.setProduct(product);
	        result.setOk();
	        return result;
	    }
	    
	    @ApiOperation("删除产品")
	    @PostMapping("/del")
	    public BaseEntity del(
	    		@RequestParam(value="id", required = true) String id
	    ) {
	    	BaseEntity result = new BaseEntity();
	    	Product product = productRepository.findById(id);
	        mongoTemplate.remove(product);
	        result.setOk();
	        return result;
	    }   
		
		@ApiOperation("分页查询产品")
		@GetMapping("/list")	
	    public ProductListEntity list(
	            HttpServletRequest request,
	            @RequestParam(value="classCode", required = false, defaultValue = "0") long classCode,
	            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
	            @RequestParam(value="proName", required = false, defaultValue = "") String proName,
	            @RequestParam(value="owner", required = false, defaultValue = "") String owner,
	            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
	            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
	            @RequestParam(value="max", required = false, defaultValue = "0") double max,
	            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
	            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
	            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
	            @RequestParam(value="sortby", required = false, defaultValue = "level") String sortby,
	            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
	    ) {
			ProductListEntity result = new ProductListEntity();
	        List<Product> lists=new ArrayList<Product>();
	        Query query = new Query();
	        if (lon>0&&lat>0&&max>0) {
	        	query.addCriteria(Criteria.where("shopLocation").near(new Point(lon,lat)).maxDistance(max));  
	        }
	        if (classCode > 0) {
	        	query.addCriteria(Criteria.where("classification").is(classCode));  
	        }
	        if (!"".equals(marketid)) {
	        	query.addCriteria(Criteria.where("marketid").is(marketid));
	        }
	        if (!"".equals(owner)) {
	        	query.addCriteria(Criteria.where("owner").is(owner));
	        }
	        if (!"".equals(proName)) {
	        	query.addCriteria(Criteria.where("proName").regex(".*?\\" +proName+ ".*"));
	        } 
	        try {
	            if (paged == 1) {
	            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
	                //构建分页信息
	                long totalCount = mongoTemplate.count(query, Product.class);
	                //查询指定分页的内容
	                lists = mongoTemplate.find(query.with(pageRequest),
	                		Product.class);
	                long totalPage = (totalCount+pagesize-1)/pagesize;
	                result.setTotalCount(totalCount);
	                result.setTotalPage(totalPage);
	                
	            } else {
	            	lists = mongoTemplate.find(query, Product.class);
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
