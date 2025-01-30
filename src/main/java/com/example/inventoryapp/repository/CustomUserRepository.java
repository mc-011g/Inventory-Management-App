package com.example.inventoryapp.repository;

public interface CustomUserRepository {
    public void updateUserPassword(String id, String password);

    public void updateProfileInformation(String id, String email, String firstName, String lastName, String newPassword,
            String role);
}
