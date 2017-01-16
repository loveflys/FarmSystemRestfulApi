package com.cay.Model.Market.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Market.vo.Market;

public class MarketListEntity extends BaseEntity{
	private List<Market> list;

	public List<Market> getList() {
		return list;
	}

	public void setList(List<Market> list) {
		this.list = list;
	}
}
