package com.cay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cay.Model.Classification.vo.Classification;
import com.cay.Model.Product.vo.Product;

@Service
public class ProductServiceImpl {
	@Autowired
    private com.cay.repository.ProductRepository productRepository;
	
	public void save(Product product) {
		productRepository.save(product);
    }
	
	public Product findById(String id) {
		return this.productRepository.findById(id);		
	};
	
	public List<Product> findByProName(String proName) {
		return this.productRepository.findByProName(proName);		
	};
	
	public List<Product> findByWeight(int weight) {
		return this.productRepository.findByWeight(weight);
	};
	
	public List<Product> findByMarketid(String marketid) {
		return this.productRepository.findByMarketid(marketid);
	};
	
	public List<Product> findByOwner(String owner) {
		return this.productRepository.findByOwner(owner);
	};
	
	public List<Product> findByClassification(String classification) {
		return this.productRepository.findByClassification(classification);
	};
}
