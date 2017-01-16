package com.cay.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Info.vo.Info;

public interface InfoRepository extends PagingAndSortingRepository<Info, Long> {
	Info findById(String id);
	
	List<Info> findByType(int type);
	
	List<Info> findByWeight(int weight);
}
