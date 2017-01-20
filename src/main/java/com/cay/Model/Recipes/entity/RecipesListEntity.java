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
	public List<Recipes> getList() {
		return list;
	}

	public void setList(List<Recipes> list) {
		this.list = list;
	}
}
