package com.cay.Model.Recipes.vo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 食谱
 * @author 陈安一
 *
 */
@Document(collection = "recipes")
public class Recipes {
	@Id
	private String id;
	private String title;
	private String author;
	private String authorName;
	private int fav;
	private long favNum;
	private List<String> imgs;
	/**
	 * 食材用料
	 */
	private List<Material> materials;
	/**
	 * 做法
	 */
	private String method;
	/**
	 * 权重
	 */
	private int weight;
	/**
	 * 点击量/浏览量
	 */
	private long viewNum;
	/**
	 * 收藏数量
	 */
	private long collectNum;
	/**
	 * 状态
	 * 0-待审核
	 * 1-审核通过
	 * 2-审核不通过
	 */
	private int status;
	/**
	 * 审核失败原因
	 */
	private String reason;
	/**
	 * 审核时间
	 */
	private long verifyTime;
	/**
	 * 删除时间
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
	 * 修改时间
	 */
	private long updateTime;
	
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public List<Material> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getFav() {
		return fav;
	}
	public void setFav(int fav) {
		this.fav = fav;
	}
	public long getFavNum() {
		return favNum;
	}
	public void setFavNum(long favNum) {
		this.favNum = favNum;
	}
	public long getViewNum() {
		return viewNum;
	}
	public void setViewNum(long viewNum) {
		this.viewNum = viewNum;
	}
	public long getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(long collectNum) {
		this.collectNum = collectNum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public long getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(long verifyTime) {
		this.verifyTime = verifyTime;
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
}
