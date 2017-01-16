package com.cay.Model.Classification.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "classification")
public class Classification {
	@Id
	private String id;
	private String name;
	private String descr;
	/**
	 * 营养价值（文本）
	 */
	private String nutrition;
	private String parentId;
	/**
	 * 分类等级
	 */
	private int level;	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getNutrition() {
		return nutrition;
	}
	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
