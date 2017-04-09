package com.cay.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.cay.Model.Division.vo.Division;

@Repository
public interface DivisionService {
	void save(Division division);
	
	Division findById(String id);
	
	List<Division> findByParentId(long parentId);
	
	List<Division> findByLevel(int level);
	
	Division findByDivisionCode(long divisionCode);
}
