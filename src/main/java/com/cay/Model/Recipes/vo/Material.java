package com.cay.Model.Recipes.vo;

/**
 * 食谱用料
 * @author 陈安一
 *
 */
public class Material {
	/**
	 * 分类id
	 */
	private String id;
	/**
	 * 材料（分类）名称
	 */
	private String name;
	/**
	 * 用料/用量
	 */
	private String dosage;
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
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public Material() {
		super();
	}
	public Material (String name, String dosage) {
		this.name = name;
		this.dosage = dosage;
	}
}
