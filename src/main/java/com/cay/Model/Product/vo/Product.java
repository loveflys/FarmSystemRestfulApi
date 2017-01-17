package com.cay.Model.Product.vo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 商品
 * @author 陈安一
 *
 */
@Document(collection = "product")
public class Product {
	@Id
	private String id;
	/**
	 * 分类id
	 */
	private String classification;
	/**
	 * 产品名称
	 */
	private String proName;
	/**
	 * 产品图片
	 */
	private List<String> imgs;
	/**
	 * 产品价格
	 */
	private BigDecimal price;
	private BigDecimal oldprice;
	/**
	 * 库存
	 */
	private int stock;
	/**
	 * 权重
	 */
	private int weight;
	/**
	 * 是否下架
	 */
	private Boolean is_off_shelve;
	/**
	 * 下架时间
	 */
	private long offshelveTime;
	/**
	 * 是否删除
	 */
	private Boolean deleted;
	/**
	 * 收藏数量
	 */
	private long favNum;
	/**
	 * 删除时间
	 */
	private long deleteTime;
	/**
	 * 所属市场
	 */
	private String marketid;
	/**
	 * 所属商户
	 */
	private String owner;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getOldprice() {
		return oldprice;
	}
	public void setOldprice(BigDecimal oldprice) {
		this.oldprice = oldprice;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
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
	public Boolean getIs_off_shelve() {
		return is_off_shelve;
	}
	public void setIs_off_shelve(Boolean is_off_shelve) {
		this.is_off_shelve = is_off_shelve;
	}
	public long getOffshelveTime() {
		return offshelveTime;
	}
	public void setOffshelveTime(long offshelveTime) {
		this.offshelveTime = offshelveTime;
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
	public String getMarketid() {
		return marketid;
	}
	public void setMarketid(String marketid) {
		this.marketid = marketid;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
