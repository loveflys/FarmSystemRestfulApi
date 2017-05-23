package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Recipes.vo.RecipesDrafts;

@Repository
public interface RecipesDraftsService {
	
	void save(RecipesDrafts recipesdrafts);
	
	RecipesDrafts findById(String id);
}
