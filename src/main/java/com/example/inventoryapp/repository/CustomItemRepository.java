package com.example.inventoryapp.repository;

import org.bson.types.ObjectId;

public interface CustomItemRepository {
    void updateProductQuantity(String name, float newQuantity);

    void updateProductDetails(ObjectId _id,
            String newName, String newCategory, double newPrice, int quantity, String SKU);
}
