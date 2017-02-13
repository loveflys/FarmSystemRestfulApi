package com.cay.Model.Recipes.vo;

/**
 * 食谱用料
 * @author 陈安一
 *
 */
public class Material {
	/**
	 * 材料名称
	 */
	private String name;
	/**
	 * 用料/用量
	 */
	private String dosage;
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
