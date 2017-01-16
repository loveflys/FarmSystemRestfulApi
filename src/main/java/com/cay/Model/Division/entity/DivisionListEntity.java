package com.cay.Model.Division.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Division.vo.Division;
/**
 * 区划列表
 * @author 陈安一
 *
 */
public class DivisionListEntity extends BaseEntity {
	private List<Division> list;

	public List<Division> getList() {
		return list;
	}

	public void setList(List<Division> list) {
		this.list = list;
	}
}
