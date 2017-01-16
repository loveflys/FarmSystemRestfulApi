package com.cay.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Classification.vo.Classification;

public interface ClassRepository extends PagingAndSortingRepository<Classification, Long> {
	Classification findById(String id);
	
	List<Classification> findByParentId(String parentId);
	
	List<Classification> findByLevel(String level);
}
