package com.cay.Model.Users.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Users.vo.User;

public class UserEntity extends BaseEntity {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
