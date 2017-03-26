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
	private String classInfo;
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
	public String getClassInfo() {
		return classInfo;
	}
	public void setClassInfo(String classInfo) {
		this.classInfo = classInfo;
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
	public Material (String classInfo, String dosage) {
		this.classInfo = classInfo;
		this.dosage = dosage;
	}
}
