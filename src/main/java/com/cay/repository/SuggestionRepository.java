package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Suggestion.vo.Suggestion;
/**
 * 意见/建议
 * @author 陈安一
 *
 */
public interface SuggestionRepository extends PagingAndSortingRepository<Suggestion, Long> {
	Suggestion findById(String id);
}
