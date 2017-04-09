package com.cay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cay.Model.Division.vo.Division;

@Service
public class DivisionServiceImpl {
	@Autowired
    private com.cay.repository.DivisionRepository divisionRepository;
	
	public void save(Division division) {
		divisionRepository.save(division);
    }
	
	public Division findById(String id) {
		return this.divisionRepository.findById(id);		
	};
	
	public List<Division> findByParentId(long parentId) {
		return this.divisionRepository.findByParentId(parentId);		
	};
	
	public List<Division> findByLevel(int level) {
		return this.divisionRepository.findByLevel(level);
	};
	
	public Division findByDivisionCode(long divisionCode) {
		return this.divisionRepository.findByDivisionCode(divisionCode);
	};
}
