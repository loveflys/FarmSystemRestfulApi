package com.cay.Model.Recipes.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Recipes.vo.RecipesDrafts;
/**
 * 食谱
 * @author 陈安一
 *
 */
public class RecipesDraftsEntity extends BaseEntity{
	private RecipesDrafts recipesdrafts;

	public RecipesDrafts getRecipesDrafts() {
		return recipesdrafts;
	}

	public void setRecipesDrafts(RecipesDrafts recipesdrafts) {
		this.recipesdrafts = recipesdrafts;
	}
}
