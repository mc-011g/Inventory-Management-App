package com.example.inventoryapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.inventoryapp.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);
}
