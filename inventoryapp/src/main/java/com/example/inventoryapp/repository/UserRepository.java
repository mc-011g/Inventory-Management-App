package com.example.inventoryapp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.inventoryapp.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findUserByEmail(String email);
}
