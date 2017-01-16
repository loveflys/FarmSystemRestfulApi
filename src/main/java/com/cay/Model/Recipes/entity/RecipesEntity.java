package com.cay.Model.Recipes.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Recipes.vo.Recipes;
/**
 * 食谱
 * @author 陈安一
 *
 */
public class RecipesEntity extends BaseEntity{
	private Recipes recipes;

	public Recipes getRecipes() {
		return recipes;
	}

	public void setRecipes(Recipes recipes) {
		this.recipes = recipes;
	}
}
