package com.cay.Model.Product.entity;

import java.util.List;

import com.cay.Model.BaseEntity;
import com.cay.Model.Product.vo.Product;
/**
 * 商品列表
 * @author 陈安一
 *
 */
public class ProductListEntity  extends BaseEntity{
	private List<Product> list;
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

	public List<Product> getList() {
		return list;
	}

	public void setList(List<Product> list) {
		this.list = list;
	}
}
