package com.cay.Model.Users.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Users.vo.User;
/**
 * 用户列表
 * @author 陈安一
 *
 */
public class UserListEntity extends BaseEntity{
	private List<User> users;
	private long totalCount;
	private long totalPage;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
