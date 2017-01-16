package com.cay.Model.Traders.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Traders.vo.Trader;

public class TradersListEntity extends BaseEntity{
	private List<Trader> list;

	public List<Trader> getList() {
		return list;
	}

	public void setList(List<Trader> list) {
		this.list = list;
	}
}
