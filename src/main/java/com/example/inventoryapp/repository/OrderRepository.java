package com.example.inventoryapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.inventoryapp.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findOrdersById(String id);

    @Query(value = "{category:'?0'}")
    List<Order> findAll(String category);

    Order findOrderById(String id);

    List<Order> findOrdersByUserId(String userId);

    List<Order> findByStatus(String status);

    public long count();
}
