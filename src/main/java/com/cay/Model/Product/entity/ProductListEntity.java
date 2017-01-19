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

	public List<Product> getList() {
		return list;
	}

	public void setList(List<Product> list) {
		this.list = list;
	}
}