package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Favorite.vo.favorite;

@Repository
public interface FavoriteService {
	
	void save(favorite favorite);
	
	favorite findById(String id);
}
