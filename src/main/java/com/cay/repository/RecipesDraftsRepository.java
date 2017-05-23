package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Recipes.vo.RecipesDrafts;
/**
 * 食谱草稿仓库
 * @author 陈安一
 *
 */
public interface RecipesDraftsRepository extends PagingAndSortingRepository<RecipesDrafts, Long> {
	RecipesDrafts findById(String id);
}
