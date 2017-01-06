package com.cay.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cay.Model.Users.User;

public interface UserRepository extends MongoRepository<User, Long> {

    User findByPhone(String username);

}
