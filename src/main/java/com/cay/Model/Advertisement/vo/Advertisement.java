package com.cay.Model.Advertisement.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 广告
 * @author 陈安一
 *
 */
@Document(collection = "advertisement")
public class Advertisement {
	@Id
	private String id;
	/**
	 * 广告标题，仅对管理端展示
	 */
	private String title;
	/**
	 * 广告类型
	 * 1-图片
	 * 2-文字
	 */
	private int type;
	/**
	 * 响应类型
	 * 1-无
	 * 2-超链接
	 * 3-click
	 */
	private int responseType;
	private String img;
	private String content;
	private String url;
	/**
	 * 是否删除
	 */
	private Boolean deleted;
	/**
	 * 删除时间
	 */
	private long deleteTime;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 是否推送至首页
	 */
	private Boolean pushed;
	/**
	 * 推送时间
	 */
	private long pushTime;
	/**
	 * 展示方式
	 * 1-永久展示
	 * 2-时间区间
	 */
	private int showType;
	/**
	 * 展示开始时间
	 */
	private long showStartTime;
	/**
	 * 展示结束时间
	 */
	private long showEndTime;
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
	public int getType() {
		return type;
	}
	/**
	 * 广告类型
	 * 1-图片
	 * 2-文字
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 响应类型
	 * 1-无
	 * 2-超链接
	 * 3-click
	 */
	public int getResponseType() {
		return responseType;
	}
	/**
	 * 响应类型
	 * 1-无
	 * 2-超链接
	 * 3-click
	 */
	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public long getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(long deleteTime) {
		this.deleteTime = deleteTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public Boolean getPushed() {
		return pushed;
	}
	public void setPushed(Boolean pushed) {
		this.pushed = pushed;
	}
	public long getPushTime() {
		return pushTime;
	}
	public void setPushTime(long pushTime) {
		this.pushTime = pushTime;
	}
	public int getShowType() {
		return showType;
	}
	/**
	 * 展示方式
	 * 1-永久展示
	 * 2-时间区间
	 */
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public long getShowStartTime() {
		return showStartTime;
	}
	public void setShowStartTime(long showStartTime) {
		this.showStartTime = showStartTime;
	}
	public long getShowEndTime() {
		return showEndTime;
	}
	public void setShowEndTime(long showEndTime) {
		this.showEndTime = showEndTime;
	}
}
