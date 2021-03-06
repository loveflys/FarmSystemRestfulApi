package com.cay.Model.BBS.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.vo.BBS;

public class BBSListEntity extends BaseEntity{
	private List<BBS> list;

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
	
	public List<BBS> getList() {
		return list;
	}

	public void setList(List<BBS> list) {
		this.list = list;
	}
	
}
