package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Market.vo.Market;

public interface MarketRepository extends PagingAndSortingRepository<Market, Long> {
	Market findById(String id);
}
