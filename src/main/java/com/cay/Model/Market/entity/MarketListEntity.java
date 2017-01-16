package com.cay.Model.Market.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Market.vo.Market;
/**
 * 市场列表
 * @author 陈安一
 *
 */
public class MarketListEntity extends BaseEntity{
	private List<Market> list;
	private long totalCount;
	private long totalPage;
	public List<Market> getList() {
		return list;
	}
	public void setList(List<Market> list) {
		this.list = list;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
}
