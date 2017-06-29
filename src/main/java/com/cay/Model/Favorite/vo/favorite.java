package com.cay.Model.Favorite.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cay.Model.BBS.vo.BBS;
import com.cay.Model.Location.vo.Location;
import com.cay.Model.Product.vo.Product;
import com.cay.Model.Recipes.vo.Recipes;

/**
 * 收藏
 * @author 陈安一
 *
 */
@Document(collection = "favorite")
public class favorite {
	@Id
	private String id;
	private String favId;
	private String favUserId;
	/*
	 * 收藏类型
	 * 1-商品
	 * 2-帖子
	 * 3-菜谱
	 */
	private int favType;
	private long favTime;
	private String dis;
	private Product pro;
	private BBS bbs;
	private Recipes recipes;
	private Location shopLocation;
	public Location getShopLocation() {
		return shopLocation;
	}
	public void setShopLocation(Location shopLocation) {
		this.shopLocation = shopLocation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFavId() {
		return favId;
	}
	public void setFavId(String favId) {
		this.favId = favId;
	}
	public String getDis() {
		return dis;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public String getFavUserId() {
		return favUserId;
	}
	public void setFavUserId(String favUserId) {
		this.favUserId = favUserId;
	}
	public int getFavType() {
		return favType;
	}
	public void setFavType(int favType) {
		this.favType = favType;
	}
	public long getFavTime() {
		return favTime;
	}
	public void setFavTime(long favTime) {
		this.favTime = favTime;
	}
	public Product getPro() {
		return pro;
	}
	public void setPro(Product pro) {
		this.pro = pro;
	}
	public BBS getBbs() {
		return bbs;
	}
	public void setBbs(BBS bbs) {
		this.bbs = bbs;
	}
	public Recipes getRecipes() {
		return recipes;
	}
	public void setRecipes(Recipes recipes) {
		this.recipes = recipes;
	}
}
