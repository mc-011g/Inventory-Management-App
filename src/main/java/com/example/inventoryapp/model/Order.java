package com.example.inventoryapp.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("orders")
public class Order {

    @Id
    @Field("_id")
    private String id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;

    private double totalPrice;

    private String status;

    private List<OrderItem> orderItems;

    private String notes;

    private String userId;

    private String createdAt;

    private String updatedAt;

    public Order(String id, String customerName, String customerEmail, String customerPhone, String customerAddress,
            double totalPrice, Date orderDate, String status, List<OrderItem> orderItems, String notes) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderItems = orderItems;
        this.notes = notes;
    }

    public Order() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String toString() {
        return "Customer name: " + getCustomerName() +
                " Customer email: " + getCustomerEmail() +
                " Customer phone: " + getCustomerPhone() +
                " Customer address: " + getCustomerAddress() +
                " Total price: " + getTotalPrice() +
                " Order creation date: " + getCreatedAt() +
                " Order last update date: " + getUpdatedAt() +
                " Status: " + getStatus() +
                " Order Items: " + getOrderItems() +
                " Notes: " + getNotes();
    }
}
