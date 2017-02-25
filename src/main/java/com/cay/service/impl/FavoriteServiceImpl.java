package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Favorite.vo.favorite;

@Service
public class FavoriteServiceImpl {
	@Autowired
    private com.cay.repository.FavoriteRepository favoriteRepository;
	
	public void save(favorite favorite) {
		favoriteRepository.save(favorite);
    }
	
	public favorite findById(String id) {
		return this.favoriteRepository.findById(id);
	};
}
