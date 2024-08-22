package com.example.inventoryapp.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.inventoryapp.model.User;
import com.example.inventoryapp.repository.CustomUserRepository;
import com.example.inventoryapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserRepository customUserRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void registerNewUser(String email, String password, String role) {
        userRepository.save(new User(
                email,
                passwordEncoder.encode(password),
                role));
    }

    public void updateUserPassword(String email, String password) {
        customUserRepository.updateUserPassword(email, password);
    }

    public void updateUserInformation(ObjectId id, String email, String role, String newPassword) {
        customUserRepository.updateUserDetails(id, email, role, newPassword);
    }

    public boolean checkForExistingEmail(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    public boolean checkValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }

}
