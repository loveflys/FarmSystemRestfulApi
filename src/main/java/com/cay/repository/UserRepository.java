package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Users.vo.User;
/**
 * 用户仓库
 * @author 陈安一
 *
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByPhone(String phone);

    User findById(String id);
}
