package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.Config.RedisConfig;
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
import com.cay.Helper.AESHelper;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Users.entity.LoginEntity;
import com.cay.Model.Users.entity.UserEntity;
import com.cay.Model.Users.entity.UserListEntity;
import com.cay.Model.Users.vo.LoginRecord;
import com.cay.Model.Users.vo.User;
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
			@RequestParam("lon") double lon,
			@RequestParam("lat") double lat,
			@RequestParam("ciphertext") String ciphertext) {
		LoginEntity result = new LoginEntity();

		String deviceId = request.getHeader("X-DEVICEID");
		String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String pwd = param[0];
		String phone = param[1];
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
				if (user.getType() == 2 && user.getStatus() != 1) {
					result.setErr("-203", "当前商户未审核通过");
					return result;
				}
				LoginRecord record = new LoginRecord();
				String token = ParamUtils.generateString(32);
				record.setDeviceId(deviceId);
				record.setOperate(2);
				record.setLogin_identity(user.getType());
				record.setOp_time(System.currentTimeMillis());
				record.setPhone(phone);
//				Location local = ParamUtils.deserialize(location, Location.class);
				record.setLocation(new Location(lon,lat));			
				//token之后需要改为验证有效期
				record.setToken(token);
				result.setToken(token);
				result.setUserid(user.getId());
				result.setAvatar(user.getAvatar());
				result.setName(user.getName());
				mongoTemplate.save(record);
				//设置用户token存储到redis中
				jedis.setex("token_"+user.getId(), 60*60*24*3, token);//通过此方法，可以指定key的存活（有效时间） 时间为秒
				result.setOk();
			} else {
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
            @RequestParam(value="sex", required = false, defaultValue = "1") int sex,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="address", required = false, defaultValue = "") String address,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,
            @RequestParam(value="reason", required = false, defaultValue = "") String reason,
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
    	String cipher = "";
    	String phone = "";
    	String pwd = "";
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
    		result.setErr("-200", "商户用户信息不完整");
			return result;
    	} else {
    		if (!"".equals(identityImg)) {
        		user.setIdentityImg(identityImg);
        	}
        	if (!"".equals(shopImg)) {
        		user.setShopImg(shopImg);
        	}
        	if (!"".equals(marketid)) {
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
	@FarmAuth(validate = true)
    public UserEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	UserEntity result = new UserEntity();
    	User user = userRepository.findById(id);
        result.setUser(user);
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
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
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
        	query.addCriteria(Criteria.where("name").regex(".*?\\" +name+ ".*"));
        } 
        if (!"".equals(realName)) {
        	query.addCriteria(Criteria.where("realName").regex(".*?\\" +realName+ ".*"));
        } 
        if (!"".equals(phone)) {
        	query.addCriteria(Criteria.where("phone").regex(".*?\\" +phone+ ".*"));
        } 
        if (type>0) {
        	query.addCriteria(Criteria.where("type").is(type));  
        }
        if (status>-1) {
        	query.addCriteria(Criteria.where("status").is(status));  
        }
        if (type == 2 && lon > 0 && lat > 0 && max > 0) {
        	query.addCriteria(Criteria.where("shopLocation").near(new Point(lon,lat)).maxDistance(max));  
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, User.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		User.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, User.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            result.setOk();
            result.setUsers(lists);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
}