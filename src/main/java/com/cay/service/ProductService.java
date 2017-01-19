package com.cay.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.cay.Model.Product.vo.Product;

@Repository
public interface ProductService {
	void save(Product info);
	
	Product findById(String id);
	
	List<Product> findByProName(String proName);
	
	List<Product> findByWeight(int weight);
	
	List<Product> findByMarketid(String marketid);
	
	List<Product> findByOwner(String owner);
	
	List<Product> findByClassification(String classification);
}
