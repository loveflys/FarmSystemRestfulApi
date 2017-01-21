package com.cay.Model.BBS.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.vo.Comment;

public class CommentListEntity extends BaseEntity{
	private List<Comment> list;

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
	
	public List<Comment> getList() {
		return list;
	}

	public void setList(List<Comment> list) {
		this.list = list;
	}
	
}
