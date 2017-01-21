package com.cay.Model.BBS.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.BBS.vo.BBS;

public class BBSListEntity extends BaseEntity{
	private List<BBS> list;

	public List<BBS> getList() {
		return list;
	}

	public void setList(List<BBS> list) {
		this.list = list;
	}
	
}
