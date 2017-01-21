package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Info.vo.Comment;
/**
 * 信息帖子仓库
 * @author 陈安一
 *
 */
public interface InfoCommentRepository extends PagingAndSortingRepository<Comment, Long> {
	Comment findById(String id);
}
