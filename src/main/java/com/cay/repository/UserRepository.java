package com.cay.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cay.Model.Users.vo.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByPhone(String username);

}
