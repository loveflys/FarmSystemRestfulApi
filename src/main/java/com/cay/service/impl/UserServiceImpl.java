package com.cay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cay.Model.Users.vo.User;
import com.cay.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private com.cay.repository.UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByPhone(String phone) {
        return this.userRepository.findByPhone(phone);
    }
}