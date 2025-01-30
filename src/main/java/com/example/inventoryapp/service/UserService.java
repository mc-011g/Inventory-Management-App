package com.example.inventoryapp.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.inventoryapp.model.User;
import com.example.inventoryapp.repository.CustomUserRepository;
import com.example.inventoryapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private MongoAuthUserDetailService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserRepository customUserRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean registerNewUser(String id, String email, String password, String firstName, String lastName,
            String role) {

        userRepository.save(new User(
                id,
                email,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                role));
        return true;
    }

    public void updateUserPassword(String id, String password) {
        customUserRepository.updateUserPassword(id, password);
    }

    public void updateUserProfileInformation(String id, String email, String firstName, String lastName,
            String newPassword, String role) {
        customUserRepository.updateProfileInformation(id, email, firstName, lastName, newPassword, role);
    }

    public boolean checkForExistingEmail(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    public boolean checkValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void reauthenticateUser(String email, String currentPassword) {
        UsernamePasswordAuthenticationToken authRequest;

        try {
            authRequest = new UsernamePasswordAuthenticationToken(email,
                    currentPassword);
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailService.loadUserByUsername(email);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                    currentPassword, userDetails.getAuthorities()));

        } catch (BadCredentialsException e) {
            try {
                throw new Exception("Invalid email or password", e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
