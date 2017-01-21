package com.cay.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cay.Model.Advertisement.vo.Advertisement;

@Repository
public interface AdService {
	
	void save(Advertisement advertisement);
	
	Advertisement findById(String id);
	
	List<Advertisement> findByType(int type);
	
	List<Advertisement> findByResponseType(int responseType);
	
	List<Advertisement> findByPushed(Boolean pushed);
	
	List<Advertisement> findByShowType(int showType);
}
