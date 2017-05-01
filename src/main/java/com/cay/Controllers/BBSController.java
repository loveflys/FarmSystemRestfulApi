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
import com.cay.Helper.ParamUtils;
import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.entity.BBSEntity;
import com.cay.Model.BBS.entity.BBSListEntity;
import com.cay.Model.BBS.entity.CommentListEntity;
import com.cay.Model.BBS.vo.BBS;
import com.cay.Model.BBS.vo.Comment;
import com.cay.Model.Config.PushConfig;
import com.cay.Model.Config.PushExtra;
import com.cay.Model.Favorite.vo.favorite;
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
	@Autowired
	private PushConfig pushConfig;
	
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
        bbs1.setIsTop(false);
        bbs1.setStatus(1);
        bbs1.setTitle("第一条帖子！");
        bbs1.setViewNum(0);
        bbs1.setWeight(0);
        mongoTemplate.save(bbs1);  
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
        BBS bbs3 = new BBS();
        bbs3.setAuthorId("");
        bbs3.setAuthorName("陈安一");
        bbs3.setCommentNum(4);
        bbs3.setComments(null);
        bbs3.setContent("第三次发帖！");
        bbs3.setCreateTime(new Date().getTime());
        bbs3.setDeleted(false);
        bbs3.setFavNum(0);
        bbs3.setImgs(null);
        bbs3.setIsTop(true);
        bbs3.setStatus(0);
        bbs3.setTitle("第三条帖子！");
        bbs3.setViewNum(5);
        bbs3.setWeight(3);
        mongoTemplate.save(bbs3);
    }
	
	@GetMapping("/setc")    
	public void savec(HttpServletRequest request) {
        // 初始化数据
        Comment bbs1 = new Comment();
        bbs1.setAnonymous(false);
        bbs1.setAvatar("http://m.yuan.cn/content/images/200.png");
        bbs1.setContent("厉害了我的哥");
        bbs1.setCreateTime(new Date().getTime());
        bbs1.setDeleted(false);
        bbs1.setBbsId(bbsRepository.findAll().iterator().next().getId());
        bbs1.setUserId("587e4e3589df873dcc9471c2");
        bbs1.setUserName("安一水果摊");
        mongoTemplate.save(bbs1);  
        Comment bbs2 = new Comment();
        bbs2.setAnonymous(false);
        bbs2.setAvatar("http://m.yuan.cn/content/images/200.png");
        bbs2.setContent("厉害了我的姐姐");
        bbs2.setCreateTime(new Date().getTime());
        bbs2.setDeleted(false);
        bbs2.setBbsId(bbsRepository.findAll().iterator().next().getId());
        bbs2.setUserId("587e4e3589df873dcc9471c2");
        bbs2.setUserName("安一水果摊");
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
    	bbs.setDeleted(false);
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
    	List<String> alias = new ArrayList<String>();
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
        BBS bbs = bbsRepository.findById(bbsId);
        if (bbs!=null && !userId.equals(bbs.getAuthorId())) {
        	User tempuser = userRepository.findById(bbs.getAuthorId());
        	if (tempuser != null && !alias.contains(tempuser.getDeviceId()) && tempuser.getPushsetting() != 0) {
    			alias.add(tempuser.getDeviceId());
    		}
        }
        long commentNum = bbs.getCommentNum();
        bbs.setCommentNum(commentNum + 1);
        mongoTemplate.save(bbs);
        if (alias != null && alias.size() > 0) {
	        PushController push = new PushController();
	        List<PushExtra> extralist = new ArrayList<PushExtra>();
	        extralist.add(new PushExtra("id",bbs.getId()));
	        extralist.add(new PushExtra("type","bbs"));
	        String pushalias = JSONArray.toJSONString(alias);
	        String pushextra = JSONArray.toJSONString(extralist);
	        push.push(bbs.getTitle(), pushalias, "新的帖子评论", "", pushextra,pushConfig.getAppKey(),pushConfig.getMasterSecret());
        }
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
            @RequestParam(value="deleted", required = false, defaultValue = "-1") int deleted,
            @RequestParam(value="content", required = false, defaultValue = "") String content,
            @RequestParam(value="istop", required = false, defaultValue = "-1") int istop,
            @RequestParam(value="status", required = false, defaultValue = "-1") int status,
            @RequestParam(value="imgs", required = false, defaultValue = "[]") String imgarray,
            @RequestParam(value="commentnum", required = false, defaultValue = "-1") long commentnum,
            @RequestParam(value="viewnum", required = false, defaultValue = "-1") long viewnum,
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
    	if (istop > -1) {
    		if (istop == 1) {
    			bbs.setIsTop(true);
    		} else {
    			bbs.setIsTop(false);
    		}
    	}
    	if (imgs.size() > 0) {
    		bbs.setImgs(imgs);
    	}
    	if (status > -1 && status != bbs.getStatus() ) {
    		if (bbs.getStatus() == 0) {
    			//待审核 审核或者拒绝后，推送消息给发帖人
    			List<String> alias = new ArrayList<String>();
    			User tempuser = userRepository.findById(bbs.getAuthorId());
            	if (tempuser.getPushsetting() != 0) {
        			alias.add(tempuser.getDeviceId());
        		}
            	PushController push = new PushController();
                List<PushExtra> extralist = new ArrayList<PushExtra>();
                extralist.add(new PushExtra("id",bbs.getId()));
                extralist.add(new PushExtra("type","bbs"));
                String pushalias = JSONArray.toJSONString(alias);
                String pushextra = JSONArray.toJSONString(extralist);
                String notifyResult = "";
                if (status == 1) {
                	notifyResult = "您的帖子已审核通过，打开app看看吧。";
                } else {
                	notifyResult = "您的帖子未通过，打开app查看拒绝原因。";
                }
                push.push(bbs.getTitle(), pushalias, notifyResult, "", pushextra,pushConfig.getAppKey(),pushConfig.getMasterSecret());
    		}
        	bbs.setStatus(status);
    	}
    	if (deleted > -1 && !bbs.getDeleted()) {
        	bbs.setDeleted(true);
    	}
    	if (bbs.getStatus() == 2 && !bbs.getDeleted()) {
    		//审核拒绝的帖子，修改信息后将状态改为待审核
    		bbs.setStatus(0);
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
    
    @ApiOperation("审核帖子")
    @PostMapping("/check")
	@FarmAuth(validate = true)
	public BaseEntity check(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="reason", required = false, defaultValue = "") String reason,
    		@RequestParam(value="status", required = false, defaultValue = "true") boolean status
    ) {
    	BaseEntity result = new BaseEntity();
    	BBS bbs = bbsRepository.findById(id);
    	if (bbs == null) {
    		result.setErr("-200", "未查询到帖子。");
    		return result;
    	}
    	if (bbs.getDeleted()) {
    		result.setErr("-200", "帖子已被删除");
			return result;
    	}
    	
    	if (bbs.getStatus() == 1) {
    		result.setErr("-200", "已审核通过，请勿重复审核。");
			return result;
    	}
    	
    	if (bbs.getStatus() == 0 && !status) {
			//审核不通过
			if (reason == null || "".equals(reason)) {
				result.setErr("-200", "拒绝理由不能为空");
				return result;
			} else {
				bbs.setStatus(2);
				bbs.setReason(reason);
			}
		} else {
			bbs.setStatus(1);
		}
    	bbs.setUpdateTime(new Date().getTime());
        mongoTemplate.save(bbs);
        result.setOk();
        return result;
    }
    
	@ApiOperation("获取帖子")
    @GetMapping("/get")
    public BBSEntity get(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = true) String id
    ) {
    	BBSEntity result = new BBSEntity();
    	BBS bbs = bbsRepository.findById(id);   
    	String userid = "";
    	if (!"".equals(request.getHeader("X-USERID"))) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user != null) {
	        	userid = user.getId();
	        	Query query = new Query();
	            query.addCriteria(Criteria.where("favUserId").is(userid));
	            query.addCriteria(Criteria.where("favType").is(2));
	            query.addCriteria(Criteria.where("favId").is(id));
	            
	            favorite fav = mongoTemplate.findOne(query, favorite.class);
	        	if (fav != null) {
	        		bbs.setFav(1);
	        	} else {
	        		bbs.setFav(2);
	        	}
	        }
		}
        long viewNum = bbs.getViewNum();
    	bbs.setViewNum(viewNum + 1);
    	List<Comment> comments = mongoTemplate.find(new Query(Criteria.where("bbsId").is(bbs.getId())), Comment.class);    
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
    
    @ApiOperation("收藏/取消收藏 帖子")
    @PostMapping("/fav")
    @FarmAuth(validate = true)
    public BaseEntity fav(
    		HttpServletRequest request,
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="op", required = false, defaultValue = "0") int op
    ) {
    	BaseEntity result = new BaseEntity();
    	String userid = "";
    	if (!"".equals(request.getHeader("X-USERID"))) {
			User user = userRepository.findById(request.getHeader("X-USERID"));
	        if (user != null) {
	        	userid = user.getId();
	        } else {
	        	result.setErr("-200", "请先登录后再试");
	        }
		} else {
			result.setErr("-200", "请先登录后再试");
		}
    	BBS bbs = bbsRepository.findById(id);
    	if (bbs == null || bbs.getDeleted() || bbs.getStatus() == 0) {
    		result.setErr("-200", "帖子已失效");   
    		return result;
    	} else {
    		Query query = new Query();
	        query.addCriteria(Criteria.where("favUserId").is(userid));
	        query.addCriteria(Criteria.where("favType").is(2));
	        query.addCriteria(Criteria.where("favId").is(id));
    		if (op == 1) {
				favorite temp = mongoTemplate.findOne(query, favorite.class);
				if (temp != null) {
					temp.setFavTime(new Date().getTime());
					bbs.setFavNum(bbs.getFavNum()+1);
					mongoTemplate.save(temp);
					mongoTemplate.save(bbs);
				} else {
					favorite fav = new favorite();
					fav.setFavType(2);
					fav.setFavId(id);
					fav.setFavTime(new Date().getTime());
					fav.setFavUserId(userid);
					bbs.setFavNum(bbs.getFavNum()+1);
					mongoTemplate.save(fav);
					mongoTemplate.save(bbs);
				}
        	} else if (op == 2) {    	        
    	        mongoTemplate.remove(query,
    	        		favorite.class);
    	        bbs.setFavNum(bbs.getFavNum()-1);
    	        mongoTemplate.save(bbs);
        	}    
    	}    
        result.setOk();
        return result;
    }
    
    @ApiOperation("分页查询论坛帖子")
    @GetMapping("/list")
	public BBSListEntity list(
            HttpServletRequest request,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
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
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(new Criteria().orOperator(Criteria.where("title").regex(".*?\\" +key+ ".*"),Criteria.where("content").regex(".*?\\" +key+ ".*"),Criteria.where("authorName").regex(".*?\\" +key+ ".*")));
        } 
        if (status > -1) {
        	query.addCriteria(Criteria.where("status").is(status));  
        }
        if (istop > -1) {
        	if (istop == 1) {
        		query.addCriteria(Criteria.where("isTop").is(true));  
        	} else {
        		query.addCriteria(Criteria.where("isTop").is(false));  
        	}
        }
        query.addCriteria(Criteria.where("deleted").is(false));
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

    @ApiOperation("分页查询我收藏的帖子")
    @GetMapping("/listfav")
	public BBSListEntity listfav(
            HttpServletRequest request,
            @RequestParam(value="favUserId", required = true) String favUserId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "1") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "id") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	BBSListEntity result = new BBSListEntity();
    	List<favorite> lists = new ArrayList<favorite>();
        List<BBS> list = new ArrayList<BBS>();
        Query query = new Query();
        query.addCriteria(Criteria.where("favUserId").is(favUserId));
        query.addCriteria(Criteria.where("favType").is(2));
        try {
            if (paged == 1) {
            	PageRequest pageRequest = ParamUtils.buildPageRequest(pagenum,pagesize,sort,sortby);
                //构建分页信息
                long totalCount = mongoTemplate.count(query, favorite.class);
                //查询指定分页的内容
                lists = mongoTemplate.find(query.with(pageRequest),
                		favorite.class);
                long totalPage = (totalCount+pagesize-1)/pagesize;
                result.setTotalCount(totalCount);
                result.setTotalPage(totalPage);
                
            } else {
            	lists = mongoTemplate.find(query, favorite.class);
                result.setTotalCount(lists.size());
                result.setTotalPage(1);
            }
            for (favorite fav : lists) {
				if (fav != null && fav.getFavType() == 2) {
					BBS temp = bbsRepository.findById(fav.getFavId());
					list.add(temp);
				}
			}
            result.setOk();
            result.setList(list);
        } catch (Exception e) {
            log.info(request.getRemoteAddr()+"的用户请求api==>"+request.getRequestURL()+"抛出异常==>"+e.getMessage());
            result.setErr("-200", "00", e.getMessage());
        }
		return result;
	}
    
    @ApiOperation("分页查询论坛帖子评论--管理端")
    @GetMapping("/listcomment")
    @FarmAuth(validate = true)
	public CommentListEntity listcomment(
            HttpServletRequest request,
            @RequestParam(value="bbsId", required = false, defaultValue = "") String bbsId,
            @RequestParam(value="key", required = false, defaultValue = "") String key,
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
        if (bbsId!=null && bbsId.length()>0) {
        	query.addCriteria(Criteria.where("bbsId").is(bbsId));
        } 
        if (key!=null && key.length()>0) {        	
        	query.addCriteria(new Criteria().orOperator(Criteria.where("userName").regex(".*?\\" +key+ ".*"),Criteria.where("content").regex(".*?\\" +key+ ".*")));
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
    
    @ApiOperation("分页查询我评论过的论坛帖子")
    @GetMapping("/listcommentbyme")
    @FarmAuth(validate = true)
	public BBSListEntity listcommentbyme(
            HttpServletRequest request,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId,
            @RequestParam(value="pagenum", required = false, defaultValue = "1") int pagenum,
            @RequestParam(value="pagesize", required = false, defaultValue = "10") int pagesize,
            @RequestParam(value="sort", required = false, defaultValue = "2") int sort,
            @RequestParam(value="sortby", required = false, defaultValue = "createTime") String sortby,
            @RequestParam(value="paged", required = false, defaultValue = "0") int paged
    ) {
    	BBSListEntity result = new BBSListEntity();
        List<BBS> lists = new ArrayList<BBS>();
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
                	if (!ids.contains(temp.getBbsId())) {
	                	ids.add(temp.getBbsId());
	                	BBS tempbbs = bbsRepository.findById(temp.getBbsId());
	                	if (tempbbs != null) {
	                		lists.add(tempbbs);
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
                	if (!ids.contains(temp.getBbsId())) {
	                	ids.add(temp.getBbsId());
	                	BBS tempbbs = bbsRepository.findById(temp.getBbsId());
	                	if (tempbbs != null) {
	                		lists.add(tempbbs);
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
