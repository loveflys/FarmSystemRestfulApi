package com.cay.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cay.Model.Info.vo.Info;

@Repository
public interface InfoService {
	void save(Info info);
	
	Info findById(String id);
	
	List<Info> findByType(int type);
	
	List<Info> findByWeight(int weight);
}
