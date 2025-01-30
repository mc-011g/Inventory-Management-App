package com.example.inventoryapp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByUserId(String id) {
        return categoryRepository.findCategoriesByUserId(id);
    }

    public Category getCategory(String id) {
        return categoryRepository.findCategoryById(id);
    }

    public String createNewCategory(Category newCategory, String userId) {
        if (checkIfCategoryExists(newCategory.getName(), userId)) {
            return "existingCategory";
        } else {
            categoryRepository.save(newCategory);
            return "addCategory";
        }
    }

    public boolean checkIfCategoryExists(String name, String userId) {
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(userId);
        for (Category userCategory : userCategories) {
            if (userCategory.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String updateCategory(Category category, String userId) {
        String originalCategoryName = categoryRepository.findCategoryById(category.getId()).getName();

        if (!originalCategoryName.equals(category.getName())) {
            if (checkIfCategoryExists(category.getName(), userId)) {
                return "existingCategory";
            }
        }

        category.setUpdatedAt(new Date().toString());
        categoryRepository.save(category);
        return "editCategory";
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

}
