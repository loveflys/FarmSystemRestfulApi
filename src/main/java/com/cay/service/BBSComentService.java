package com.cay.service;

import org.springframework.stereotype.Repository;
import com.cay.Model.BBS.vo.Comment;

@Repository
public interface BBSComentService {
	
	void save(Comment comment);
	
	Comment findById(String id);
}
