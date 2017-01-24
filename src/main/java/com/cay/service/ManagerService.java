package com.cay.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cay.Model.Advertisement.vo.Advertisement;
import com.cay.Model.Manager.vo.Manager;

@Repository
public interface ManagerService extends PagingAndSortingRepository<Advertisement, Long> {
	
	void save(Manager manager);
	
	Manager findById(String id);
	
	Manager findByLogin(String login);
}
