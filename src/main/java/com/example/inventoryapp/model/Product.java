package com.example.inventoryapp.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Document("products")
public class Product {

    @Id
    @Field("_id")
    private ObjectId id;

    private String name;

    private double price;

    private String category;

    private int quantity;

    @Valid
    private List<@Pattern(regexp = "https://.*\\.(jpg|png|gif)", message = "Each image needs to be a valid URL") String> images;

    private List<Map<String, String>> attributes;

    private String SKU;

    private ObjectId userId;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public Product(ObjectId id, String name, double price,
            String category, int quantity, String SKU, List<String> images,
            List<Map<String, String>> attributes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.images = images;
    }

    public Product() {
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSKU() {
        return SKU;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<Map<String, String>> getAttributes() {
        return attributes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAttributes(List<Map<String, String>> attributes) {
        this.attributes = attributes;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toString() {
        return "Name: " + getName() +
                " Price: " + getPrice() +
                " Id: " + getId() +
                " Category: " + getCategory() +
                " SKU: " + getSKU();
    }
}
