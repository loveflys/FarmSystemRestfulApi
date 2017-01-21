package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Recipes.vo.Recipes;

@Service
public class RecipesServiceImpl {
	@Autowired
    private com.cay.repository.RecipesRepository recipesRepository;
	
	public void save(Recipes recipes) {
		recipesRepository.save(recipes);
    }
	
	public Recipes findById(String id) {
		return this.recipesRepository.findById(id);		
	};
}
