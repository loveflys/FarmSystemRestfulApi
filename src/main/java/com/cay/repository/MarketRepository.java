package com.cay.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Market.vo.Market;

import java.util.List;

/**
 * 市场仓库
 * @author 陈安一
 *
 */
public interface MarketRepository extends PagingAndSortingRepository<Market, Long> {
	Market findById(String id);

	List<Market> findByLocationNear(Point p, Distance d, PageRequest pageRequest);

	List<Market> findByLocationWithin(Box b, PageRequest pageRequest);
}
