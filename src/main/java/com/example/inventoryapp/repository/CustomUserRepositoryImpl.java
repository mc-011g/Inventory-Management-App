package com.example.inventoryapp.repository;

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
    public void updateUserPassword(String id, String password) {
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update();
        String encodedNewPassword = passwordEncoder.encode(password);
        update.set("password", encodedNewPassword);

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        System.out.println(result.getModifiedCount() + " document(s) updated.");
    }

    public void updateProfileInformation(String id, String email, String firstName, String lastName, String newPassword,
            String role) {

        Query query = new Query(Criteria.where("id").is(id));
        System.out.println("QUERY:" + query);

        Update update = new Update();
        update.set("email", email);
        update.set("firstName", firstName);
        update.set("lastName", lastName);

        if (newPassword != "") {
            update.set("password", passwordEncoder.encode(newPassword));
        }

        if (role != "") {
            update.set("role", role);
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        System.out.println(result.getModifiedCount() + " documents(s) updated..");
    }
}
