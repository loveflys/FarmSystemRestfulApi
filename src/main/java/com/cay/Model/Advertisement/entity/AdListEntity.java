package com.cay.Model.Advertisement.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Advertisement.vo.Advertisement;
/**
 * 广告
 * @author 陈安一
 *
 */
public class AdListEntity extends BaseEntity{
	private List<Advertisement> list;

	public List<Advertisement> getList() {
		return list;
	}

	public void setList(List<Advertisement> list) {
		this.list = list;
	}
}
