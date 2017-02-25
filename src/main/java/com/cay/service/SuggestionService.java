package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Suggestion.vo.Suggestion;

@Repository
public interface SuggestionService {
	
	void save(Suggestion suggestion);
	
	Suggestion findById(String id);
}
