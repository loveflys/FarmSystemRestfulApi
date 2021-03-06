package com.cay.Model.Classification.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.vo.Classification;
/**
 * 分类列表
 * @author 陈安一
 *
 */
public class ClassListEntity extends BaseEntity {
	private List<Classification> list;
	private long totalCount;
	private long totalPage;
	
	public List<Classification> getList() {
		return list;
	}

	public void setList(List<Classification> list) {
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
