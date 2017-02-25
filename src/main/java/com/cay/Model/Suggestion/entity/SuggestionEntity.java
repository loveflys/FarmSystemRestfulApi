package com.cay.Model.Suggestion.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Suggestion.vo.Suggestion;

public class SuggestionEntity extends BaseEntity{
	private Suggestion suggestion;

	public Suggestion getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}
}
