package com.example.inventoryapp.repository;

public interface CustomUserRepository {
    public void updateUserPassword(String email, String password);

    public void updateUserDetails(String id, String email, String role, String newPassword);
}
