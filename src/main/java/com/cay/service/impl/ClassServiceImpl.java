package com.cay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cay.Model.Classification.vo.Classification;

@Service
public class ClassServiceImpl {
	@Autowired
    private com.cay.repository.ClassRepository classRepository;
	
	public void save(Classification classification) {
		classRepository.save(classification);
    }
	
	public Classification findById(String id) {
		return this.classRepository.findById(id);		
	};
	
	public List<Classification> findByParentId(String parentId) {
		return this.classRepository.findByParentId(parentId);		
	};
	
	public List<Classification> findByLevel(String level) {
		return this.classRepository.findByLevel(level);
	};
	
	public Classification findByCode(long code) {
		return this.classRepository.findByCode(code);
	};
}
