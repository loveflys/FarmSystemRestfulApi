package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cay.Model.BBS.vo.Comment;

@Service
public class BBSCommentServiceImpl {
	@Autowired
    private com.cay.repository.BBSCommentRepository bbscommentRepository;
	
	public void save(Comment comment) {
		bbscommentRepository.save(comment);
    }
	
	public Comment findById(String id) {
		return this.bbscommentRepository.findById(id);
	};
}
