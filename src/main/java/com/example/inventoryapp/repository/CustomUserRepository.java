package com.example.inventoryapp.repository;

import org.bson.types.ObjectId;

public interface CustomUserRepository {
    public void updateUserPassword(String email, String password);

    public void updateUserDetails(ObjectId id, String email, String role, String newPassword);
}
