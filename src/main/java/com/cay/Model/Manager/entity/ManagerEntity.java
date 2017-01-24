package com.cay.Model.Manager.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Manager.vo.Manager;

public class ManagerEntity extends BaseEntity{
	private Manager manager;

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
}
