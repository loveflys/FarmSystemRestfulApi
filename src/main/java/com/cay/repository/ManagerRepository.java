package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.cay.Model.Manager.vo.Manager;
/**
 * 管理员仓库
 * @author 陈安一
 *
 */
public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long> {
	Manager findById(String id);
	
	Manager findByLogin(String login);
}
