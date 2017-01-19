package com.cay.Model.Info.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 信息
 * @author 陈安一
 *
 */
@Document(collection = "info")
public class Info {
	@Id
	private String id;
	/**
	 * 类型：
	 * 1-普通消息
	 * 2-美食消息
	 */
	private int type;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 主图
	 */
	private String mainImg;
	/**
	 * 作者姓名
	 */
	private String authorName;
	/**
	 * 作者id
	 */
	private String authorId;
	/**
	 * 信息内容
	 */
	private String content;
	/**
	 * 是否删除
	 */
	private Boolean deleted;
	/**
	 * 创建/发布时间
	 */
	private long CreateTime;
	/**
	 * 修改时间
	 */
	private long UpdateTime;
	/**
	 * 删除时间
	 */
	private long DeleteTime;
	/**
	 * 评论量
	 */
	private long commentNum;
	/**
	 * 浏览量/点击量
	 */
	private long viewNum;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public long getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(long updateTime) {
		UpdateTime = updateTime;
	}
	public long getDeleteTime() {
		return DeleteTime;
	}
	public void setDeleteTime(long deleteTime) {
		DeleteTime = deleteTime;
	}
	public long getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}
	public long getViewNum() {
		return viewNum;
	}
	public void setViewNum(long viewNum) {
		this.viewNum = viewNum;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
