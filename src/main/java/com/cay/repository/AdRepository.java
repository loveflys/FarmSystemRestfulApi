package com.cay.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Advertisement.vo.Advertisement;
/**
 * 广告仓库
 * @author 陈安一
 *
 */
public interface AdRepository extends PagingAndSortingRepository<Advertisement, Long> {
	Advertisement findById(String id);
	
	List<Advertisement> findByType(int type);
	
	List<Advertisement> findByResponseType(int responseType);
	
	List<Advertisement> findByPushed(Boolean pushed);
	
	List<Advertisement> findByShowType(int showType);
}
