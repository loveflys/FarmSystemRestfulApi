package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Suggestion.vo.Suggestion;

@Service
public class SuggestionServiceImpl {
	@Autowired
    private com.cay.repository.SuggestionRepository suggestionRepository;
	
	public void save(Suggestion suggestion) {
		suggestionRepository.save(suggestion);
    }
	
	public Suggestion findById(String id) {
		return this.suggestionRepository.findById(id);
	};
}
