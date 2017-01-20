package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

@Api(value = "用户服务",description="提供用户信息增删改查API")
@RestController
@RequestMapping("/user")
public class UserController {
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
			user.setCreatetime(System.currentTimeMillis());
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
				result.setOk();
			} else {
				result.setErr("-201", "用户名/密码错误");
			}			
		}
        return result;
    }
    
	@ApiOperation("修改用户")
    @PostMapping("/update")
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
            @RequestParam(value="pwd", required = false, defaultValue = "") String password,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="address", required = false, defaultValue = "") String address,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,
            @RequestParam(value="identityImg", required = false, defaultValue = "") String identityImg,
            @RequestParam(value="shopImg", required = false, defaultValue = "") String shopImg,            
            @RequestParam(value="marketid", required = false, defaultValue = "") String marketid,
            @RequestParam(value="lon", required = false, defaultValue = "0") double lon,
            @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
            @RequestParam(value="sex", required = false, defaultValue = "0") int sex,
            @RequestParam(value="isdelete", required = false, defaultValue = "0") int isdelete,
            @RequestParam(value="disabled", required = false, defaultValue = "0") int disabled,
            @RequestParam(value="pushsetting", required = false, defaultValue = "10") int pushsetting
    ) {
    	BaseEntity result = new BaseEntity();
    	User user = userRepository.findById(id);
    	if (!"".equals(password)) {
    		user.setPassword(password);
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
    	if (!"".equals(phone)) {
    		user.setPhone(phone);
    	}
    	if (!"".equals(avatar)) {
    		user.setAvatar(avatar);
    	}
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
        if (sex > 0) {
        	user.setSex(sex);
        }
        if (isdelete > 0) {
        	user.setIsdelete(isdelete);
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
    
    @ApiOperation("获取用户")
    @GetMapping("/get/{id}")
    public UserEntity get(
    		@PathVariable(value="id", required = true) String id
    ) {
    	UserEntity result = new UserEntity();
    	User user = userRepository.findById(id);
        result.setUser(user);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除用户")
    @PostMapping("/del")
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	User user = userRepository.findById(id);
        mongoTemplate.remove(user);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("分页查询用户")
	@GetMapping("/list")
	public UserListEntity list(
            HttpServletRequest request,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
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