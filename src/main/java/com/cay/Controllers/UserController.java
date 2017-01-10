package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.Users.entity.UserEntity;
import com.cay.Model.Users.entity.UserListEntity;
import com.cay.Model.Users.vo.Label;
import com.cay.Model.Users.vo.User;
import com.cay.repository.UserRepository;
import com.cay.service.UserService;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;

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

	@ApiOperation("根据手机号码、用户名和密码新建用户")
    @GetMapping("/save")
    public User save(@RequestParam("phone") String phone, @RequestParam("user") String username, @RequestParam("pwd") String password) {
    	User temp = userService.findByPhone(phone);
    	User user = null;
    	if (temp == null) {
	    	ArrayList<Label> labels = null;
	        user = new User(username, password, 1, "山东青岛李沧区1688产业园", phone, "http://m.yuan.cn/content/images/200.png", labels, System.currentTimeMillis(), 0, 0, 0);
	        mongoTemplate.save(user);
    	}
        return user;
    }

	@ApiOperation(
			value="查询所有用户列表(可分页)", 
			notes="无参数时获取全部用户,paged传1时调取分页获取用户，pagenum默认为1，pagesize默认为10")
    @GetMapping("/find")
    public UserListEntity find(HttpServletRequest request, @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum, 
    		@RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize, 
    		@RequestParam(value="sort", required = false, defaultValue = "") String sort, 
    		@RequestParam(value="sortby", required = false, defaultValue = "") String sortby, 		
    		@RequestParam(value="paged", required = false, defaultValue = "0") int paged) {
		List<User> lists = new ArrayList<User>();
		UserListEntity result = new UserListEntity();
		try {
			if (paged == 1) {
				//构建分页信息
		        PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
		        Query query = new Query();
		        long totalCount = mongoTemplate.count(query, User.class);
		        //查询指定分页的内容
		        Iterator<User> users =  userRepository.findAll(pageRequest).iterator();	        
		        while(users.hasNext()){
		            lists.add(users.next());
		        }
		        long totalPage = (totalCount+pagesize-1)/pagesize;
		        result.setTotalCount(totalCount);
		        result.setTotalPage(totalPage);
			} else {
				lists = mongoTemplate.findAll(User.class);
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
    
    @ApiOperation("根据手机号码/名字 查询用户信息")
    @GetMapping("/findby")
    public UserEntity findby(@RequestParam("phone") String phone, @RequestParam("username") String username) {
    	UserEntity result = new UserEntity();
    	try {
    		BasicDBList orList = new BasicDBList(); //用于记录
        	if (StringUtils.hasLength(phone)) {
        	  orList.add(new BasicDBObject("phone", phone));
        	}
        	if (StringUtils.hasLength(username)) {
        	  orList.add(new BasicDBObject("username", username));
        	}
        	BasicDBObject orDBObject = new BasicDBObject("$or", orList);
            User user = mongoTemplate.findOne(new BasicQuery(orDBObject), User.class);
            result.setOk();
            result.setUser(user);
		} catch (Exception e) {
			result.setErr("-200", "00", e.getMessage());
		}    	        
        return result;
    }

    @ApiOperation("根据手机号查询用户信息")
    @GetMapping("/findByphone")
    public UserEntity findByName(@RequestParam("phone") String phone) {        
        UserEntity result = new UserEntity();
    	try {
    		User user = userService.findByPhone(phone);
            result.setOk();
            result.setUser(user);
		} catch (Exception e) {
			result.setErr("-200", "00", e.getMessage());
		}    	        
        return result;
    }
    
    @ApiOperation("根据手机号码更新用户名称")
    @GetMapping("/update")
    public BaseEntity update(@RequestParam("phone") String phone, @RequestParam("name") String name) {
        BaseEntity result = new BaseEntity();
    	try {
        	//BasicDBObject base = new BasicDBObject("phone", phone);
        	User temp = userService.findByPhone(phone);
        	temp.setUsername(name);
            User user = userRepository.save(temp);
        	//BasicDBObject update = new BasicDBObject("username", name);
            //WriteResult result = mongoTemplate.updateFirst(new BasicQuery(base), new BasicUpdate(update), User.class);
            if (user != null) {
            	result.setOk();
            } else {
            	result.setErr("-100", "查询失败，请稍后再试.");
            }
		} catch (Exception e) {
			result.setErr("-200", "00", e.getMessage());
		}    	        
        return result;
    }
    
    @ApiOperation("根据手机号码删除用户")
    @GetMapping("/del")
    public BaseEntity del(@RequestParam("phone") String phone) {
    	
        BaseEntity result = new BaseEntity();
    	try {
    		BasicDBObject base = new BasicDBObject("phone", phone);
            WriteResult results = mongoTemplate.remove(new BasicQuery(base), User.class);
            if (results.getN() == 1) {
            	result.setOk();
            } else {
            	result.setErr("-100", "删除失败，请稍后再试.");
            }
		} catch (Exception e) {
			result.setErr("-200", "00", e.getMessage());
		}    	        
        return result;
    }
}
