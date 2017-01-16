package com.cay.Model.Traders.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Traders.vo.Trader;

public class TradersEntity extends BaseEntity{
	private Trader trader;

	public Trader getTrader() {
		return trader;
	}

	public void setTrader(Trader trader) {
		this.trader = trader;
	}
}
