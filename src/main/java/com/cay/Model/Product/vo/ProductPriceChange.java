package com.cay.Model.Product.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productpricechange")
public class ProductPriceChange {
	@Id
	private String id;
	private String productId;
	private String productName;
	private long changeTime;
	private long productAmount;
	/**
	 * 操作类型：
	 * 1： 创建
	 * 2： 修改
	 */
	private int type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
	}
	public long getProductAmount() {
		return productAmount;
	}
	public void setProductAmount(long productAmount) {
		this.productAmount = productAmount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
