package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.cay.Helper.auth.FarmAuth;
import com.cay.Model.Config.RedisConfig;
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
import com.cay.Helper.AESHelper;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Manager.entity.ManageInitEntity;
import com.cay.Model.Manager.entity.ManagerEntity;
import com.cay.Model.Manager.entity.ManagerListEntity;
import com.cay.Model.Manager.vo.Manager;
import com.cay.Model.Users.entity.LoginEntity;
import com.cay.Model.Users.vo.LoginRecord;
import com.cay.repository.ManagerRepository;
import com.cay.service.ManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Api(value = "管理员服务",description="提供管理员信息增删改查API")
@RestController
@RequestMapping("/manager")

public class ManagerController {
	@Autowired
	private RedisConfig redisConfig;

	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private com.cay.Model.Config.AESConfig aes;

	@ApiOperation("初始化-设置系统超级管理员")
    @GetMapping("/init")
    public ManageInitEntity init() throws Exception {
		ManageInitEntity result = new ManageInitEntity();
		Manager manager = new Manager();
		manager.setCreateTime(new Date().getTime());
		/*
		 * 是否被禁用
		 * 0为否 1 为已禁用
		 */
		manager.setDisabled(0);
		manager.setLogin("theboss");
		manager.setName("系统管理员");
		String pwd = ParamUtils.generateNumber(8);
		String cipher = AESHelper.encrypt(pwd.getBytes(), aes.getKey(), aes.getIv());
		manager.setPassword(cipher);
		manager.setType(4);
		manager.setDisabled(0);
		manager.setIsdelete(0);
		try {
			mongoTemplate.save(manager);
			result.setOk();
			result.setLogin(manager.getLogin());
			result.setPwd(pwd);
		} catch (Exception e) {
			result.setErr("-222", e.getMessage());
		}		
        return result;
    }
	
	@ApiOperation("登录")
    @PostMapping("/login")
    public LoginEntity Login(
			HttpServletRequest request,
			@RequestParam("ciphertext") String ciphertext) {
		LoginEntity result = new LoginEntity();

		String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String pwd = param[0];
		String login = param[1];
		JedisPool pool = new JedisPool(new JedisPoolConfig(), redisConfig.getIp());
		Jedis jedis = pool.getResource();

		Manager manager = managerService.findByLogin(login);
		if (manager == null) {
			result.setErr("-200", "账户不存在");
		} else {	
			if (pwd.equals(manager.getPassword())) {
				if (manager.getType() == 1 && manager.getDisabled() == 1) {
					result.setErr("-202", "当前账户已被禁用");
					return result;
				}
				LoginRecord record = new LoginRecord();
				String token = ParamUtils.generateString(32);
				record.setOperate(2);
				record.setOp_time(System.currentTimeMillis());	
				//token之后需要改为验证有效期
				record.setToken(token);
				record.setLogin(manager.getLogin());
				record.setLogin_identity(manager.getType());
				result.setToken(token);				
				result.setUserid(manager.getId());
				result.setAvatar(manager.getAvatar());
				result.setName(manager.getName());
				mongoTemplate.save(record);
				//设置用户token存储到redis中
				jedis.setex("token_"+manager.getId(), 60*60*24*3, token);//通过此方法，可以指定key的存活（有效时间） 时间为秒
				result.setOk();
			} else {
				result.setErr("-201", "用户名/密码错误,密码是==>"+AESHelper.decrypt(manager.getPassword().getBytes(), aes.getKey(), aes.getIv()));
			}			
		}
        return result;
    }
    
	@ApiOperation("新增管理员")
    @PostMapping("/add")
	@FarmAuth(validate = true)
    public ManagerEntity add(
    		@RequestParam(value="ciphertext", required = true) String ciphertext,
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,   
            @RequestParam(value="sex", required = false, defaultValue = "0") int sex
    ) {
    	ManagerEntity result = new ManagerEntity();
    	String cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());
		String[] param = cipher.split("\\*");
		String password = param[0];
		String phone = param[1];
		String login = param[2];
    	Manager manager = new Manager();
    	manager.setLogin(login);
    	manager.setPassword(password);
    	manager.setPhone(phone);
    	manager.setName(name);
    	if (!"".equals(realName)) {
    		manager.setRealName(realName);
    	}    	
    	if (!"".equals(avatar)) {
    		manager.setAvatar(avatar);
    	}
        if (sex > 0) {
        	manager.setSex(sex);
        }
        manager.setIsdelete(0);
        manager.setDisabled(0);
        manager.setCreateTime(new Date().getTime());
        mongoTemplate.save(manager);
        result.setOk();
        result.setManager(manager);
        return result;
    }    
    	
	@ApiOperation("修改管理员")
    @PostMapping("/update")
	@FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="ciphertext", required = false, defaultValue = "") String ciphertext,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="realName", required = false, defaultValue = "") String realName,
            @RequestParam(value="avatar", required = false, defaultValue = "") String avatar,   
            @RequestParam(value="sex", required = false, defaultValue = "0") int sex,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="isdelete", required = false, defaultValue = "-1") int isdelete,
            @RequestParam(value="disabled", required = false, defaultValue = "-1") int disabled
    ) {
    	BaseEntity result = new BaseEntity();
    	Manager manager = managerRepository.findById(id);
    	String cipher = "";
    	String phone = "";
    	String login = "";
    	String pwd = "";
    	if (!"".equals(ciphertext)) {
	    	cipher = AESHelper.decrypt(ciphertext.getBytes(), aes.getKey(), aes.getIv());	    	
			String[] param = cipher.split("\\*");
			pwd = param[0];
			phone = param[1];	
			login = param[2];
    	}
		if (!"".equals(login)) {
    		manager.setLogin(login);
    	}
    	if (!"".equals(pwd)) {
    		manager.setPassword(pwd);
    	}
    	if (!"".equals(phone)) {
    		manager.setPhone(phone);
    	}
    	if (!"".equals(name)) {
    		manager.setName(name);
    	}
    	if (!"".equals(realName)) {
    		manager.setRealName(realName);
    	}
    	if (!"".equals(avatar)) {
    		manager.setAvatar(avatar);
    	}
        if (sex > 0) {
        	manager.setSex(sex);
        }
        if (type > 0) {
        	manager.setType(type);
        }
        if (isdelete > -1 && isdelete != manager.getIsdelete() ) {
        	manager.setIsdelete(isdelete);
        }
        if (disabled > -1 && disabled != manager.getDisabled()) {
        	manager.setDisabled(disabled);
        }
        manager.setUpdateTime(new Date().getTime());
        mongoTemplate.save(manager);
        result.setOk();
        return result;
    } 
    
    @ApiOperation("获取管理员")
    @GetMapping("/get")
	@FarmAuth(validate = true)
    public ManagerEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	ManagerEntity result = new ManagerEntity();
    	Manager manager = managerRepository.findById(id);
        result.setManager(manager);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除管理员")
    @PostMapping("/del")
	@FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Manager manager = managerRepository.findById(id);
        mongoTemplate.remove(manager);
        result.setOk();
        return result;
    }
    
    @ApiOperation("分页查询管理员")
    @PostMapping("/list")
	@FarmAuth(validate = true)
	public ManagerListEntity list(
            HttpServletRequest request,
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="login", required = false, defaultValue = "") String login,
            @RequestParam(value="phone", required = false, defaultValue = "") String phone,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	ManagerListEntity result = new ManagerListEntity();
        List<Manager> lists = new ArrayList<Manager>();
        Query query = new Query();
        if (!"".equals(name)) {
        	query.addCriteria(Criteria.where("name").regex(".*?\\" +name+ ".*"));
        } 
        if (!"".equals(login)) {
        	query.addCriteria(Criteria.where("login").is(login));
        } 
        if (!"".equals(phone)) {
        	query.addCriteria(Criteria.where("phone").regex(".*?\\" +phone+ ".*"));
        } 
        if (type>0) {
        	query.addCriteria(Criteria.where("type").is(type));  
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Manager.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Manager.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Manager.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            result.setOk();
            result.setLists(lists);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
}