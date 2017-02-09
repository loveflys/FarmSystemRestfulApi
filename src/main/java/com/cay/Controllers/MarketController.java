package com.cay.Controllers;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.ParamUtils;
import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.BaseEntity;
import com.cay.Model.Market.entity.MarketEntity;
import com.cay.Model.Market.entity.MarketListEntity;
import com.cay.repository.MarketRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Market.vo.Market;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;

@Api(value = "市场服务",description="提供市场信息增删改查API")
@RestController
@RequestMapping("/market")
public class MarketController {
    private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MarketRepository marketRepository;
	
	@GetMapping("/set")
    public void save() {
		// 等同db.location.ensureIndex( {position: "2d"} )
        List<String> imgs = new ArrayList<String>();
        imgs.add("http://m.yuan.cn/content/images/200.png");
    	mongoTemplate.indexOps(Market.class).ensureIndex(new GeospatialIndex("location"));
    	Market m1 = new Market();
    	m1.setDeleted(false);
    	m1.setDescr("测试数据");
    	m1.setImgs(imgs);
    	m1.setLocationName("山东省东营市市辖区东城黄河路与东三路交叉路口西北角好宜家小商品城1212号");
		double a1 = 118.668089;
		double a2 = 37.449626;
		m1.setDivision(370501);
    	m1.setLocation(new Location(a1,a2));
    	m1.setName("好宜家小商品城");
    	
    	Market m2 = new Market();
    	m2.setLocationName("山东省东营市东营区西四路");
		double b1 = 118.495812;
		double b2 = 37.452633;
    	m2.setLocation(new Location(b1,b2));
        m2.setDeleted(false);
        m2.setDivision(370502);
        m2.setDescr("测试数据");
        m2.setImgs(imgs);
    	m2.setName("聚龙亨市场");
    	
    	Market m3 = new Market();
    	m3.setLocationName("山东省东营市广饶县其他孙武路101号");
		double c1 = 118.415699;
		double c2 = 37.0449;
    	m3.setLocation(new Location(c1,c2));
        m3.setDeleted(false);
        m3.setDescr("测试数据");
        m3.setDivision(370523);
        m3.setImgs(imgs);
    	m3.setName("广饶城南批发市场");
    	
    	Market m4 = new Market();
    	m4.setLocationName("山东省东营市东营区云门山路");
		double d1 = 118.486723;
		double d2 = 37.465366;
        m4.setDeleted(false);
        m4.setDivision(370502);
        m4.setDescr("测试数据");
        m4.setImgs(imgs);
    	m4.setLocation(new Location(d1,d2));
    	m4.setName("东营市水产市场");
        // 初始化数据
    	mongoTemplate.save(m1);
    	mongoTemplate.save(m2);
    	mongoTemplate.save(m3);
    	mongoTemplate.save(m4);
    }
    
	@ApiOperation("新增市场")
    @PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="imgs", required = true) String imgarray,
            @RequestParam(value="descr", required = false, defaultValue = "无") String descr,
            @RequestParam(value="locationName", required = false, defaultValue = "") String locationName,
            @RequestParam(value="division", required = true) long division,
            @RequestParam(value="lon", required = true) double lon,
            @RequestParam(value="lat", required = true) double lat,
            @RequestParam(value="name", required = true) String name
    ) {
        BaseEntity result = new BaseEntity();
        Market market = new Market();
        List<String> imgs = JSONArray.parseArray(imgarray, String.class);
        market.setDeleted(false);
        market.setDescr(descr);
        market.setImgs(imgs);
        market.setDivision(division);
        market.setLocationName(name);
        double a1 = lon;
        double a2 = lat;
        market.setLocation(new Location(a1,a2));
        market.setName(name);
        mongoTemplate.save(market);
        result.setOk();
        return result;
    }
    
    @ApiOperation("修改市场")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="descr", required = false, defaultValue = "") String descr,
            @RequestParam(value="locationName", required = false, defaultValue = "") String locationName,
            @RequestParam(value="division", required = false, defaultValue = "0") long division,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="name", required = false, defaultValue = "") String name
    ) {
    	BaseEntity result = new BaseEntity();
    	Market market = marketRepository.findById(id);
    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
    	if (imgs.size()>0) {
    		market.setImgs(imgs);
    	}
    	if (!"".equals(descr)) {
    		market.setDescr(descr);
    	}
        if (!"".equals(locationName)) {
        	market.setLocationName(locationName);
        }
        if (division > 0) {
        	market.setDivision(division);
        }
        if (lon > 0 && lat > 0) {
        	market.setLocation(new Location(lon,lat));
        }
        if (!"".equals(name)) {
        	market.setName(name);
        }
        mongoTemplate.save(market);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取市场详情")
    @GetMapping("/get/{id}")
    public MarketEntity get(
    		@PathVariable(value="id", required = true) String id
    ) {
    	MarketEntity result = new MarketEntity();
    	Market market = marketRepository.findById(id);
    	result.setMarket(market);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除市场")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Market market = marketRepository.findById(id);
        mongoTemplate.remove(market);
        result.setOk();
        return result;
    }    
        
    @ApiOperation("根据经纬度，距离分页查询市场")
    @GetMapping("/list")
	public MarketListEntity list(
            HttpServletRequest request,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="division", required = false, defaultValue = "0") long division,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="max", required = false, defaultValue = "0") double max,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "location") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
        MarketListEntity result = new MarketListEntity();
        List<Market> lists=new ArrayList<Market>();
        Query query = new Query();
        if (lon>0&&lat>0&&max>0) {
        	query.addCriteria(Criteria.where("location").near(new Point(lon,lat)).maxDistance(max));  
        }
        if (name!=null && name.length()>0) {
        	query.addCriteria(Criteria.where("name").regex(".*?\\" +name+ ".*"));
        } 
        try {
            if (paged == 1) {
                //构建分页信息
                PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                long totalCount = mongoTemplate.count(query, Market.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                        Market.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
            } else {
            	lists = mongoTemplate.find(query,
                        Market.class);
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
