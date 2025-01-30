package com.example.inventoryapp.model;

import java.util.Collection;
import java.util.Set;

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
    private String id;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    private String firstName;

    private String lastName;

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

    public User(String id, String email, String password, String firstName, String lastName, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
