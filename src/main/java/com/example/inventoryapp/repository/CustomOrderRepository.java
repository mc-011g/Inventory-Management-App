package com.example.inventoryapp.repository;

import java.util.List;
import com.example.inventoryapp.model.OrderItem;

public interface CustomOrderRepository {

    void updateOrderDetails(String id, String customerName, String customerEmail, String customerPhone,
            String customerAddress, List<OrderItem> orderItems, double totalPrice, String status, String notes);
}
