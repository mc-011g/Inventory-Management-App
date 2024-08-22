package com.example.inventoryapp.repository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.example.inventoryapp.model.Product;
import com.mongodb.client.result.UpdateResult;

@Component
public class CustomItemRepositoryImpl implements CustomItemRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateProductQuantity(String name, float newQuantity) {
        Query query = new Query(Criteria.where("name").is(name));

        Update update = new Update();
        update.set("quantity", newQuantity);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);

        System.out.println(result.getModifiedCount() + " document(s) updated..");
    }

    @Override
    public void updateProductDetails(ObjectId _id, String newName, String newCategory, double newPrice, int newQuantity,
            String newSKU) {
        Query query = new Query(Criteria.where("_id").is(_id));

        Update update = new Update();
        update.set("price", newPrice);
        update.set("name", newName);
        update.set("category", newCategory);
        update.set("quantity", newQuantity);
        update.set("SKU", newSKU);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);

        System.out.println(result.getModifiedCount() + " documents(s) updated..");
    }
}
