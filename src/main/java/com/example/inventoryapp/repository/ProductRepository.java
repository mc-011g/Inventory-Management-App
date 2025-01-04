package com.example.inventoryapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.inventoryapp.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product findProductByName(String name);

    List<Product> findProductsByUserId(String userId);

    @Query(value = "{category:'?0'}")
    List<Product> findAll(String category);

    Product findProductById(String id);

    public long count();
}
