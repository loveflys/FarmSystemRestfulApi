package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Recipes.vo.Recipes;

@Repository
public interface RecipesService {
	
	void save(Recipes recipes);
	
	Recipes findById(String id);
}
