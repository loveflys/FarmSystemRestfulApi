package com.cay.Model.Manager.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Manager.vo.Manager;

public class ManagerListEntity extends BaseEntity{
	
	private List<Manager> lists;
	
	private long totalCount;
	
	private long totalPage;
	

	public List<Manager> getLists() {
		return lists;
	}

	public void setLists(List<Manager> lists) {
		this.lists = lists;
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
