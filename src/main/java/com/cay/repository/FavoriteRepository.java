package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Favorite.vo.favorite;
/**
 * 收藏
 * @author 陈安一
 *
 */
public interface FavoriteRepository extends PagingAndSortingRepository<favorite, Long> {
	favorite findById(String id);
}
