package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.Info.vo.Comment;

@Service
public class InfoCommentServiceImpl {
	@Autowired
    private com.cay.repository.InfoCommentRepository infocommentRepository;
	
	public void save(Comment comment) {
		infocommentRepository.save(comment);
    }
	
	public Comment findById(String id) {
		return this.infocommentRepository.findById(id);
	};
}
