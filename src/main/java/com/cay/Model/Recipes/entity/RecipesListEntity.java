package com.cay.Model.Recipes.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Recipes.vo.Recipes;
/**
 * 食谱列表
 * @author 陈安一
 *
 */
public class RecipesListEntity  extends BaseEntity{
	private List<Recipes> list;

	public List<Recipes> getList() {
		return list;
	}

	public void setList(List<Recipes> list) {
		this.list = list;
	}
}
