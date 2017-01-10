package com.cay.service;

import org.springframework.stereotype.Repository;

import com.cay.Model.Users.vo.User;

@Repository
public interface UserService {
    void save(User user);

    User findByPhone(String name);

}
