package com.cay.Model.Product.vo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cay.Model.Location.vo.Location;

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
	private List<String> classification;
	/**
	 * 分类名称
	 */
	private List<String> className;
	/**
	 * 区划
	 */
	private List<Long> areas;
	public List<Long> getAreas() {
		return areas;
	}
	public void setAreas(List<Long> areas) {
		this.areas = areas;
	}
	/**
	 * 产品名称
	 */
	private String proName;
	/**
	 * 产品图片
	 */
	private List<String> imgs;
	/**
	 * 产品价格(分)
	 */
	private long price;
	private long oldprice;
	private Location shopLocation;
	/**
	 * 库存
	 */
	private int stock;
	private String dis;
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
	private int fav;
	/**
	 * 删除时间
	 */
	private long deleteTime;
	/**
	 * 修改时间
	 */
	private long updateTime;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 所属市场
	 */
	private String marketid;
	private String marketPic;
	private String marketName;
	private String ownerName;
	/**
	 * 所属商户
	 */
	private String owner;
	private String ownerAvatar;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getClassification() {
		return classification;
	}
	public void setClassification(List<String> classification) {
		this.classification = classification;
	}
	public List<String> getClassName() {
		return className;
	}
	public void setClassName(List<String> className) {
		this.className = className;
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
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getOldprice() {
		return oldprice;
	}
	public void setOldprice(long oldprice) {
		this.oldprice = oldprice;
	}
	public String getMarketPic() {
		return marketPic;
	}
	public void setMarketPic(String marketPic) {
		this.marketPic = marketPic;
	}
	public Location getShopLocation() {
		return shopLocation;
	}
	public void setShopLocation(Location shopLocation) {
		this.shopLocation = shopLocation;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getDis() {
		return dis;
	}
	public void setDis(String dis) {
		this.dis = dis;
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
	public String getOwnerAvatar() {
		return ownerAvatar;
	}
	public void setOwnerAvatar(String ownerAvatar) {
		this.ownerAvatar = ownerAvatar;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getFav() {
		return fav;
	}
	public void setFav(int fav) {
		this.fav = fav;
	}
}
