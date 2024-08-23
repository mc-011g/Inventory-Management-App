package com.example.inventoryapp.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.inventoryapp.model.Product;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.repository.CustomItemRepository;
import com.example.inventoryapp.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private CustomItemRepository customItemRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(ObjectId id) {
        return productRepository.findProductById(id);
    }

    public List<Product> getUserProducts(ObjectId userId) {
        return productRepository.findProductsByUserId(userId);
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                User user = (User) authentication.getPrincipal();
                products = getUserProducts(user.getId());
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                products = getAllProducts();
            }
        }

        return products;
    }

    public boolean checkIfProductExists(String name, ObjectId userId) {
        List<Product> userProducts = productRepository.findProductsByUserId(userId);
        for (Product userProduct : userProducts) {
            if (userProduct.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String createNewProduct(Product newProduct, ObjectId userId) {
        if (checkIfProductExists(newProduct.getName(), userId)) {
            return "existingProduct";
        } else {
            productRepository.save(newProduct);
            return "addProduct";
        }
    }

    public void updateProduct(ObjectId _id, String name, String category, double price, int quantity, String SKU) {
        customItemRepository.updateProductDetails(_id, name, category, price, quantity, SKU);
    }

    public void deleteProduct(ObjectId id) {
        productRepository.deleteById(id);
    }
}
