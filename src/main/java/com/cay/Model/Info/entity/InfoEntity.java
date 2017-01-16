package com.cay.Model.Info.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Info.vo.Info;

/**
 * 信息
 * @author 陈安一
 *
 */
public class InfoEntity extends BaseEntity{
	private Info info;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
}
