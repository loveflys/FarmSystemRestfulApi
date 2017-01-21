package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.Info.vo.Comment;

@Repository
public interface InfoComentService {
	
	void save(Comment comment);
	
	Comment findById(String id);
}
