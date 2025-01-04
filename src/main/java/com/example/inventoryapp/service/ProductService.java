package com.example.inventoryapp.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public Product getProduct(String id) {
        return productRepository.findProductById(id);
    }

    public List<Product> getUserProducts(String userId) {
        return productRepository.findProductsByUserId(userId);
    }

    // Low stock threshold is set to 15. 5 product limit.
    public List<Product> getLowStockProducts(String userId) {
        List<Product> products = getUserProducts(userId);
        List<Product> lowStockProducts = new ArrayList<Product>();

        for (Product product : products) {
            if (product.getQuantity() <= 15) {
                lowStockProducts.add(product);
            }
        }

        if (lowStockProducts.size() >= 5) {
            return lowStockProducts.subList(0, 5);
        } else {
            return lowStockProducts;
        }
    }

    public String getTotalStock(String userId) {
        List<Product> products = getUserProducts(userId);
        int totalStock = 0;

        for (Product product : products) {
            totalStock += product.getQuantity();
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(totalStock);
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

    public boolean checkIfProductExists(String name, String userId) {
        List<Product> userProducts = productRepository.findProductsByUserId(userId);
        for (Product userProduct : userProducts) {
            if (userProduct.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String createNewProduct(Product newProduct, String userId) {
        if (checkIfProductExists(newProduct.getName(), userId)) {
            return "existingProduct";
        } else {
            productRepository.save(newProduct);
            return "addProduct";
        }
    }

    public void updateProduct(String _id, String name, String category, double price, int quantity, String SKU) {
        customItemRepository.updateProductDetails(_id, name, category, price, quantity, SKU);
    }

    public void updateProduct(Product product) {
        product.setUpdatedAt(new Date().toString());
        productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
