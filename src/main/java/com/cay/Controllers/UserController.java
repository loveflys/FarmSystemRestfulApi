package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.Config.RedisConfig;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.AESHelper;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.entity.ClassListEntity;
import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Market.vo.Market;
import com.cay.Model.Users.entity.LoginEntity;
import com.cay.Model.Users.entity.UserEntity;
import com.cay.Model.Users.entity.UserListEntity;
import com.cay.Model.Users.vo.LoginRecord;
import com.cay.Model.Users.vo.User;
import com.cay.Model.Users.vo.UserCate;
import com.cay.repository.ClassRepository;
import com.cay.repository.MarketRepository;
import com.cay.repository.UserRepository;
import com.cay.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Api(value = "用户服务",description="提供用户信息增删改查API")
@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	private RedisConfig redisConfig;

	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClassRepository classRepository;
	@Autowired
	private MarketRepository marketRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private com.cay.Model.Config.AESConfig aes;

	@ApiOperation("注册")
    @PostMapping("/register")
    public LoginEntity Register(
    		HttpServletRequest request, 
    		@RequestParam("lon") double lon, 
    		@RequestParam("lat") double lat,
    		@RequestParam("ciphertext") String ciphertext, 
    		@RequestParam("code") String verifyCode,
    		@RequestParam("type") int type,
    		@RequestParam("name") String name) throws Exception {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), redisConfig.getIp());
		Jedis jedis = pool.getResource();
		LoginEntity result = new LoginEntity();
		String deviceId = request.getHeader("X-DEVICEID");
		HttpSession session = request.getSession();
		String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String pwd = param[0];
		String phone = param[1];
		String sessionCode = (String) session.getAttribute("verifyCode");
		if (verifyCode.equals(sessionCode)) {//redis.opsForValue().get("verifyCode_"+phone)) {
			User user = new User();
			user.setPhone(phone);
			user.setType(type);
			user.setDeviceId(deviceId);
			user.setPassword(pwd);
			user.setCreateTime(System.currentTimeMillis());
			user.setPushsetting(1);
//			Location local = ParamUtils.deserialize(location, Location.class);
			LoginRecord record = new LoginRecord();
			String token = ParamUtils.generateString(32);
			record.setDeviceId(deviceId);
			record.setOperate(1);
			record.setOp_time(System.currentTimeMillis());
			record.setPhone(phone);
			record.setLocation(new Location(lon,lat));
			//token之后需要改为验证有效期
			record.setToken(token);
			result.setToken(token);
			
			if (type == 1) {
				user.setName("用户"+ParamUtils.generateNumber(6));
				user.setStatus(2);
				record.setLogin_identity(1);
			} else {
				user.setRealName(name);		
				user.setStatus(0);
				record.setLogin_identity(2);
			}
			mongoTemplate.save(user);
			User temp = userService.findByPhone(phone);
			mongoTemplate.save(record);
			if(temp != null) {
				//设置用户token存储到redis中
				jedis.setex("token_"+temp.getId(), 60*60*24*3, token);//通过此方法，可以指定key的存活（有效时间） 时间为秒
				result.setUserid(temp.getId());
				result.setOk();
			} else {
				result.setErr("-101", "服务器错误");
			}
		} else {
			result.setErr("-100", "验证码输入错误");
		}
        return result;
    }
	
	@GetMapping("/setIndex")
    public void setIndex() {
    	mongoTemplate.indexOps(User.class).ensureIndex(new GeospatialIndex("shopLocation").typed(GeoSpatialIndexType.GEO_2DSPHERE));
    }
	
	@ApiOperation("完善资料")
    @PostMapping("/complate")
    public LoginEntity Complate(
    		HttpServletRequest request, 
    		@RequestParam("phone") String phone, 
    		@RequestParam("lon") double lon, 
    		@RequestParam("lat") double lat,
    		@RequestParam("identityImg") String identityImg,
    		@RequestParam("name") String name,
    		@RequestParam("shopImg") String shopImg,
    		@RequestParam("marketid") String marketid) throws Exception {
		LoginEntity result = new LoginEntity();
		User user = userService.findByPhone(phone);
		user.setName(name);		
		user.setShopLocation(new Location(lon,lat));
		Market market = marketRepository.findById(marketid);
		if (market == null) {
			result.setErr("200", "市场信息错误");
		}
		user.setAreas(market.getDivision());
		user.setMarketid(marketid);				
		user.setIdentityImg(identityImg);	
		user.setShopImg(shopImg);
		user.setStatus(1);
		user.setUpdateTime(System.currentTimeMillis());
		userService.save(user);
		result.setOk();
        return result;
    }
		
	@ApiOperation("登录")
    @PostMapping("/login")
    public LoginEntity Login(
			HttpServletRequest request,
			@RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
			@RequestParam("ciphertext") String ciphertext) {
		LoginEntity result = new LoginEntity();

		String deviceId = request.getHeader("X-DEVICEID");
		String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String pwd = param[0];
		String phone = param[1];
		System.out.println(pwd+"||"+phone);
		JedisPool pool = new JedisPool(new JedisPoolConfig(), redisConfig.getIp());
		Jedis jedis = pool.getResource();

		User user = userService.findByPhone(phone);
		if (user == null) {
			result.setErr("-200", "用户不存在");
		} else {	
			if (pwd.equals(user.getPassword())) {
				if (user.getType() == 1 && user.getDisabled() == 1) {
					result.setErr("-202", "当前用户已被禁用");
					return result;
				}
				if (user.getType() == 2 && user.getStatus() != 2) {
					if (user.getStatus() == 1) {
						result.setErr("-203", "请耐心等待审核。");
					} else if (user.getStatus() == 3) {
						result.setErr("-204", "您的审核未通过。拒绝原因:"+user.getReason()+",请重新提交审核。");
					} else if (user.getStatus() == 0) {
						result.setErr("-202", "请先进行审核。");
					}
					return result;
				}
				if (!deviceId.equals(user.getDeviceId())) {
					user.setDeviceId(deviceId);
					mongoTemplate.save(user);
				}
				LoginRecord record = new LoginRecord();
				String token = ParamUtils.generateString(32);
				record.setDeviceId(deviceId);
				record.setOperate(2);
				record.setLogin_identity(user.getType());
				record.setOp_time(System.currentTimeMillis());
				record.setPhone(phone);
				if (lon > 0 && lat > 0) {
					record.setLocation(new Location(lon,lat));
				}
				//token之后需要改为验证有效期
				record.setToken(token);
				result.setToken(token);
				System.out.println("登陆返回token===>>>"+token);
				result.setUserid(user.getId());
				result.setAvatar(user.getAvatar());
				result.setName(user.getName());
				mongoTemplate.save(record);
				//设置用户token存储到redis中
				jedis.setex("token_"+user.getId(), 60*60*24*3, token);//通过此方法，可以指定key的存活（有效时间） 时间为秒
				result.setOk();
			} else {
				System.out.println("密码是==>"+user.getPassword());
				result.setErr("-201", "用户名/密码错误");
			}			
		}
        return result;
    }
	
	@ApiOperation("新增用户")
    @PostMapping("/add")
	@FarmAuth(validate = true)
    public BaseEntity add(
    		@RequestParam(value="ciphertext", required = true) String ciphertext,
    		@RequestParam(value="type", required = false, defaultValue = "1") int type,
    		@RequestParam(value="deviceId", required = false, defaultValue = "") String deviceId,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="address", required = false, defaultValue = "") String address,
            @RequestParam(value="sex", required = false, defaultValue = "1") int sex,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,
            @RequestParam(value="identityImg", required = false, defaultValue = "") String identityImg,
            @RequestParam(value="shopImg", required = false, defaultValue = "") String shopImg,            
            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="isdelete", required = false, defaultValue = "0") int isdelete,
            @RequestParam(value="disabled", required = false, defaultValue = "0") int disabled,
            @RequestParam(value="pushsetting", required = false, defaultValue = "10") int pushsetting
    ) {
    	BaseEntity result = new BaseEntity();
        String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String pwd = param[0];
		String phone = param[1];
		
		User user = new User();
		user.setPhone(phone);
		user.setType(type);
		user.setSex(sex);
		user.setAddress(address);
		user.setAvatar(avatar);
		user.setPassword(pwd);
		user.setCreateTime(System.currentTimeMillis());
		user.setPushsetting(1);
//		Location local = ParamUtils.deserialize(location, Location.class);
		LoginRecord record = new LoginRecord();
		String token = ParamUtils.generateString(32);
		record.setDeviceId(deviceId);
		record.setOperate(1);
		record.setOp_time(System.currentTimeMillis());
		record.setPhone(phone);
		record.setLocation(new Location(lon,lat));
		//token之后需要改为验证有效期
		record.setToken(token);
		if (name != null && !"".equals(name)) {
			user.setName(name);
		} else {
			user.setName("用户"+ParamUtils.generateNumber(6));
		}
		user.setRealName(realName);
		if (type == 1) {			
			user.setStatus(2);
			record.setLogin_identity(1);
		} else {
			if (lon>0&&lat>0) {
				user.setShopLocation(new Location(lon,lat));
			} else {
				result.setErr("-200", "商户位置未上传");
			}
			if (identityImg != null && !"".equals(identityImg)) {
				user.setIdentityImg(identityImg);
			} else {
				result.setErr("-200", "证件照未上传");
			}
			if (shopImg != null && !"".equals(shopImg)) {
				user.setShopImg(shopImg);
			} else {
				result.setErr("-200", "店铺照未上传");
			}
			if (marketid != null && !"".equals(marketid)) {
				Market market = marketRepository.findById(marketid);
				if (market == null) {
					result.setErr("200", "市场信息有误");
				}
				user.setAreas(market.getDivision());
				user.setMarketid(marketid);
			} else {
				result.setErr("-200", "所属市场未上传");
			}
			user.setStatus(1);
			record.setLogin_identity(2);
		}
		mongoTemplate.save(user);
		mongoTemplate.save(record);
        
        result.setOk();
        return result;
    }
    
	@ApiOperation("修改用户")
    @PostMapping("/update")
	@FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="ciphertext", required = false, defaultValue = "") String ciphertext,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="sex", required = false, defaultValue = "-1") int sex,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="address", required = false, defaultValue = "") String address,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,
            @RequestParam(value="reason", required = false, defaultValue = "") String reason,
            @RequestParam(value="identityImg", required = false, defaultValue = "") String identityImg,
            @RequestParam(value="shopImg", required = false, defaultValue = "") String shopImg,            
            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="cates", required = false, defaultValue = "[]") String cates,
            @RequestParam(value="isdelete", required = false, defaultValue = "0") int isdelete,
            @RequestParam(value="disabled", required = false, defaultValue = "0") int disabled,
            @RequestParam(value="pushsetting", required = false, defaultValue = "10") int pushsetting
    ) {
    	BaseEntity result = new BaseEntity();
    	String cipher = "";
    	String phone = "";
    	String pwd = "";
    	List<UserCate> cate = JSON.parseArray(cates, UserCate.class);
    	
    	
    	if (!"".equals(ciphertext)) {
	    	cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());	    	
			String[] param = cipher.split("\\*");
			pwd = param[0];
			phone = param[1];		
    	}
    	User user = userRepository.findById(id);
    	if (!"".equals(pwd)) {
    		user.setPassword(pwd);
    	}
    	if (cate != null && cate.size() > 0) {
    		user.setCate(cate);
    	}
    	if (!"".equals(name)) {
    		user.setName(name);
    	}
    	if (!"".equals(realName)) {
    		user.setRealName(realName);
    	}
    	if (!"".equals(address)) {
    		user.setAddress(address);
    	}
    	if (!"".equals(phone)&&!user.getPhone().equals(phone)) {
    		User temp = userRepository.findByPhone(phone);
    		if (temp!= null) {
    			result.setErr("-200", "该手机号码已注册用户");
    			return result;
    		}
    	}
    	if (!"".equals(avatar)) {
    		user.setAvatar(avatar);
    	}    	

        if (type != user.getType()&&type>0) {
        	user.setType(type);
        }
    	
    	if (user.getType() == 2 && user.getStatus() != 2 && ("".equals(identityImg) || "".equals(shopImg) || "".equals(shopImg) || "".equals(marketid) || lon == 0 || lat == 0)) {
    		//商户身份且状态不为审核通过时，必须同时上传证件照店铺照片所属市场和所在地经纬度
    		result.setErr("-200", "商户用户信息不完整或未审核");
			return result;
    	} else {
    		if (!"".equals(identityImg)) {
        		user.setIdentityImg(identityImg);
        	}
        	if (!"".equals(shopImg)) {
        		user.setShopImg(shopImg);
        	}
        	if (!"".equals(marketid)) {
        		Market market = marketRepository.findById(marketid);
				if (market == null) {
					result.setErr("200", "市场信息有误");					
				}
				user.setAreas(market.getDivision());
        		user.setMarketid(marketid);
        	}
        	if (lon > 0 && lat > 0) {
            	user.setShopLocation(new Location(lon,lat));
            }
        	
        	if (user.getType() == 2 && user.getStatus() != 2) {
        		//商户身份且状态不为审核通过时，设置状态为待审核（已提交资料）;
        		user.setStatus(1);
        	}
    	}
        
        if (sex > 0) {
        	user.setSex(sex);
        }
        if (isdelete > 0) {
        	user.setIsdelete(isdelete);
        }
        if (sex != user.getSex()) {
        	user.setSex(sex);;
        }
        if (disabled != user.getDisabled()) {
        	user.setDisabled(disabled);
        }
        
        if (pushsetting != 10 && pushsetting != user.getPushsetting()) {
        	user.setPushsetting(pushsetting);
        }

    	user.setUpdateTime(new Date().getTime());
        mongoTemplate.save(user);
        result.setOk();
        return result;
    }    
	
	@ApiOperation("重设密码")
    @PutMapping("/resetpwd")
    public UserEntity resetpwd(
    		HttpServletRequest request,
    		@RequestParam(value="oldpwd", required = true) String oldpwd,
    		@RequestParam(value="newpwd", required = true) String newpwd
    ) {
		UserEntity result = new UserEntity();
    	if (!"".equals(request.getHeader("X-USERID"))) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user != null && (AESHelper.decrypt(user.getPassword().getBytes(), aes.getKey(), aes.getIv()).equals(oldpwd))) {
	        	try {
	        		user.setPassword(AESHelper.encrypt(newpwd.getBytes(), aes.getKey(), aes.getIv()));
	    			user.setUpdateTime(new Date().getTime());
	    			mongoTemplate.save(user);
	    			result.setUser(user);
	    			result.setOk();
	    			return result;
	    		} catch (Exception e) {
	    			result.setErr("-200", e.getMessage());	
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}	
	        } else {
	        	if (user == null) {
		        	result.setErr("-201", "查询不到用户信息");	
	        	} else {
	        		result.setErr("-202", "原密码输入错误");
	        	}
			}
		} else {
			result.setErr("-200", "请登录后再试");	
		}
        return result;
    }    
    
	@ApiOperation("审核商户")
    @PostMapping("/check")
	@FarmAuth(validate = true)
	public BaseEntity check(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="reason", required = false, defaultValue = "") String reason,
    		@RequestParam(value="status", required = false, defaultValue = "true") boolean status
    ) {
    	BaseEntity result = new BaseEntity();
    	User user = userRepository.findById(id);
    	
    	if (user.getDisabled() == 1) {
    		result.setErr("-200", "商户已被封禁");
			return result;
    	}
    	if (user.getIsdelete() == 1) {
    		result.setErr("-200", "商户已被删除");
			return result;
    	}
    	
    	if (user.getStatus() == 2) {
    		result.setErr("-200", "已审核通过，请勿重复审核。");
			return result;
    	}
    	
    	if (user.getStatus() == 0 || "".equals(user.getIdentityImg()) || user.getShopLocation()==null || "".equals(user.getShopImg()) || "".equals(user.getMarketid())) {
    		result.setErr("-200", "认证信息不完整");
			return result;
    	}
    	
    	if (user.getType() == 2) {
    		if (user.getStatus() == 1 && !status) {
    			//审核不通过
    			if (reason == null || "".equals(reason)) {
    				result.setErr("-200", "拒绝理由不能为空");
    				return result;
    			} else {
    				user.setStatus(3);
    				user.setReason(reason);
    			}
    		} else {
    			user.setStatus(2);
    		}
    	}
    	user.setUpdateTime(new Date().getTime());
        mongoTemplate.save(user);
        result.setOk();
        return result;
    }    
    
	
    @ApiOperation("获取用户")
    @GetMapping("/get")
    public UserEntity get(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = false, defaultValue="") String id
    		
    ) {
    	UserEntity result = new UserEntity();
    	if ("".equals(id)) {
    		if (!"".equals(request.getHeader("X-USERID"))) {
    			id = request.getHeader("X-USERID");
    			User user = userRepository.findById(id);
    			if (user!=null&&user.getType() == 2) {
    				Market market = marketRepository.findById(user.getMarketid());
    				if (market != null) {
    					user.setMarketName(market.getName());
    					if (market.getImgs()!=null && market.getImgs().size()>0) {
    						user.setMarketPic(market.getImgs().get(0));
    					}
    				} else {
    					result.setErr("-201", "查询市场信息失败");
    				}
    			}
    	        result.setUser(user);
    	        result.setOk();
    		} else {
    			result.setErr("-200", "请先登录后再试");
    		}
    	} else {
    		User user = userRepository.findById(id);
	        result.setUser(user);
	        result.setOk();
    	}    	
        return result;
    }
    
    @ApiOperation("获取用户所选分类")
    @GetMapping("/getcates")
	@FarmAuth(validate = true)
    public ClassListEntity getShopCates(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = false, defaultValue="") String id
    		
    ) {
    	ClassListEntity result = new ClassListEntity();
    	List<Classification> list = new ArrayList<Classification>();
    	User user = null;
    	if ("".equals(id)) {
    		if (!"".equals(request.getHeader("X-USERID"))) {
    			id = request.getHeader("X-USERID");
    			user = userRepository.findById(id);
    			
    		} else {
    			result.setErr("-200", "请先登录后再试");
    		}
    	} else {
    		user = userRepository.findById(id);
    	}    
    	List<UserCate> cates = user.getCate();
		if (cates!=null && cates.size()>0) {
			for (UserCate cate : cates) {
				if (cate!= null && cate.getCate()!=null && cate.getCate().size()>0) {
					System.out.println(cate.getCate().get(cate.getCate().size()-1));
					Classification temp = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(cate.getCate().get(cate.getCate().size()-1))), Classification.class);
					if (temp!=null) {
						boolean isHas = false;
						for (Classification c : list) {
							if (c.getId().equals(temp.getId())) {
								isHas = true;
							}
						}
						if (!isHas) {
							list.add(temp);
						}
					} else {
						result.setErr("-200", "没有这条数据");
					}
				}
			}
		} 
        result.setList(list);;
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除用户")
    @PostMapping("/del")
	@FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	User user = userRepository.findById(id);
        mongoTemplate.remove(user);
        result.setOk();
        return result;
    }  
    
    @ApiOperation("添加用户分类")
    @PostMapping("/addcate")
	@FarmAuth(validate = true)
    public BaseEntity addcate(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="cate", required = true) String cate
    ) {
    	BaseEntity result = new BaseEntity();
    	List<String> temps = JSONArray.parseArray(cate, String.class);
    	List<String> catelist = new ArrayList<String>();
    	for (int i = 0; i < temps.size(); i++) {
    		if (!"".equals(temps.get(i))) {
				catelist.add(temps.get(i));
			}
		}
    	User user = userRepository.findById(id);
        if (user!= null) {
        	List<UserCate> cates = user.getCate();
        	if (cates == null || cates.size() <= 0) {
        		cates = new ArrayList<UserCate>();
        	}
        	for (UserCate userCate : cates) {		
        		System.out.println(userCate.getCate().size()+"||"+catelist.size());
        		if (userCate.getCate() != null && userCate.getCate().size() > 0 &&userCate.getCate().size() == catelist.size() && userCate.getCate().get(catelist.size()-1).equals(catelist.get(catelist.size()-1))) {
        			result.setErr("-200", "已有此分类");
        			return result;
        		}
			}
        	UserCate temp = new UserCate();
        	temp.setCate(catelist);
        	temp.setLastCate(catelist.get(catelist.size()-1));
        	cates.add(temp);
			user.setCate(cates);
			mongoTemplate.save(user);
			result.setOk();
        } else {
        	result.setErr("-200", "用户错误");
        }
        return result;
    }    
    
    @ApiOperation("删除用户分类")
    @DeleteMapping("/delcate")
	@FarmAuth(validate = true)
    public BaseEntity delcate(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="cate", required = true) String cate
    ) {
    	BaseEntity result = new BaseEntity();
    	User user = userRepository.findById(id);
        if (user!= null) {
        	List<UserCate> cates = user.getCate();
        	if (cates!=null && cates.size()>0) {
        		for (UserCate userCate : cates) {
					if (userCate != null && userCate.getCate() != null && userCate.getCate().size() > 0 && userCate.getCate().get(userCate.getCate().size()-1).equals(cate)) {
						cates.remove(userCate);
						user.setCate(cates);
						mongoTemplate.save(user);
	        			result.setOk();
	        			return result;
					}
				}
        	} else {
        		result.setErr("-200", "用户无分类");
        	}
        } else {
        	result.setErr("-200", "用户错误");
        }
        result.setErr("-200", "用户无此分类，无法删除");
        return result;
    }    
    
    @ApiOperation("退出登录")
    @PostMapping("/logout")
	@FarmAuth(validate = true)
    public BaseEntity logout(HttpServletRequest request) {
    	BaseEntity result = new BaseEntity();
    	String userId = request.getHeader("X-USERID");
    	JedisPool pool = new JedisPool(new JedisPoolConfig(), redisConfig.getIp());
		Jedis jedis = pool.getResource();		
		jedis.del("token_"+userId);
        result.setOk();
        return result;
    }   
    
    @ApiOperation("分页查询用户")
    @GetMapping("/list")	
    @FarmAuth(validate = true)
	public UserListEntity list(
            HttpServletRequest request,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="division", required = false, defaultValue = "0") long division,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="startDate", required = false, defaultValue = "0") long startDate,
            @RequestParam(value="endDate", required = false, defaultValue = "0") long endDate,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="max", required = false, defaultValue = "0") double max,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	UserListEntity result = new UserListEntity();
        List<User> lists = new ArrayList<User>();
        
        Query query = new Query();
        if (!"".equals(name)) {
        	query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (!"".equals(realName)) {
        	query.addCriteria(Criteria.where("realName").regex(Pattern.compile("^.*"+realName+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(new Criteria().orOperator(Criteria.where("name").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)),Criteria.where("realName").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE)),Criteria.where("phone").regex(Pattern.compile("^.*"+key+".*$",Pattern.CASE_INSENSITIVE))));
        }
        if (!"".equals(phone)) {
        	query.addCriteria(Criteria.where("phone").regex(Pattern.compile("^.*"+phone+".*$",Pattern.CASE_INSENSITIVE)));
        }
        if (startDate>0) {
        	if (endDate>0) {
        		query.addCriteria(Criteria.where("createTime").gte(startDate).lte(endDate));
        	} else {
        		query.addCriteria(Criteria.where("createTime").gte(startDate));
        	}
        } else {
        	if (endDate>0) {
        		query.addCriteria(Criteria.where("createTime").lte(endDate));
        	}
        }
        if (division>0) {
        	query.addCriteria(Criteria.where("areas").in(division));  
        }
        if (type>0) {
        	query.addCriteria(Criteria.where("type").is(type));  
        }
        if (status>-1) {
        	query.addCriteria(Criteria.where("status").is(status));  
        }
        if (type == 2 && lon > 0 && lat > 0 && max > 0) {
        	query.addCriteria(Criteria.where("shopLocation").nearSphere(new Point(lon,lat)).maxDistance(max));  
        }
        long totalCount = mongoTemplate.count(query, User.class);
        result.setTotalCount(totalCount);
        if (paged == 1) {        	
        	long totalPage = (totalCount+pagesize-1)/pagesize;
        	result.setTotalPage(totalPage);
        } else {
        	result.setTotalPage(1);
        }
        
        
        if (type == 2 && lon > 0 && lat > 0 && max > 0) {
        	Criteria criteria= new Criteria();
        	
        	Sort sorts = null;
        	if (sort == 1) {
        		sorts = new Sort(Sort.Direction.DESC, sortby);
        	} else {
        		sorts = new Sort(Sort.Direction.ASC, sortby);
        	}
        	
        	if (!"".equals(name)) {
        		criteria.and("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE));
        	}
        	
        	if (!"".equals(realName)) {
        		criteria.and("realName").regex(Pattern.compile("^.*"+realName+".*$",Pattern.CASE_INSENSITIVE));
	        } 
	        if (!"".equals(phone)) {
	        	criteria.and("phone").regex(Pattern.compile("^.*"+phone+".*$",Pattern.CASE_INSENSITIVE));
	        }
	        
	        if (status>-1) {
	        	criteria.and("status").is(status);
	        }
	        
	        NearQuery querys = NearQuery.near(new Point(lon,lat)).num(10).spherical(true).distanceMultiplier(6378137).maxDistance(100/6378137);
	    	TypedAggregation<User> aggregation = Aggregation.newAggregation(User.class, 
	    			Aggregation.geoNear(querys, "dis"),
	                Aggregation.match(  
	                        criteria
	                ),                  
	    			Aggregation.sort(sorts), 
	                Aggregation.skip(pagenum>1?(pagenum-1)*pagesize:0),  
	                Aggregation.limit(pagesize)
	        );  	    	
	    	List<User> list = mongoTemplate.aggregate(aggregation, User.class).getMappedResults();
	    	for (User user : list) {
	    		if (user!=null&&user.getType() == 2) {
    				Market market = marketRepository.findById(user.getMarketid());
    				if (market != null) {
    					user.setMarketName(market.getName());
    					if (market.getImgs()!=null && market.getImgs().size()>0) {
    						user.setMarketPic(market.getImgs().get(0));
    					}
    				} else {
    					result.setErr("-201", "查询市场信息失败");
    				}
    			}
			}
	    	result.setOk();
	    	result.setUsers(list);        	
        } else {	        
	        try {
	            if (paged == 1) {
	            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
	                //查询指定分页的内容
	                lists = mongoTemplate.find(query.with(pageRequest),
	                		User.class);	                
	            } else {
	            	lists = mongoTemplate.find(query, User.class);
	            }
	            for (User user : lists) {
		    		if (user!=null&&user.getType() == 2) {
	    				Market market = marketRepository.findById(user.getMarketid());
	    				if (market != null) {
	    					user.setMarketName(market.getName());
	    					if (market.getImgs()!=null && market.getImgs().size()>0) {
	    						user.setMarketPic(market.getImgs().get(0));
	    					}
	    				} else {
	    					result.setErr("-201", "查询市场信息失败");
	    				}
	    			}
				}
	            result.setOk();
	            result.setUsers(lists);
	        } catch (Exception e) {
	            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
	            result.setErr("-200", "00", e.getMessage());
	        }
        }
		return result;
	}
    
    
    
    @ApiOperation("分页查询商户")
    @GetMapping("/shoplist")	
	public UserListEntity shoplist(
            HttpServletRequest request,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="cate", required = false, defaultValue = "") String cates,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="max", required = false, defaultValue = "0") double max,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "200") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	UserListEntity result = new UserListEntity();        
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(2));
        if (!"".equals(name)) {
        	query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (!"".equals(marketid)) {
        	query.addCriteria(Criteria.where("marketid").is(marketid));
        } 
        if (!"".equals(realName)) {
        	query.addCriteria(Criteria.where("realName").regex(Pattern.compile("^.*"+realName+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (!"".equals(phone)) {
        	query.addCriteria(Criteria.where("phone").regex(Pattern.compile("^.*"+phone+".*$",Pattern.CASE_INSENSITIVE)));
        } 
        if (status>-1) {
        	query.addCriteria(Criteria.where("status").is(status));  
        }
        if (!"".equals(cates)) {
        	query.addCriteria(Criteria.where("cates.cate").in(cates));  
        }
        if (lon > 0 && lat > 0 && max > 0) {
        	query.addCriteria(Criteria.where("shopLocation").nearSphere(new Point(lon,lat)).maxDistance(max));  
        }
        long totalCount = mongoTemplate.count(query, User.class);
        result.setTotalCount(totalCount);
        if (paged == 1) {        	
        	long totalPage = (totalCount+pagesize-1)/pagesize;
        	result.setTotalPage(totalPage);
        } else {
        	result.setTotalPage(1);
        }
        
        
        if (lon > 0 && lat > 0 && max > 0) {
        	Criteria criteria= new Criteria();
        	criteria.and("type").is(2);
        	Sort sorts = null;
        	if (sort == 1) {
        		sorts = new Sort(Sort.Direction.DESC, sortby);
        	} else {
        		sorts = new Sort(Sort.Direction.ASC, sortby);
        	}
        	
        	if (!"".equals(name)) {
        		criteria.and("name").regex(Pattern.compile("^.*"+name+".*$",Pattern.CASE_INSENSITIVE));
        	}
        	
        	if (!"".equals(realName)) {
        		criteria.and("realName").regex(Pattern.compile("^.*"+realName+".*$",Pattern.CASE_INSENSITIVE));
	        } 
        	if (!"".equals(marketid)) {
        		criteria.and("marketid").is(marketid);
	        } 
	        if (!"".equals(phone)) {
	        	criteria.and("phone").regex(Pattern.compile("^.*"+phone+".*$",Pattern.CASE_INSENSITIVE));
	        }
	        
	        if (status>-1) {
	        	criteria.and("status").is(status);
	        }
	        if (!"".equals(cates)) {
	        	criteria.and("cates.cate").in(cates);
	        }
	        NearQuery querys = NearQuery.near(new Point(lon,lat)).num(10).spherical(true).distanceMultiplier(6378137).maxDistance(100/6378137);
	    	TypedAggregation<User> aggregation = Aggregation.newAggregation(User.class, 
	    			Aggregation.geoNear(querys, "dis"),
	                Aggregation.match(  
	                        criteria
	                ),
	    			Aggregation.sort(sorts), 
	                Aggregation.skip(pagenum>1?(pagenum-1)*pagesize:0),  
	                Aggregation.limit(pagesize)
	        );  	 
	    	List<Classification> allCate = new ArrayList<Classification>();
	    	List<User> list = mongoTemplate.aggregate(aggregation, User.class).getMappedResults();
	    	for (User user : list) {
	    		if (user!=null&&user.getType() == 2) {
	    			if (user.getCate() != null && user.getCate().size()>0) {
	    				List<Classification> userCate = new ArrayList<Classification>();
		    			for (UserCate cate : user.getCate()) {
		    				if (cate!= null && cate.getCate() != null && cate.getCate().size()>0) {
			    				Classification c1 = classRepository.findById(cate.getCate().get(0));
			    				boolean AllCateHas = false;
			    				boolean UserCateHas = false;
			    				for (Classification classification : allCate) {
									if (classification.getId().equals(c1.getId())) {
										AllCateHas = true;
									}
								}
			    				for (Classification classification : userCate) {
									if (classification.getId().equals(c1.getId())) {
										UserCateHas = true;
									}
								}
			    				if (!AllCateHas) {
			    					allCate.add(c1);
			    				}
			    				if (!UserCateHas) {
			    					userCate.add(c1);
			    				}
		    				}
						}
		    			user.setCateList(userCate);
	    			}
    				Market market = marketRepository.findById(user.getMarketid());
    				if (market != null) {
    					user.setMarketName(market.getName());
    					if (market.getImgs()!=null && market.getImgs().size()>0) {
    						user.setMarketPic(market.getImgs().get(0));
    					}
    				} else {
    					result.setErr("-201", "查询市场信息失败");
    				}
    			}
			}
	    	result.setOk();
	    	result.setUsers(list);
	    	result.setAllClass(allCate);
        } else {
        	result.setErr("-200", "参数有误");
        }
		return result;
	}
}