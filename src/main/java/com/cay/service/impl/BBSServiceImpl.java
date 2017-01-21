package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.BBS.vo.BBS;

@Service
public class BBSServiceImpl {
	@Autowired
    private com.cay.repository.BBSRepository bbsRepository;
	
	public void save(BBS bbs) {
		bbsRepository.save(bbs);
    }
	
	public BBS findById(String id) {
		return this.bbsRepository.findById(id);
	};
}
