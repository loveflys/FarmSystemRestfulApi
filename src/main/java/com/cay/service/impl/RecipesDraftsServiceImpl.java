package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Recipes.vo.RecipesDrafts;

@Service
public class RecipesDraftsServiceImpl {
	@Autowired
    private com.cay.repository.RecipesDraftsRepository recipesdraftsRepository;
	
	public void save(RecipesDrafts recipes) {
		recipesdraftsRepository.save(recipes);
    }
	
	public RecipesDrafts findById(String id) {
		return this.recipesdraftsRepository.findById(id);		
	};
}
