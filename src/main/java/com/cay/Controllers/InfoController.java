package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cay.Helper.auth.FarmAuth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.vo.BBS;
import com.cay.Model.Config.PushConfig;
import com.cay.Model.Config.PushExtra;
import com.cay.Model.Info.entity.CommentListEntity;
import com.cay.Model.Info.vo.Comment;
import com.cay.Model.Info.entity.InfoEntity;
import com.cay.Model.Info.entity.InfoListEntity;
import com.cay.Model.Info.vo.Info;
import com.cay.Model.Users.vo.User;
import com.cay.repository.InfoCommentRepository;
import com.cay.repository.InfoRepository;
import com.cay.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "信息服务",description="提供信息增删改查API")
@RestController
@RequestMapping("/info")
public class InfoController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private InfoRepository infoRepository;
	@Autowired
	private InfoCommentRepository infocommentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PushConfig pushConfig;
	
	@GetMapping("/set")    
	public void save() {
        // 初始化数据
        Info info1 = new Info();
        info1.setAuthorName("官方");
        info1.setCommentNum(5);
        info1.setCreateTime(new Date().getTime());
        info1.setContent("<p>输入内容的地方</p>");
        info1.setTitle("测试信息");
        info1.setType(1);
        info1.setViewNum(6);
        info1.setWeight(0);
        info1.setMainImg("http://m.yuan.cn/content/images/200.png");
        mongoTemplate.save(info1);
        
        Info info2 = new Info();
        info2.setAuthorName("美食栏目官方");
        info2.setCommentNum(2);
        info2.setCreateTime(new Date().getTime());
        info2.setContent("<p>输入美食内容的地方</p>");
        info2.setTitle("测试美食信息");
        info2.setType(2);
        info2.setViewNum(13);
        info2.setWeight(0);
        info2.setMainImg("http://m.yuan.cn/content/images/200.png");
        mongoTemplate.save(info2);
    }
	
	@GetMapping("/setc") 
	public void savecomment(HttpServletRequest request) {
		Comment ic1 = new Comment();
        ic1.setAnonymous(false);
        ic1.setAvatar("http://m.yuan.cn/content/images/200.png");
        ic1.setContent("厉害了我的哥");
        ic1.setCreateTime(new Date().getTime());
        ic1.setDeleted(false);
        ic1.setInfoId(infoRepository.findAll().iterator().next().getId());
        ic1.setUserId("587e4e3589df873dcc9471c2");
        ic1.setUserName("安一水果摊");
        mongoTemplate.save(ic1);
        
        Comment ic2 = new Comment();
        ic2.setAnonymous(false);
        ic2.setAvatar("http://m.yuan.cn/content/images/200.png");
        ic2.setContent("厉害了我的姐");
        ic2.setCreateTime(new Date().getTime());
        ic2.setDeleted(false);
        ic2.setInfoId(infoRepository.findAll().iterator().next().getId());
        ic2.setUserId("587e4e3589df873dcc9471c2");
        ic2.setUserName("安一水果摊");
        mongoTemplate.save(ic2);
	}
    
	@ApiOperation("新增信息")
    @PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="author", required = false, defaultValue = "官方") String author,
            @RequestParam(value="comment", required = false, defaultValue = "0") long comment,
            @RequestParam(value="view", required = false, defaultValue = "0") long view,
            @RequestParam(value="content", required = true) String content,
            @RequestParam(value="title", required = true) String title,
            @RequestParam(value="type", required = false, defaultValue = "1") int type,
            @RequestParam(value="weight", required = false, defaultValue = "0") int weight,
            @RequestParam(value="mainImg", required = false, defaultValue = "") String mainImg
    ) {
    	BaseEntity result = new BaseEntity();
    	Info info = new Info();
        info.setAuthorName(author);
        info.setCommentNum(comment);
        info.setCreateTime(new Date().getTime());
        info.setContent(content);
        info.setTitle(title);
        info.setType(type);
        info.setViewNum(view);
        info.setWeight(weight);
        info.setMainImg(mainImg);
        mongoTemplate.save(info);
        result.setOk();
        return result;
    }
	
	@ApiOperation("新增评论")
    @PostMapping("/addcomment")
    @FarmAuth(validate = true)
    public BaseEntity addcomment(
            @RequestParam(value="infoId", required = true) String infoId,
            @RequestParam(value="userId", required = true) String userId,
            @RequestParam(value="content", required = true) String content
    ) {
    	BaseEntity result = new BaseEntity();
    	Comment comment = new Comment();
    	//暂不支持匿名
    	comment.setAnonymous(false);
    	comment.setInfoId(infoId);
    	//过滤敏感词
    	
    	User user = userRepository.findById(userId);
        if (user == null) {
        	result.setErr("-200", "查询不到用户信息");
        }
        List<String> alias = new ArrayList<String>();
        comment.setAvatar(user.getAvatar());
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setUserName(user.getName());
        comment.setCreateTime(new Date().getTime());
        comment.setDeleted(false);
        mongoTemplate.save(comment);
        result.setOk();
        Iterator<Comment> comments = mongoTemplate.find(new Query().addCriteria(Criteria.where("infoId").is(infoId)), Comment.class).iterator();
        while (comments.hasNext()) {
        	Comment temp = comments.next();
        	User tempuser = userRepository.findById(temp.getUserId());
        	if (tempuser!=null) {
        		if (!alias.contains(tempuser.getDeviceId()) && tempuser.getPushsetting() != 0) {
        			alias.add(tempuser.getDeviceId());
        		}
        	}
        }
        
        Info info = infoRepository.findById(infoId);
        if (info!=null && info.getAuthorId() != null && !"".equals(info.getAuthorId()) && !userId.equals(info.getAuthorId())) {
        	User tempuser = userRepository.findById(info.getAuthorId());
        	if (tempuser != null&&!alias.contains(tempuser.getDeviceId()) && tempuser.getPushsetting() != 0) {
    			alias.add(tempuser.getDeviceId());
    		}
        }
        long commentNum = info.getCommentNum();
    	info.setCommentNum(commentNum + 1);
    	mongoTemplate.save(info);
        PushController push = new PushController();
        List<PushExtra> extralist = new ArrayList<PushExtra>();
        extralist.add(new PushExtra("id",info.getId()));
        extralist.add(new PushExtra("type","info"));
        push.pushMessage(info.getTitle(), JSONArray.toJSONString(alias), "新的信息评论", "新消息", JSONArray.toJSONString(extralist),pushConfig.getAppKey(),pushConfig.getMasterSecret());
        return result;
    }
    
    @ApiOperation("修改信息")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
            @RequestParam(value="author", required = false, defaultValue = "官方") String author,
            @RequestParam(value="comment", required = false, defaultValue = "-1") long comment,
            @RequestParam(value="view", required = false, defaultValue = "-1") long view,
            @RequestParam(value="content", required = false, defaultValue = "") String content,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="type", required = false, defaultValue = "1") int type,
            @RequestParam(value="weight", required = false, defaultValue = "0") int weight,
            @RequestParam(value="mainImg", required = false, defaultValue = "") String mainImg
    ) {
    	BaseEntity result = new BaseEntity();
    	Info info = infoRepository.findById(id);
    	if (!"".equals(author)) {
    		info.setAuthorName(author);
    	}
    	if (comment>-1) {
    		info.setCommentNum(comment);
    	}
        info.setUpdateTime(new Date().getTime());
        if (!"".equals(content)) {
        	info.setContent(content);
        }
        if (!"".equals(title)) {
        	info.setTitle(title);
        }
        if (type > 0) {
        	info.setType(type);
        }
        if (view > -1) {
        	info.setViewNum(view);
        }
        if (weight > 0) {
        	info.setWeight(weight);
        }
        if (!"".equals(mainImg)) {
        	info.setMainImg(mainImg);
        }
        mongoTemplate.save(info);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("修改帖子评论")
    @PostMapping("/updatecomment")
    @FarmAuth(validate = true)
    public BaseEntity updatecomment(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="deleted", required = false, defaultValue = "0") int deleted
    ) {
    	//目前仅支持设置帖子评论是否被删除
    	BaseEntity result = new BaseEntity();
    	Comment comment = infocommentRepository.findById(id);
    	if (deleted > 0) {
    		if (deleted == 1) {
    			comment.setDeleted(true);
    		} else {
    			comment.setDeleted(false);
    		}
    	}
        mongoTemplate.save(comment);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("获取信息")
    @GetMapping("/get")
    public InfoEntity get(
    		@RequestParam(value="id", required = true) String id
    ) {
    	InfoEntity result = new InfoEntity();
    	Info info = infoRepository.findById(id);
    	List<Comment> comments = mongoTemplate.find(new Query(Criteria.where("infoId").is(info.getId())), Comment.class);   
    	long viewNum = info.getViewNum();
    	info.setViewNum(viewNum + 1);
        mongoTemplate.save(info);
        info.setComments(comments);
        result.setInfo(info);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除信息")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	Info info = infoRepository.findById(id);
        mongoTemplate.remove(info);
        result.setOk();
        return result;
    }
    
    @ApiOperation("分页查询信息")
    @GetMapping("/list")
	public InfoListEntity list(
            HttpServletRequest request,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="type", required = false, defaultValue = "0") int type,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	InfoListEntity result = new InfoListEntity();
        List<Info> lists = new ArrayList<Info>();
        Query query = new Query();
        if (type>0) {
        	query.addCriteria(Criteria.where("type").is(type));  
        }
        if (title!=null && title.length()>0) {
        	query.addCriteria(Criteria.where("title").regex(".*?\\" +title+ ".*"));
        } 
        query.addCriteria(Criteria.where("deleted").is(false));
        try {
            if (paged == 1) {
            	System.out.println(sortby);
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Info.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Info.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Info.class);
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

    @ApiOperation("分页查询信息帖子评论--管理端")
    @GetMapping("/listcomment")
    @FarmAuth(validate = true)
    public CommentListEntity listcomment(
            HttpServletRequest request,
            @RequestParam(value="infoId", required = false, defaultValue = "") String infoId,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="deleted", required = false, defaultValue = "0") int deleted,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	CommentListEntity result = new CommentListEntity();
        List<Comment> lists = new ArrayList<Comment>();
        Query query = new Query();
        if (infoId!=null && infoId.length()>0) {
        	query.addCriteria(Criteria.where("infoId").is(infoId));
        } 
        if (userId!=null && userId.length()>0) {
        	query.addCriteria(Criteria.where("userId").is(userId));
        }
        if (deleted > 0) {
        	boolean delete = false;
        	if (deleted == 1) {
        		delete = true;
        	}
        	query.addCriteria(Criteria.where("deleted").is(delete));   
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Comment.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Comment.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, Comment.class);
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
    
    @ApiOperation("分页查询我评论过的信息")
    @GetMapping("/listcommentbyme")
    @FarmAuth(validate = true)
    public InfoListEntity listcommentbyme(
            HttpServletRequest request,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "2") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "createTime") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	InfoListEntity result = new InfoListEntity();
        List<Info> lists = new ArrayList<Info>();
        List<String> ids = new ArrayList<String>();
        Query query = new Query();
        if (userId!=null && userId.length()>0) {
        	query.addCriteria(Criteria.where("userId").is(userId));
        }
        query.addCriteria(Criteria.where("deleted").is(false));  
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Comment.class);
                //查询指定分页的内容
                Iterator<Comment> list = mongoTemplate.find(query.with(pageRequest),
                		Comment.class).iterator();
                while(list.hasNext()) {
                	Comment temp = list.next();
                	if (!ids.contains(temp.getInfoId())) {
	                	ids.add(temp.getInfoId());
	                	Info tempinfo = infoRepository.findById(temp.getInfoId());
	                	if (tempinfo != null) {
	                		lists.add(tempinfo);
	                	}
                	}
                }
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	Iterator<Comment> list = mongoTemplate.find(query, Comment.class).iterator();
            	while(list.hasNext()) {
                	Comment temp = list.next();
                	if (!ids.contains(temp.getInfoId())) {
	                	ids.add(temp.getInfoId());
	                	Info tempinfo = infoRepository.findById(temp.getInfoId());
	                	if (tempinfo != null) {
	                		lists.add(tempinfo);
	                	}
                	}
                }
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
    
    @ApiOperation("分页查询我的信息 的评论")
	@GetMapping("/mycomment")
    @FarmAuth(validate = true)
	public InfoListEntity mycomment(
            HttpServletRequest request,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	InfoListEntity result = new InfoListEntity();
    	List<Info> info = new ArrayList<Info>();
        List<Comment> lists = new ArrayList<Comment>();
        Query query = new Query();
        if (userId!=null && userId.length()>0) {
        	query.addCriteria(Criteria.where("userId").is(userId));
        }       
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, Comment.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		Comment.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);                
            } else {
            	lists = mongoTemplate.find(query, Comment.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            for (Comment comment : lists) {
				Info temp = infoRepository.findById(comment.getInfoId());
				info.add(temp);
			}
            result.setOk();
            result.setList(info);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
}
