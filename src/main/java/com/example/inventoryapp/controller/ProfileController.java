package com.example.inventoryapp.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        user.setPassword("");
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfileInformation(@Valid @ModelAttribute User user,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model, Principal principal) {

        User authenticatedUser = userService.getUserByEmail(principal.getName());

        if (user.getId().equals(authenticatedUser.getId())) {
            if (passwordEncoder.matches(user.getPassword(), authenticatedUser.getPassword())) {

                if (user.getFirstName() == null || user.getFirstName().length() == 0) {
                    bindingResult.rejectValue("firstName",
                            "error.user",
                            "First name is required");
                    return "profile";
                }

                if (user.getLastName() == null || user.getLastName().length() == 0) {
                    bindingResult.rejectValue("lastName",
                            "error.user",
                            "Last name is required");
                    return "profile";
                }

                String reauthenticationPassword = "";

                // If setting a new password
                if (user.getNewPassword().length() != 0) {
                    if (user.getNewPassword().length() < 8) {
                        bindingResult.rejectValue("newPassword",
                                "error.user",
                                "Password length needs to be at least 8 characters");
                        return "profile";
                    }
                    reauthenticationPassword = user.getNewPassword();
                } else {
                    reauthenticationPassword = authenticatedUser.getPassword();
                }

                if (authenticatedUser.getRole().equals("USER")) {
                    user.setRole("USER");
                }

                String reauthenticationEmail = "";

                // If setting a new email
                if (!authenticatedUser.getEmail().equals(user.getEmail())) {
                    if (user.getEmail() == null || user.getEmail().length() == 0) {
                        bindingResult.rejectValue("email",
                                "error.user",
                                "Email is required");
                        return "profile";
                    }
                    if (userService.checkForExistingEmail(user.getEmail())) {
                        bindingResult.rejectValue("email",
                                "error.user",
                                "Email already in use");
                        return "profile";
                    } else {
                        reauthenticationEmail = user.getEmail();
                    }
                } else {
                    reauthenticationEmail = authenticatedUser.getEmail();
                }

                userService.updateUserProfileInformation(user.getId(), reauthenticationEmail, user.getFirstName(),
                        user.getLastName(), user.getNewPassword(), user.getRole());

                // Reauthenticate the user if the email was changed
                if (!authenticatedUser.getEmail().equals(user.getEmail())) {
                    userService.reauthenticateUser(reauthenticationEmail, reauthenticationPassword);
                }

                redirectAttributes.addFlashAttribute("message", "saveProfile");
            } else {
                bindingResult.rejectValue("password",
                        "error.user",
                        "Incorrect password");
                return "profile";
            }
        }
        return "redirect:/profile";
    }
}
