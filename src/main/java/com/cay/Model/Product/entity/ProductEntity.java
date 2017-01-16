package com.cay.Model.Product.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Product.vo.Product;

public class ProductEntity extends BaseEntity{
	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
