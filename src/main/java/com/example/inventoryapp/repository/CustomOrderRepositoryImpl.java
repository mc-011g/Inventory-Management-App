package com.example.inventoryapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.example.inventoryapp.model.Order;
import com.example.inventoryapp.model.OrderItem;
import com.mongodb.client.result.UpdateResult;

@Component
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateOrderDetails(String id, String customerName, String customerEmail, String customerPhone,
            String customerAddress, List<OrderItem> orderItems, double totalPrice, String status, String notes) {

        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update();

        update.set("customerName", customerName);
        update.set("customerEmail", customerEmail);
        update.set("customerPhone", customerPhone);
        update.set("customerAddress", customerAddress);
        update.set("orderItems", orderItems);
        update.set("totalPrice", totalPrice);
        update.set("status", status);
        update.set("notes", notes);
        update.set("updatedAt", new Date().toString());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Order.class);

        System.out.println(result.getModifiedCount() + " documents(s) updated..");
    }
}
