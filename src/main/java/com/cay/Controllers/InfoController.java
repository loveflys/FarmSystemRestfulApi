package com.cay.Controllers;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
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
        comment.setAvatar(user.getAvatar());
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setUserName(user.getName());
        comment.setCreateTime(new Date().getTime());
        comment.setDeleted(false);
        mongoTemplate.save(comment);
        result.setOk();
        return result;
    }
    
    @ApiOperation("修改信息")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
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
    	Info info = infoRepository.findById(id);
    	if (!"".equals(author)) {
    		info.setAuthorName(author);
    	}
    	if (comment>0) {
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
        if (view > 0) {
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
    @GetMapping("/get/{id}")
    public InfoEntity get(
    		@PathVariable(value="id", required = true) String id
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
    
    @ApiOperation("分页查询分类")
    @PostMapping("/list")
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
        try {
            if (paged == 1) {
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
            @RequestParam(value="deleted", required = false, defaultValue = "false") Boolean deleted,
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
        query.addCriteria(Criteria.where("deleted").is(deleted));         
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
    
    @ApiOperation("分页查询我的信息帖子评论")
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
