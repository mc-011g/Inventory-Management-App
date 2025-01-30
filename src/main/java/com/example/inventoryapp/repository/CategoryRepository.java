package com.example.inventoryapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.inventoryapp.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findCategoriesById(String id);

    List<Category> findCategoriesByUserId(String userId);

    @Query(value = "{category:'?0'}")
    List<Category> findAll(String category);

    Category findCategoryById(String id);

    public long count();

}
