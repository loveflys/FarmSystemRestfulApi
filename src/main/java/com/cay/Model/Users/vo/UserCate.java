package com.cay.Model.Users.vo;

import java.util.List;

public class UserCate {
	private List<Long> cate;
	private Long lastCate;
	public List<Long> getCate() {
		return cate;
	}
	public void setCate(List<Long> cate) {
		this.cate = cate;
	}
	public Long getLastCate() {
		return lastCate;
	}
	public void setLastCate(Long lastCate) {
		this.lastCate = lastCate;
	}
}
