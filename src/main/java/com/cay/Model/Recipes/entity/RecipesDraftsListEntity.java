package com.cay.Model.Recipes.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Recipes.vo.RecipesDrafts;
/**
 * 食谱草稿箱列表
 * @author 陈安一
 *
 */
public class RecipesDraftsListEntity  extends BaseEntity{
	private List<RecipesDrafts> list;
	private long totalCount;
	private long totalPage;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	public List<RecipesDrafts> getList() {
		return list;
	}

	public void setList(List<RecipesDrafts> list) {
		this.list = list;
	}
}
