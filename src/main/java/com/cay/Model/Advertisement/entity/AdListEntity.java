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
	private long totalCount;
	private long totalPage;
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
	public List<Advertisement> getList() {
		return list;
	}

	public void setList(List<Advertisement> list) {
		this.list = list;
	}
}
