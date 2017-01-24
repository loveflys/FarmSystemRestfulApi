package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Manager.vo.Manager;

@Service
public class ManagerServiceImpl {
	@Autowired
    private com.cay.repository.ManagerRepository managerRepository;
	
	public void save(Manager manager) {
		managerRepository.save(manager);
    }
	
	public Manager findById(String id) {
		return this.managerRepository.findById(id);
	};
	
	public Manager findByLogin(String login) {
		return this.managerRepository.findByLogin(login);
	};
}
