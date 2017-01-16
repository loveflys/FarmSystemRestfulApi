package com.cay.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Division.vo.Division;

public interface DivisionRepository extends PagingAndSortingRepository<Division, Long> {
	Division findById(String id);
	
	List<Division> findByParentId(String parentId);
	
	List<Division> findByLevel(String level);
}
