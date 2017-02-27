package com.cay.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cay.Model.Classification.vo.Classification;

@Repository
public interface ClassService {
	void save(Classification classification);
	
	Classification findById(String id);
	
	List<Classification> findByParentId(String parentId);
	
	List<Classification> findByLevel(String level);
	
	Classification findByCode(String code);
}
