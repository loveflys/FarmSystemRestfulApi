package com.cay.Controllers;

import java.util.*;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Market.entity.MarketListEntity;
import com.cay.Model.Users.vo.User;
import com.cay.repository.MarketRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
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
    	m1.setLocationName("地址1");
		double a1 = 120.420458;
		double a2 = 36.162769;
    	m1.setLocation(new Location(a1,a2));
    	m1.setName("双玉新苑市场");
    	
    	Market m2 = new Market();
    	m2.setLocationName("地址2");
		double b1 = 120.435361;
		double b2 = 36.178356;
    	m2.setLocation(new Location(b1,b2));
        m2.setDeleted(false);
        m2.setDescr("测试数据");
        m2.setImgs(imgs);
    	m2.setName("1688市场");
    	
    	Market m3 = new Market();
    	m3.setLocationName("地址3");
		double c1 = 118.644777;
		double c2 = 35.79344;
    	m3.setLocation(new Location(c1,c2));
        m3.setDeleted(false);
        m3.setDescr("测试数据");
        m3.setImgs(imgs);
    	m3.setName("沂水市场");
    	
    	Market m4 = new Market();
    	m4.setLocationName("地址4");
		double d1 = 118.697535;
		double d2 = 37.468503;
        m4.setDeleted(false);
        m4.setDescr("测试数据");
        m4.setImgs(imgs);
    	m4.setLocation(new Location(d1,d2));
    	m4.setName("东营市场");
        // 初始化数据
    	mongoTemplate.save(m1);
    	mongoTemplate.save(m2);
    	mongoTemplate.save(m3);
    	mongoTemplate.save(m4);
    }
    @ApiOperation("新增市场")
    @PostMapping("/add")
    public BaseEntity add(
            @RequestParam(value="imgs", required = true) List<String> imgs,
            @RequestParam(value="descr", required = false, defaultValue = "无") String descr,
            @RequestParam(value="locationName", required = false, defaultValue = "") String locationName,
            @RequestParam(value="lon", required = true) double lon,
            @RequestParam(value="lat", required = true) double lat,
            @RequestParam(value="name", required = true) String name
    ) {
        BaseEntity result = new BaseEntity();
        Market market = new Market();
        market.setDeleted(false);
        market.setDescr(descr);
        market.setImgs(imgs);
        market.setLocationName(name);
        double a1 = lon;
        double a2 = lat;
        market.setLocation(new Location(a1,a2));
        market.setName(name);
        mongoTemplate.save(market);
        result.setOk();
        return result;
    }
    @ApiOperation("根据经纬度，距离分页查询市场")
	@GetMapping("/get")
	public MarketListEntity get(
            HttpServletRequest request,
            @RequestParam(value="lon", required = true) double lon,
            @RequestParam(value="lat", required = true) double lat,
            @RequestParam(value="max", required = true) double max,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "") String sort,
            @RequestParam(value="sortby", required = false, defaultValue = "") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
        MarketListEntity result = new MarketListEntity();
        List<Market> lists=new ArrayList<Market>();
        try {
            if (paged == 1) {
                //构建分页信息
                PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                Query query = new Query();
                long totalCount = mongoTemplate.count(query, User.class);
                //查询指定分页的内容
                Iterator<Market> market =  marketRepository.findByLocationNear(new Point(lon,lat),new Distance(max),pageRequest).iterator();
                while(market.hasNext()){
                    lists.add(market.next());
                }
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
            } else {
                lists = mongoTemplate.findAll(Market.class);
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
