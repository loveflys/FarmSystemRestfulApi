package com.cay.Model.Classification.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Classification.vo.Classification;

public class ClassEntity extends BaseEntity{
	private Classification result;

	public Classification getResult() {
		return result;
	}

	public void setResult(Classification result) {
		this.result = result;
	}
}
