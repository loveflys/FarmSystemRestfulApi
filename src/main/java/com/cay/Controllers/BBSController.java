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

import com.alibaba.fastjson.JSONArray;
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.entity.BBSEntity;
import com.cay.Model.BBS.entity.BBSListEntity;
import com.cay.Model.BBS.entity.CommentListEntity;
import com.cay.Model.BBS.vo.BBS;
import com.cay.Model.BBS.vo.Comment;
import com.cay.Model.Users.vo.User;
import com.cay.repository.BBSCommentRepository;
import com.cay.repository.BBSRepository;
import com.cay.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "论坛服务",description="提供论坛相关增删改查API")
@RestController
@RequestMapping("/bbs")
public class BBSController {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private BBSRepository bbsRepository;
	@Autowired
	private BBSCommentRepository bbscommentRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/set")    
	public void save() {
        // 初始化数据
        BBS bbs1 = new BBS();
        bbs1.setAuthorId("");
        bbs1.setAuthorName("陈安一");
        bbs1.setCommentNum(0);
        bbs1.setComments(null);
        bbs1.setContent("第一次发帖！");
        bbs1.setCreateTime(new Date().getTime());
        bbs1.setDeleted(false);
        bbs1.setFavNum(0);
        bbs1.setImgs(null);
        bbs1.setIsTop(true);
        bbs1.setStatus(1);
        bbs1.setTitle("第一条帖子！");
        bbs1.setViewNum(0);
        bbs1.setWeight(0);
        
        BBS bbs2 = new BBS();
        bbs2.setAuthorId("");
        bbs2.setAuthorName("陈安er");
        bbs2.setCommentNum(0);
        bbs2.setComments(null);
        bbs2.setContent("第er次发帖！");
        bbs2.setCreateTime(new Date().getTime());
        bbs2.setDeleted(false);
        bbs2.setFavNum(0);
        bbs2.setImgs(null);
        bbs2.setIsTop(false);
        bbs2.setStatus(1);
        bbs2.setTitle("第二条帖子！");
        bbs2.setViewNum(0);
        bbs2.setWeight(0);
        mongoTemplate.save(bbs2);        
    }
    
	@ApiOperation("新增帖子")
    @PostMapping("/add")
    @FarmAuth(validate = true)
    public BaseEntity add(
            @RequestParam(value="authorId", required = true) String authorId,
            @RequestParam(value="authorName", required = false, defaultValue = "") String authorName,
            @RequestParam(value="content", required = true) String content,
            @RequestParam(value="title", required = true) String title,
            @RequestParam(value="istop", required = false, defaultValue = "false") Boolean istop,
            @RequestParam(value="status", required = false, defaultValue = "0") int status,
            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="viewnum", required = false, defaultValue = "0") long viewnum,
            @RequestParam(value="weight", required = false, defaultValue = "0") int weight
    ) {
    	BaseEntity result = new BaseEntity();
    	BBS bbs = new BBS();
    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
    	bbs.setAuthorId(authorId);
    	if (!"".equals(authorName)) {
    		bbs.setAuthorName(authorName);
    	}
    	bbs.setCommentNum(0);
    	bbs.setCreateTime(new Date().getTime());
    	bbs.setContent(content);
    	bbs.setTitle(title);
    	bbs.setIsTop(istop);
    	bbs.setStatus(status);
    	bbs.setImgs(imgs);
		bbs.setViewNum(viewnum);
		bbs.setWeight(weight);
        mongoTemplate.save(bbs);
        result.setOk();
        return result;
    }
	
	@ApiOperation("新增评论")
    @PostMapping("/addcomment")
    @FarmAuth(validate = true)
    public BaseEntity addcomment(
            @RequestParam(value="bbsId", required = true) String bbsId,
            @RequestParam(value="userId", required = true) String userId,
            @RequestParam(value="content", required = true) String content
    ) {
    	BaseEntity result = new BaseEntity();
    	Comment comment = new Comment();
    	//暂不支持匿名
    	comment.setAnonymous(false);
    	comment.setBbsId(bbsId);
    	
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
    
    @ApiOperation("修改帖子")
    @PostMapping("/update")
    @FarmAuth(validate = true)
    public BaseEntity update(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="authorId", required = false, defaultValue = "") String authorId,
            @RequestParam(value="authorName", required = false, defaultValue = "") String authorName,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="content", required = false, defaultValue = "") String content,
            @RequestParam(value="istop", required = false, defaultValue = "false") Boolean istop,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="commentnum", required = false, defaultValue = "0") long commentnum,
            @RequestParam(value="viewnum", required = false, defaultValue = "0") long viewnum,
            @RequestParam(value="weight", required = false, defaultValue = "0") int weight
    ) {
    	BaseEntity result = new BaseEntity();
    	BBS bbs = bbsRepository.findById(id);
    	List<String> imgs = JSONArray.parseArray(imgarray, String.class);
    	if (!"".equals(authorId)) {
    		bbs.setAuthorId(authorId);
    	}
    	if (!"".equals(authorName)) {
    		bbs.setAuthorName(authorName);
    	}
    	if (!"".equals(title)) {
    		bbs.setTitle(title);
    	}
    	if (!"".equals(authorName)) {
    		bbs.setContent(content);
    	}
    	if (istop != bbs.getIsTop()) {
    		bbs.setIsTop(istop);
    	}
    	if (imgs.size() > 0) {
    		bbs.setImgs(imgs);
    	}
    	if (status > -1 && status != bbs.getStatus() ) {
        	bbs.setStatus(status);
    	}
    	if (commentnum > -1 && commentnum != bbs.getCommentNum() ) {
        	bbs.setCommentNum(commentnum);
    	}
    	if (viewnum > -1 && viewnum != bbs.getViewNum() ) {
        	bbs.setViewNum(viewnum);
    	}
    	if (weight > -1 && weight != bbs.getWeight() ) {
        	bbs.setWeight(weight);
    	}
    	bbs.setUpdateTime(new Date().getTime());
        mongoTemplate.save(bbs);
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
    	Comment comment = bbscommentRepository.findById(id);
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
    
    @ApiOperation("获取帖子")
    @GetMapping("/get/{id}")
    public BBSEntity get(
    		@PathVariable(value="id", required = true) String id
    ) {
    	BBSEntity result = new BBSEntity();
    	BBS bbs = bbsRepository.findById(id);    	
    	List<Comment> comments = mongoTemplate.find(new Query(Criteria.where("bbsId").is(bbs.getId())), Comment.class);    	
    	long viewNum = bbs.getViewNum();
    	bbs.setViewNum(viewNum + 1);
        mongoTemplate.save(bbs);
        bbs.setComments(comments);
        result.setBbs(bbs);
        result.setOk();
        return result;
    }
    
    @ApiOperation("删除帖子")
    @PostMapping("/del")
    @FarmAuth(validate = true)
    public BaseEntity del(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = true) String id
    ) {
    	BaseEntity result = new BaseEntity();
    	BBS bbs = bbsRepository.findById(id);
    	List<Comment> comments = mongoTemplate.find(new Query(Criteria.where("bbsId").is(bbs.getId())), Comment.class);
    	for (Comment comment : comments) {
			mongoTemplate.remove(comment);
			//直接删除帖子下所有评论 或者 评论的删除属性设置为true
//			comment.setDeleted(true);
//			mongoTemplate.save(comment);
		}
        mongoTemplate.remove(bbs);
        result.setOk();
        return result;
    }    
    
    @ApiOperation("分页查询论坛帖子")
    @PostMapping("/list")
	public BBSListEntity list(
            HttpServletRequest request,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(value="authorId", required = false, defaultValue = "") String authorId,
            @RequestParam(value="istop", required = false, defaultValue = "-1") int istop,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	BBSListEntity result = new BBSListEntity();
        List<BBS> lists = new ArrayList<BBS>();
        Query query = new Query();
        if (title!=null && title.length()>0) {
        	query.addCriteria(Criteria.where("title").regex(".*?\\" +title+ ".*"));
        } 
        if (authorId!=null && authorId.length()>0) {
        	query.addCriteria(Criteria.where("authorId").is(authorId));
        } 
        if (status > -1) {
        	query.addCriteria(Criteria.where("status").is(status));  
        }
        if (istop > -1) {
        	if (istop == 1) {
        		query.addCriteria(Criteria.where("istop").is(true));  
        	} else {
        		query.addCriteria(Criteria.where("istop").is(false));  
        	}
        }
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, BBS.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		BBS.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, BBS.class);
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
    
    @ApiOperation("分页查询论坛帖子评论--管理端")
    @PostMapping("/listcomment")
    @FarmAuth(validate = true)
	public CommentListEntity listcomment(
            HttpServletRequest request,
            @RequestParam(value="bbsId", required = false, defaultValue = "") String bbsId,
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
        if (bbsId!=null && bbsId.length()>0) {
        	query.addCriteria(Criteria.where("bbsId").is(bbsId));
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
    
    @ApiOperation("分页查询我的论坛帖子评论")
    @PostMapping("/mycomment")
    @FarmAuth(validate = true)
	public BBSListEntity mycomment(
            HttpServletRequest request,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	BBSListEntity result = new BBSListEntity();
    	List<BBS> bbs = new ArrayList<BBS>();
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
				BBS temp = bbsRepository.findById(comment.getBbsId());
				bbs.add(temp);
			}
            result.setOk();
            result.setList(bbs);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
}
