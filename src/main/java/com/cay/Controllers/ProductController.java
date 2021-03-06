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
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Favorite.vo.favorite;
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Market.vo.Market;
import com.cay.Model.Product.entity.ProductEntity;
import com.cay.Model.Product.entity.ProductListEntity;
import com.cay.Model.Product.vo.Product;
import com.cay.Model.Product.vo.ProductPriceChange;
import com.cay.Model.Users.vo.User;
import com.cay.Model.Users.vo.UserCate;
import com.cay.repository.ClassRepository;
import com.cay.repository.MarketRepository;
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
		private ClassRepository classRepository;
		@Autowired
		private ProductRepository productRepository;
		@Autowired
		private MarketRepository marketRepository;
		
		@GetMapping("/set")
	    public void init() {
			mongoTemplate.indexOps(Product.class).ensureIndex(new GeospatialIndex("shopLocation").typed(GeoSpatialIndexType.GEO_2DSPHERE));
			List<String> imgs = new ArrayList<String>();
	        imgs.add("http://m.yuan.cn/content/images/200.png");
			// 初始化数据
	        Product p1 = new Product();
	        p1.setClassification(new ArrayList<String>(){});
	        p1.setDeleted(false);
	        p1.setFavNum(0);
	        p1.setImgs(imgs);
	        p1.setIs_off_shelve(false);
	        p1.setMarketid("58b83dc85f8d582fc4ea47dc");
	        p1.setMarketName("好宜家小商品城");
	        p1.setOldprice(688);
	        p1.setOwner("58b83ed15f8d58338cf6badb");
	        p1.setOwnerName("安一水果摊");
	        p1.setPrice(888);
	        p1.setProName("沂源红富士苹果");
	        p1.setStock(19);
	        p1.setShopLocation(new Location(118.668089, 37.449626));
	        p1.setWeight(0);
	        mongoTemplate.save(p1);
	        
	        Product p2 = new Product();
	        p2.setClassification(new ArrayList<String>(){});
	        p2.setDeleted(false);
	        p2.setFavNum(0);
	        p2.setImgs(imgs);
	        p2.setIs_off_shelve(false);
	        p2.setMarketid("58b83dc85f8d582fc4ea47dc");
	        p2.setShopLocation(new Location(117.668089, 37.449626));
	        p2.setMarketName("好宜家小商品城");
	        p2.setOldprice(1150);
	        p2.setOwner("58b83ed15f8d58338cf6badb");
	        p2.setOwnerName("安一水果摊");
	        p2.setPrice(1288);
	        p2.setProName("山东青苹果");
	        p2.setStock(59);
	        p2.setWeight(0);
	        mongoTemplate.save(p2);
	        
	        Product p3 = new Product();
	        p3.setClassification(new ArrayList<String>(){});
	        p3.setDeleted(false);
	        p3.setFavNum(0);
	        p3.setImgs(imgs);
	        p3.setIs_off_shelve(false);
	        p3.setMarketid("58b83dc85f8d582fc4ea47dc");
	        p3.setShopLocation(new Location(118, 37));
	        p3.setMarketName("好宜家小商品城");
	        p3.setOldprice(1900);
	        p3.setOwner("58b83ed15f8d58338cf6badb");
	        p3.setOwnerName("安一水果摊");
	        p3.setPrice(1800);
	        p3.setProName("临沂红富士苹果");
	        p3.setStock(29);
	        p3.setWeight(0);
	        mongoTemplate.save(p3);
	        
	    }
		
		@GetMapping("/setIndex")
		public void setIndex () {
			mongoTemplate.indexOps(Product.class).ensureIndex(new GeospatialIndex("shopLocation").typed(GeoSpatialIndexType.GEO_2DSPHERE));
		}
		@GetMapping("/setFIndex")
		public void setFIndex () {
			mongoTemplate.indexOps(favorite.class).ensureIndex(new GeospatialIndex("shopLocation").typed(GeoSpatialIndexType.GEO_2DSPHERE));
		}
		@ApiOperation("新增产品")
		@PostMapping("/add")
		@FarmAuth(validate = true)
	    public BaseEntity add(
	            @RequestParam(value="classification", required = true) String classification,
	            @RequestParam(value="imgs", required = true) String imgarray,
	            @RequestParam(value="price", required = true) long price,
	            @RequestParam(value="marketid", required = true) String marketid,
	            @RequestParam(value="owner", required = true) String owner,
	            @RequestParam(value="proname", required = true) String proname,
	            @RequestParam(value="stock", required = true) int stock,
	            @RequestParam(value="weight", required = true) int weight
	    ) {
	        BaseEntity result = new BaseEntity();
	        List<String> imgs = JSONArray.parseArray(imgarray, String.class);
	        List<String> classes = new ArrayList<String>();
	        List<String> className = new ArrayList<String>();
	        Classification temp1 = classRepository.findById(classification);
	        if (temp1 == null) {
	        	result.setErr("-200", "分类信息有误");
	        	return result;
	        }
	        className.add(0,temp1.getName());
	        classes.add(0, temp1.getId());
	        classes.add(0, temp1.getParentId());
	        Classification temp2 = classRepository.findById(temp1.getParentId());
	        if (temp2 == null) {
	        	result.setErr("-200", "分类信息有误");
	        	return result;
	        }
	        className.add(0,temp2.getName());
	        classes.add(0, temp2.getParentId());
	        Classification temp3 = classRepository.findById(temp2.getParentId());
	        if (temp3 == null) {
	        	result.setErr("-200", "分类信息有误");
	        	return result;
	        }
	        className.add(0,temp3.getName());
	        User user = userRepository.findById(owner);
	        if (user == null || user.getShopLocation() == null) {
	        	result.setErr("-200", "查询不到商户信息or商户地址信息为空");
	        	return result;
	        }
	        Market market = marketRepository.findById(marketid);
	        if (market == null) {
	        	result.setErr("-200", "查询不到市场信息");
	        	return result;
	        }
	        Product product = new Product();
	        long time = new Date().getTime();
	        product.setProName(proname);
	        product.setAreas(market.getDivision());
	        product.setCreateTime(time);
	        product.setShopLocation(user.getShopLocation());
	        product.setClassification(classes);
	        product.setDeleted(false);
	        product.setFavNum(0);
	        product.setImgs(imgs);
	        product.setIs_off_shelve(false);
	        product.setMarketid(marketid);
	        product.setMarketName(market.getName());
	        List<String> market_imgs = market.getImgs();
	        if (market_imgs != null && !market_imgs.isEmpty() && market_imgs.size()>0) {
	        	product.setMarketPic(market_imgs.get(0));
	        }
	        product.setOldprice(0);
	        product.setPrice(price);
	        product.setOwner(owner);
	        if (owner != null && !"".equals(owner)) {
	        	User Shop = userRepository.findById(owner);
	        	if (Shop == null) {
	        		result.setErr("-200", "查询不到商户信息");
		        	return result;
	        	}
	        	List<UserCate> cates = Shop.getCate();
	        	if (cates == null) {
	        		cates = new ArrayList<UserCate>();
	        	}
	        	boolean hasCate = false;
	        	for (UserCate temp : cates) {
					if (temp != null && temp.getCate() != null && temp.getCate().size() > 0 && temp.getCate().get(temp.getCate().size()-1) == classification) {
						hasCate = true;
					}
				}
	        	if (!hasCate) {
	        		UserCate userCate = new UserCate();
	        		userCate.setCate(classes);
	        		userCate.setLastCate(classification);
	        		cates.add(userCate);
	        	}
	        	Shop.setCate(cates);
	        	mongoTemplate.save(Shop);
	        }
	        product.setClassName(className);
	        product.setOwnerName(user.getName());
	        product.setOwnerAvatar(user.getAvatar());
	        product.setStock(stock);
	        product.setWeight(weight);
	        mongoTemplate.save(product);
	        List<Product> res = mongoTemplate.find(new Query().addCriteria(Criteria.byExample(product)),Product.class);
	        if (res != null && res.size() > 0) {
	        	for (Product temp : res) {
					ProductPriceChange record = new ProductPriceChange();
					record.setChangeTime(time);
					record.setProductAmount(temp.getPrice());
					record.setProductId(temp.getId());
					record.setProductName(temp.getProName());
					record.setType(1);
					mongoTemplate.save(record);
				}
	        }
	        result.setOk();
	        return result;
	    }
	    
		@ApiOperation("修改产品")
	    @PostMapping("/update")
		@FarmAuth(validate = true)
	    public BaseEntity update(
	    		@RequestParam(value="id", required = true) String id,
	    		@RequestParam(value="classCode", required = false, defaultValue = "") String classCode,
	            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
	            @RequestParam(value="price", required = false, defaultValue = "0") long price,
	            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
	            @RequestParam(value="owner", required = false, defaultValue = "") String owner,
	            @RequestParam(value="proName", required = false, defaultValue = "") String proName,
	            @RequestParam(value="stock", required = false, defaultValue = "0") int stock,
	            @RequestParam(value="weight", required = false, defaultValue = "0") int weight,
	            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
	            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
	            @RequestParam(value="is_off_shelve", required = false, defaultValue = "0") int is_off_shelve,
	            @RequestParam(value="deleted", required = false, defaultValue = "0") int deleted
	    ) {
	    	BaseEntity result = new BaseEntity();
	    	Product product = productRepository.findById(id);
	    	Boolean delete = false;
	    	Boolean off_shelve = false;
	    	if (deleted == 1) {
	    		delete = true;
	    	} else {
	    		delete = false;
	    	}
	    	if (is_off_shelve == 1) {
	    		off_shelve = true;
	    	} else {
	    		off_shelve = false;
	    	}
	    	if (deleted != 0 && delete != product.getDeleted()) {
	    		product.setDeleted(delete);
	    		product.setDeleteTime(new Date().getTime());
	    	}
	    	if (is_off_shelve != 0 && off_shelve != product.getIs_off_shelve()) {
	    		product.setIs_off_shelve(off_shelve);
	    		product.setOffshelveTime(new Date().getTime());
	    	}
	    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
	    	if (!"".equals(classCode)) {
	    		List<String> classes = new ArrayList<String>();
	    		List<String> className = new ArrayList<String>();
		        Classification temp1 = classRepository.findById(classCode);
		        if (temp1 == null) {
		        	result.setErr("-200", "分类信息有误");
		        	return result;
		        }
		        classes.add(0, temp1.getId());
		        className.add(0,temp1.getName());
		        Classification temp2 = classRepository.findById(temp1.getParentId());
		        if (temp2 == null) {
		        	result.setErr("-200", "分类信息有误");
		        	return result;
		        }
		        classes.add(0, temp2.getId());
		        className.add(0,temp2.getName());
		        Classification temp3 = classRepository.findById(temp2.getParentId());
		        if (temp3 == null) {
		        	result.setErr("-200", "分类信息有误");
		        	return result;
		        }
		        classes.add(0, temp3.getId());
		        className.add(0,temp3.getName());
		        if (classes!=null && classes.size()>0) {
		    		product.setClassification(classes);
		    		product.setClassName(className);
		    	}
		        String userId = product.getOwner();
		        if (userId != null && !"".equals(userId)) {
		        	User Shop = userRepository.findById(userId);
		        	if (Shop == null) {
		        		result.setErr("-200", "查询不到商户信息");
			        	return result;
		        	}
		        	List<UserCate> cates = Shop.getCate();
		        	if (cates == null) {
		        		cates = new ArrayList<UserCate>();
		        	}
		        	boolean hasCate = false;
		        	for (UserCate temp : cates) {
						if (temp != null && temp.getCate() != null && temp.getCate().size() > 0 && temp.getCate().get(temp.getCate().size()-1) == classCode) {
							hasCate = true;
						}
					}
		        	if (!hasCate) {
		        		UserCate userCate = new UserCate();
		        		userCate.setCate(classes);
		        		userCate.setLastCate(classCode);
		        		cates.add(userCate);
		        	}
		        	Shop.setCate(cates);
		        	mongoTemplate.save(Shop);
		        }
	    	}
	    	if (imgs.size()>0) {
	    		product.setImgs(imgs);
	    	}
	    	if (!"".equals(marketid) && !marketid.equals(product.getMarketid())) {
	    		Market market = marketRepository.findById(marketid);
		        if (market == null) {
		        	result.setErr("-200", "查询不到市场信息");
		        	return result;
		        }
		        product.setAreas(market.getDivision());
	    		product.setMarketid(marketid);
	    		product.setMarketName(market.getName());
	    		List<String> market_imgs = market.getImgs();
		        if (market_imgs != null && !market_imgs.isEmpty() && market_imgs.size()>0) {
		        	product.setMarketPic(market_imgs.get(0));
		        }
	    	}
	    	if (!"".equals(owner) && !owner.equals(product.getOwner())) {
	    		User user = userRepository.findById(owner);
			    if (user == null || user.getShopLocation() == null) {
			    	result.setErr("-200", "查询不到商户信息or商户地址信息为空");
			    	return result;
			    }
	    		product.setOwner(owner);
		        product.setOwnerName(user.getName());
		        product.setOwnerAvatar(user.getAvatar());
	    	}
	    	if (!"".equals(proName)) {
	    		product.setProName(proName);
	    	}
	    	if (lon > 0 && lat > 0) {
	    		product.setShopLocation(new Location(lon,lat));
	    	}
	    	long time = new Date().getTime();
	    	if (price>0) {
	    		long oldprice = product.getPrice();
	    		product.setOldprice(oldprice);
	    		product.setPrice(price);
	    		//记录价格变化
	    		ProductPriceChange record = new ProductPriceChange();
				record.setChangeTime(time);
				record.setProductAmount(price);
				record.setProductId(id);
				record.setProductName(product.getProName());
				record.setType(2);
				mongoTemplate.save(record);
	    	}
	    	product.setUpdateTime(time);
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
	    @GetMapping("/get")
	    public ProductEntity get(
	    		HttpServletRequest request,
	    		@RequestParam(value="id", required = true) String id
	    ) {
	    	ProductEntity result = new ProductEntity();
	    	Product product = productRepository.findById(id);
	    	String userid = "";
	    	if (!"".equals(request.getHeader("X-USERID"))) {
				User user = userRepository.findById(request.getHeader("X-USERID"));
		        if (user != null) {
		        	userid = user.getId();
		        	Query query = new Query();
		            query.addCriteria(Criteria.where("favUserId").is(userid));
		            query.addCriteria(Criteria.where("favType").is(1));
		            query.addCriteria(Criteria.where("favId").is(id));
		            
		            favorite fav = mongoTemplate.findOne(query, favorite.class);
		        	if (fav != null) {
		        		product.setFav(1);
		        	} else {
		        		product.setFav(2);
		        	}
		        }
			}
	    	if (!"".equals(product.getOwner())) {
	    		User shop = userRepository.findById(product.getOwner());
	    		if (shop != null) {
	    			if (shop.getStatus() == 2) {
	    				product.setOwnerAvatar(shop.getShopImg());
	    			}
	    		}
	    	}
	    	if (!"".equals(product.getMarketid())) {
	    		Market market = marketRepository.findById(product.getMarketid());
	    		if (market != null) {
	    			if (!market.getImgs().isEmpty() && market.getImgs().size() > 0) {
	    				product.setMarketPic(market.getImgs().get(0));
	    			}
	    		}
	    	}
	        result.setProduct(product);
	        result.setOk();
	        return result;
	    }
	    
	    @ApiOperation("收藏/取消收藏 商品")
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
	    	Product pro = productRepository.findById(id);
	    	if (pro == null || pro.getDeleted()) {
	    		result.setErr("-200", "商品出错");   
	    		return result;
	    	} else {
	    		Query query = new Query();
		        query.addCriteria(Criteria.where("favUserId").is(userid));
		        query.addCriteria(Criteria.where("favType").is(1));
		        query.addCriteria(Criteria.where("favId").is(id));
	    		if (op == 1) {
					favorite temp = mongoTemplate.findOne(query, favorite.class);
					if (temp != null) {
						temp.setFavTime(new Date().getTime());
						temp.setShopLocation(pro.getShopLocation());						
						pro.setFavNum(pro.getFavNum()+1);	
						mongoTemplate.save(temp);
						mongoTemplate.save(pro);
					} else {
						favorite fav = new favorite();
						fav.setFavType(1);
						fav.setFavId(id);
						fav.setFavTime(new Date().getTime());
						fav.setFavUserId(userid);
						fav.setShopLocation(pro.getShopLocation());
						pro.setFavNum(pro.getFavNum()+1);
						mongoTemplate.save(fav);
						mongoTemplate.save(pro);
					}
	        	} else if (op == 2) {    	        
	    	        mongoTemplate.remove(query,
	    	        		favorite.class);
	    	        pro.setFavNum(pro.getFavNum()-1);
	    	        mongoTemplate.save(pro);
	        	}    
	    	}    
	        result.setOk();
	        return result;
	    }
	    
	    @ApiOperation("删除产品")
	    @PostMapping("/del")
		@FarmAuth(validate = true)
	    public BaseEntity del(
	    		@RequestParam(value="id", required = true) String id
	    ) {
	    	BaseEntity result = new BaseEntity();
	    	Product product = productRepository.findById(id);
	        mongoTemplate.remove(product);
	        result.setOk();
	        return result;
	    }   
		
		@ApiOperation("分页查询产品 deleted 1:查询未删除的商品 2：查询已删除的商品")
		@GetMapping("/list")
	    public ProductListEntity list(
	            HttpServletRequest request,
	            @RequestParam(value="key", required = false, defaultValue = "") String key,
	            @RequestParam(value="classCode", required = false, defaultValue = "") String classCode,
	            @RequestParam(value="division", required = false, defaultValue = "0") long division,
	            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
	            @RequestParam(value="proName", required = false, defaultValue = "") String proName,
	            @RequestParam(value="owner", required = false, defaultValue = "") String owner,
	            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
	            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
	            @RequestParam(value="max", required = false, defaultValue = "0") double max,
	            @RequestParam(value="deleted", required = false, defaultValue = "-1") int deleted,
	            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
	            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
	            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
	            @RequestParam(value="sortby", required = false, defaultValue = "shopLocation") String sortby,
	            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
	    ) {
			ProductListEntity result = new ProductListEntity();
	        List<Product> lists=new ArrayList<Product>();
	        Query query = new Query();
	        if (lon>0&&lat>0&&max>0) {
	        	query.addCriteria(Criteria.where("shopLocation").nearSphere(new Point(lon,lat)).maxDistance(max));  
	        }
	        if (!"".equals(classCode)) {
	        	query.addCriteria(Criteria.where("classification").in(classCode));  
	        }
	        if (division > 0) {
	        	query.addCriteria(Criteria.where("areas").in(division));  
	        }
	        if (!"".equals(marketid)) {
	        	query.addCriteria(Criteria.where("marketid").is(marketid));
	        }
	        if (!"".equals(owner)) {
	        	query.addCriteria(Criteria.where("owner").is(owner));
	        }
	        if (!"".equals(key)) {
	        	query.addCriteria(new Criteria().orOperator(Criteria.where("proName").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)), Criteria.where("className").in(key)));
	        } 
	        if (!"".equals(proName)) {
	        	query.addCriteria(Criteria.where("proName").regex(Pattern.compile("^.*"+proName+".*$",Pattern.CASE_INSENSITIVE)));
	        }
	        if (deleted > -1) {
	        	if (deleted == 1) {
	        		query.addCriteria(Criteria.where("is_off_shelve").is(false));
	        	} else {
	        		query.addCriteria(Criteria.where("is_off_shelve").is(true));
	        	}
	        }
	        long totalCount = mongoTemplate.count(query, Product.class);
	        result.setTotalCount(totalCount);
	        if (paged == 1) {        	
	        	long totalPage = (totalCount+pagesize-1)/pagesize;
	        	result.setTotalPage(totalPage);
	        } else {
	        	result.setTotalPage(1);
	        }
	        try {
	            if (paged == 1) {
	            	Criteria criteria= new Criteria();
	            	
	            	Sort sorts = null;
	            	if (sort == 1) {
	            		sorts = new Sort(Sort.Direction.DESC, sortby);
	            	} else {
	            		sorts = new Sort(Sort.Direction.ASC, sortby);
	            	}
	            	
	            	if (!"".equals(proName)) {
	            		criteria.and("proName").regex(Pattern.compile("^.*"+proName+".*$",Pattern.CASE_INSENSITIVE));
	            	}
	            	if (!"".equals(key)) {
	            		criteria.andOperator(new Criteria().orOperator(Criteria.where("proName").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)), Criteria.where("className").in(key)));
//	            		criteria.and("proName").regex(".*?\\" +proName+ ".*");
	            	}
	            	
	            	if (!"".equals(classCode)) {
	            		criteria.and("classification").in(classCode);
	            	}
	            	if (division > 0) {
	            		criteria.and("areas").in(division);  
	    	        }
	            	 if (deleted > -1) {
	     	        	if (deleted == 1) {
	     	        		criteria.and("is_off_shelve").is(false);
	     	        	} else {
	     	        		criteria.and("is_off_shelve").is(true);
	     	        	}
	     	        }
	    	        if (!"".equals(owner)) {
	    	        	criteria.and("owner").is(owner);
	    	        }
	    	        
	    	        if (!"".equals(marketid)) {
	    	        	criteria.and("marketid").is(marketid);
	    	        }
	    	        
	    	        NearQuery querys = NearQuery.near(new Point(lon,lat)).num(pagesize).spherical(true).distanceMultiplier(6378137).maxDistance(100/6378137);
	    	    	TypedAggregation<Product> aggregation = Aggregation.newAggregation(Product.class, 
	    	    			Aggregation.geoNear(querys, "dis"),
	    	                Aggregation.match(  
	    	                        criteria
	    	                ),                  
	    	    			Aggregation.sort(sorts), 
	    	                Aggregation.skip(pagenum>1?(pagenum-1)*pagesize:0)
	    	        );  	    	
	    	    	lists = mongoTemplate.aggregate(aggregation, Product.class).getMappedResults();
	            } else {
	            	lists = mongoTemplate.find(query, Product.class);
	            }
	            result.setOk();
	            result.setList(lists);
	        } catch (Exception e) {
	            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
	            result.setErr("-200", "00", e.getMessage());
	        }
			return result;
		}
		
		@ApiOperation("分页查询收藏的商品")
		@GetMapping("/listfav")
	    public ProductListEntity listFav(
	            HttpServletRequest request,
	            @RequestParam(value="favUserId", required = true) String favUserId,
	            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
	            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
	            @RequestParam(value="sort", required = false, defaultValue = "2") int sort,
	            @RequestParam(value="sortby", required = false, defaultValue = "favTime") String sortby,
	            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
	            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
	            @RequestParam(value="max", required = false, defaultValue = "0") double max,
	            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
	    ) {
			ProductListEntity result = new ProductListEntity();
	        List<favorite> lists=new ArrayList<favorite>();
	        List<Product> list=new ArrayList<Product>();
	        Query query = new Query();
	        query.addCriteria(Criteria.where("favUserId").is(favUserId));
	        query.addCriteria(Criteria.where("favType").is(1));
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
	                
	            } if (paged == 2) {
	            	if (lon == 0 || lat == 0) {
	            		result.setErr("-1", "定位信息有误");
	            		return result;
	            	} else {
	            		Criteria criteria= new Criteria();
		            	
		            	Sort sorts = null;
		            	if (sort == 1) {
		            		sorts = new Sort(Sort.Direction.DESC, sortby);
		            	} else {
		            		sorts = new Sort(Sort.Direction.ASC, sortby);
		            	}
		            	
		    	        if (!"".equals(favUserId)) {
		    	        	criteria.and("favUserId").is(favUserId);
		    	        }
		    	        
		    	        criteria.and("favType").is(1);
		    	        
		    	        NearQuery querys = NearQuery.near(new Point(lon,lat)).num(pagesize).spherical(true).distanceMultiplier(6378137).maxDistance(100/6378137);
		    	    	TypedAggregation<favorite> aggregation = Aggregation.newAggregation(favorite.class, 
		    	    			Aggregation.geoNear(querys, "dis"),
		    	                Aggregation.match(  
		    	                        criteria
		    	                ),                  
		    	    			Aggregation.sort(sorts), 
		    	                Aggregation.skip(pagenum>1?(pagenum-1)*pagesize:0),  
		    	                Aggregation.limit(pagesize)
		    	        );  	    	
		    	    	lists = mongoTemplate.aggregate(aggregation, favorite.class).getMappedResults();
	            	}	            	
	            } else {
	            	lists = mongoTemplate.find(query, favorite.class);
	                result.setTotalCount(lists.size());
	                result.setTotalPage(1);
	            }
	            for (favorite fav : lists) {
					if (fav != null && fav.getFavType() == 1) {
						Product temp = productRepository.findById(fav.getFavId());
						temp.setDis(fav.getDis());
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
