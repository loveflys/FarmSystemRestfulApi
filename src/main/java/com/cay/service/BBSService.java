package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.BBS.vo.BBS;

@Repository
public interface BBSService {
	
	void save(BBS bbs);
	
	BBS findById(String id);
}
