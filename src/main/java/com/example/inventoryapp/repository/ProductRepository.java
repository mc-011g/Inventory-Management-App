package com.example.inventoryapp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.inventoryapp.model.Product;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    Product findProductByName(String name);

    List<Product> findProductsByUserId(ObjectId userId);

    @Query(value = "{category:'?0'}")
    List<Product> findAll(String category);

    Product findProductById(ObjectId id);

    public long count();
}
