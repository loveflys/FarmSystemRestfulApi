package com.cay.Model.Favorite.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Favorite.vo.favorite;

public class favEntity extends BaseEntity{
	private favorite fav;

	public favorite getFav() {
		return fav;
	}

	public void setFav(favorite fav) {
		this.fav = fav;
	}
}
