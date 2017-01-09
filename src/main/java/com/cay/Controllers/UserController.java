package com.cay.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Model.Users.Label;
import com.cay.Model.Users.User;
import com.cay.repository.UserRepository;
import com.cay.service.UserService;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private MongoTemplate mongoTemplate;

    /**
     * 根据手机号码、用户名和密码新建用户
     * @return
     */
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

    /**
     * 查询所有用户列表
     */
    @GetMapping("/find")
    public List<User> find() {
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList;
    }
    
    /**
     * 根据手机号码/名字 查询用户信息
     */
    @GetMapping("/findby")
    public User findby(@RequestParam("phone") String phone, @RequestParam("username") String username) {
    	BasicDBList orList = new BasicDBList(); //用于记录
    	if (StringUtils.hasLength(phone)) {
    	  orList.add(new BasicDBObject("phone", phone));
    	}
    	if (StringUtils.hasLength(username)) {
    	  orList.add(new BasicDBObject("username", username));
    	}
    	BasicDBObject orDBObject = new BasicDBObject("$or", orList);
        User user = mongoTemplate.findOne(new BasicQuery(orDBObject), User.class);
        return user;
    }

    /**
     * 根据手机号查询用户信息
     * @return
     */
    @GetMapping("/findByphone")
    public User findByName(@RequestParam("phone") String phone) {
        User user = userService.findByPhone(phone);
        return user;
    }
    
    /**
     * 根据手机号码更新用户名称
     */
    @GetMapping("/update")
    public User update(@RequestParam("phone") String phone, @RequestParam("name") String name) {
    	//BasicDBObject base = new BasicDBObject("phone", phone);
    	User temp = userService.findByPhone(phone);
    	temp.setUsername(name);
        User user = userRepository.save(temp);
    	//BasicDBObject update = new BasicDBObject("username", name);
        //WriteResult result = mongoTemplate.updateFirst(new BasicQuery(base), new BasicUpdate(update), User.class);
        return user;
    }
    
    /**
     * 根据手机号码删除用户
     */
    @GetMapping("/del")
    public WriteResult del(@RequestParam("phone") String phone) {
    	BasicDBObject base = new BasicDBObject("phone", phone);
        WriteResult result = mongoTemplate.remove(new BasicQuery(base), User.class);
        return result;
    }
}
