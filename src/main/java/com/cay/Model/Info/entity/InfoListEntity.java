package com.cay.Model.Info.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Info.vo.Info;
/**
 * 信息列表
 * @author 陈安一
 *
 */
public class InfoListEntity extends BaseEntity{
	private List<Info> list;

	public List<Info> getList() {
		return list;
	}

	public void setList(List<Info> list) {
		this.list = list;
	}
}
