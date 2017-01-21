package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.BBS.vo.Comment;
/**
 * 论坛帖子仓库
 * @author 陈安一
 *
 */
public interface BBSCommentRepository extends PagingAndSortingRepository<Comment, Long> {
	Comment findById(String id);
}
