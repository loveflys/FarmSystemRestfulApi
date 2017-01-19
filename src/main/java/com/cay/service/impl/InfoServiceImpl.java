package com.cay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Info.vo.Info;

@Service
public class InfoServiceImpl {
	@Autowired
    private com.cay.repository.InfoRepository infoRepository;
	
	public void save(Info info) {
		infoRepository.save(info);
    }
	
	public Info findById(String id) {
		return this.infoRepository.findById(id);		
	};
	
	public List<Info> findByType(int type) {
		return this.infoRepository.findByType(type);		
	};
	
	public List<Info> findByWeight(int weight) {
		return this.infoRepository.findByWeight(weight);
	};
}
