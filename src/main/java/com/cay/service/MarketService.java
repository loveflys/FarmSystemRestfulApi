package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Market.vo.Market;

@Repository
public interface MarketService {
	void save(Market classification);
	
	Market findById(String id);
}
