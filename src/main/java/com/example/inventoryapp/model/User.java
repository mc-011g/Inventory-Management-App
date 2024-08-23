package com.example.inventoryapp.model;

import java.util.Collection;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Document("users")
public class User implements UserDetails {
    @Id
    @Field("_id")
    private ObjectId id;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    private Set<GrantedAuthority> userAuthorities;

    @NotEmpty(message = "Role is required")
    @Size(min = 2, max = 30, message = "Role must be between 2 and 30 characters")
    private String role;

    private String newPassword;

    public User(String email, String password, Set<GrantedAuthority> grantedAuthorities) {
        this.email = email;
        this.password = password;
        this.userAuthorities = grantedAuthorities;
    }

    public User(String email, String password, String role) {
        this.id = new ObjectId();
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public ObjectId getId() {
        return id;
    }

    public void set_id(ObjectId id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserAuthorities(Set<GrantedAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
