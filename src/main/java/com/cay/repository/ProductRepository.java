package com.cay.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Product.vo.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	Product findById(String id);
	
	List<Product> findByPro_name(String pro_name);
	
	List<Product> findByWeight(int weight);
	
	List<Product> findByMarketid(String marketid);
	
	List<Product> findByOwner(String owner);
	
	List<Product> findByClassification(String classification);
}
