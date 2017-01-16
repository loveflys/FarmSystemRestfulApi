package com.cay.Model.Market.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Market.vo.Market;
/**
 * 市场
 * @author 陈安一
 *
 */
public class MarketEntity extends BaseEntity{
	private Market market;

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}
}
