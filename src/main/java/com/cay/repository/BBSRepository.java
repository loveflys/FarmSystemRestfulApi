package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.BBS.vo.BBS;
/**
 * 论坛帖子仓库
 * @author 陈安一
 *
 */
public interface BBSRepository extends PagingAndSortingRepository<BBS, Long> {
	BBS findById(String id);
}
