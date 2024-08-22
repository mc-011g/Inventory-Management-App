package com.example.inventoryapp.repository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inventoryapp.model.User;
import com.mongodb.client.result.UpdateResult;

@Component
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateUserPassword(String email, String password) {
        Query query = new Query(Criteria.where("email").is(email));

        Update update = new Update();
        String encodedNewPassword = passwordEncoder.encode(password);
        update.set("password", encodedNewPassword);

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        System.out.println(result.getModifiedCount() + " document(s) updated.");
    }

    @Override
    public void updateUserDetails(ObjectId id, String email, String role, String newPassword) {
        Query query = new Query(Criteria.where("id").is(id));
        System.out.println("QUERY:" + query);

        Update update = new Update();
        update.set("email", email);
        update.set("role", role);

        if (newPassword != "") { // Only update if there is a new password being set
            update.set("password", passwordEncoder.encode(newPassword));
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        System.out.println(result.getModifiedCount() + " documents(s) updated..");
    }
}
