package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Market.vo.Market;

@Service
public class MarketServiceImpl {
	@Autowired
    private com.cay.repository.MarketRepository marketRepository;
	
	public void save(Market market) {
		marketRepository.save(market);
    }
	
	public Market findById(String id) {
		return this.marketRepository.findById(id);		
	};
}
