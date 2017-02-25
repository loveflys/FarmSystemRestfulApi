package com.cay.Model.Favorite.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Favorite.vo.favorite;

public class favListEntity extends BaseEntity{
	private List<favorite> list;
	private long totalCount;
	private long totalPage;
	public List<favorite> getList() {
		return list;
	}

	public void setList(List<favorite> list) {
		this.list = list;
	}
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
}
