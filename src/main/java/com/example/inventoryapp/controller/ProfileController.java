package com.example.inventoryapp.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfileInformation(@Valid @ModelAttribute User user,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (user.getPassword().length() < 6) {
            bindingResult.rejectValue("password",
                    "error.user",
                    "Password length needs to be at least 6 characters");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showPasswordInputs", true);
            return "profile";
        }

        userService.updateUserPassword(user.getEmail(), user.getPassword());
        redirectAttributes.addFlashAttribute("message", "saveProfile");

        return "redirect:/profile";
    }
}
