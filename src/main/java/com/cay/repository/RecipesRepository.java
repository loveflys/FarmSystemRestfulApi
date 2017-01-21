package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Recipes.vo.Recipes;
/**
 * 食谱仓库
 * @author 陈安一
 *
 */
public interface RecipesRepository extends PagingAndSortingRepository<Recipes, Long> {
	Recipes findById(String id);
}
