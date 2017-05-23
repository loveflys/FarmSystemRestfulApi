package com.cay.Model.Recipes.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 食谱-步骤
 * @author 陈安一
 *
 */
@Document(collection = "step")
public class Step {
	@Id
	private String id;
	private String step_title;
	private String step_img;
	private String step_descr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStep_title() {
		return step_title;
	}
	public void setStep_title(String step_title) {
		this.step_title = step_title;
	}
	public String getStep_img() {
		return step_img;
	}
	public void setStep_img(String step_img) {
		this.step_img = step_img;
	}
	public String getStep_descr() {
		return step_descr;
	}
	public void setStep_descr(String step_descr) {
		this.step_descr = step_descr;
	}
}
