package com.example.inventoryapp.repository;

public interface CustomItemRepository {
    void updateProductQuantity(String name, float newQuantity);

    void updateProductDetails(String id,
            String newName, String newCategory, double newPrice, int quantity, String SKU);
}
