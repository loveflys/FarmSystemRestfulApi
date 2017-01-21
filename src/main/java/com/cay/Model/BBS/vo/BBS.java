package com.cay.Model.BBS.vo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 论坛帖子
 * @author 陈安一
 *
 */
@Document(collection = "bbs")
public class BBS {
	@Id
	private String id;
	private String title;
	private String authorId;
	private String authorName;
	private String content;
	private List<String> imgs;
	private long createTime;
	private long updateTime;
	private Boolean deleted;
	private long DeleteTime;
	private List<Comment> comments;
	/** 
	 * 是否置顶 
	 */
	private Boolean isTop;
	/**
	 * 0-待审核
	 * 1-审核通过
	 * 2-审核拒绝
	 */
	private int status;
	/**
	 * 审核时间
	 */
	private long verifyTime;
	/**
	 * 拒绝原因
	 */
	private String reason;
	/**
	 * 点击量/浏览量
	 */
	private long viewNum;
	/**
	 * 评论量
	 */
	private long commentNum;
	/**
	 * 收藏量
	 */
	private long favNum;
	/**
	 * 权重
	 */
	private int weight;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public long getDeleteTime() {
		return DeleteTime;
	}
	public void setDeleteTime(long deleteTime) {
		DeleteTime = deleteTime;
	}
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(long verifyTime) {
		this.verifyTime = verifyTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public long getViewNum() {
		return viewNum;
	}
	public void setViewNum(long viewNum) {
		this.viewNum = viewNum;
	}
	public long getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}
	public long getFavNum() {
		return favNum;
	}
	public void setFavNum(long favNum) {
		this.favNum = favNum;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
