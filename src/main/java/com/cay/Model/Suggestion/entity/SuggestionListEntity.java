package com.cay.Model.Suggestion.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Suggestion.vo.Suggestion;

public class SuggestionListEntity extends BaseEntity{
	private List<Suggestion> list;
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
	public List<Suggestion> getList() {
		return list;
	}

	public void setList(List<Suggestion> list) {
		this.list = list;
	}
}
