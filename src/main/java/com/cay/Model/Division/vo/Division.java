package com.cay.Model.Division.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 区划
 * @author 陈安一
 *
 */
@Document(collection = "division")
public class Division {
	@Id
	private String id;
	/**
	 * 区划编码
	 */
	private int divisionCode;
	private String completeName;
	private String name;
	/**
	 * 省份的父级为0
	 */
	private int parentId;
	/**
	 * 1-省份
	 * 2-市
	 * 3-区/县
	 */
	private int level;	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompleteName() {
		return completeName;
	}
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDivisionCode() {
		return divisionCode;
	}
	public void setDivisionCode(int divisionCode) {
		this.divisionCode = divisionCode;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
