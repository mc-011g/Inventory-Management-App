package com.example.inventoryapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "");
        User newUser = new User();
        newUser.setRole("USER");
        model.addAttribute("user", newUser);

        return "register";
    }

    @PostMapping("/register")
    public String createAccount(@Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model) {

        if (userService.checkForExistingEmail(user.getEmail())) {
            bindingResult.rejectValue("email",
                    "error.user.email",
                    "This email is already being used by another user");
        }

        if (user.getPassword().length() < 6) {
            bindingResult.rejectValue("password",
                    "error.user",
                    "Password length needs to be at least 6 characters");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.registerNewUser(UUID.randomUUID().toString(), user.getEmail(), user.getPassword(), "USER");

        return "redirect:/login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        model.addAttribute("form", new User());
        model.addAttribute("user", new User());
        return "users";
    }

    @PostMapping("/users/create")
    public String createNewUser(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (userService.checkForExistingEmail(user.getEmail())) {
            bindingResult.rejectValue("email",
                    "error.user",
                    "This email is already being used by another user");
        }

        if (user.getPassword().length() < 6) {
            bindingResult.rejectValue("password",
                    "error.user",
                    "Password length needs to be at least 6 characters");
        }

        if (bindingResult.hasErrors()) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("user", user);
            model.addAttribute("showAddModal", true);
            return "users";
        }

        userService.registerNewUser(UUID.randomUUID().toString(), user.getEmail(), user.getPassword(), user.getRole());
        model.addAttribute("user", userService.getUser(user.getEmail()));
        redirectAttributes.addFlashAttribute("message", "addUser");

        return "redirect:/users";
    }

    @PostMapping("/users/edit")
    public String editUser(
            @Valid @ModelAttribute User user, BindingResult bindingResult,
            Model model, @RequestParam String selectedUserId, RedirectAttributes redirectAttributes) {

        user.set_id(selectedUserId);

        if (bindingResult.hasErrors()) {
            List<User> users = userService.getAllUsers();

            model.addAttribute("users", users);
            model.addAttribute("user", user);
            model.addAttribute("selectedUserId", selectedUserId);
            model.addAttribute("showEditModal", true);

            return "users";
        }

        User editedUser = new User();

        // Change password only if a new one was set
        if (user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
            if (user.getNewPassword().length() < 6) {

                bindingResult.rejectValue("newPassword",
                        "error.user",
                        "Password length needs to be at least 6 characters");

                List<User> users = userService.getAllUsers();

                model.addAttribute("users", users);
                model.addAttribute("user", user);
                model.addAttribute("selectedUserId", selectedUserId);
                model.addAttribute("showEditModal", true);

                return "users";
            }
        }

        editedUser.setEmail(user.getEmail());
        editedUser.setRole(user.getRole());

        userService.updateUserInformation(user.getId(), editedUser.getEmail(), editedUser.getRole(),
                user.getNewPassword());

        redirectAttributes.addFlashAttribute("message", "editUser");

        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam String deleteUserId, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", "deleteUser");
        userService.deleteUser(deleteUserId);

        return "redirect:/users";
    }

    @PostMapping(path = "/users", params = "searchUsers")
    public String searchUsers(@RequestParam String searchQuery, Model model, RedirectAttributes redirectAttributes) {

        List<User> users = userService.getAllUsers();
        List<User> searchResults = new ArrayList<>();

        for (User user : users) {
            if ((user.getEmail().toLowerCase()).contains(searchQuery.toLowerCase())) {
                searchResults.add(user);
            }
        }

        redirectAttributes.addFlashAttribute("searchResults", searchResults);
        redirectAttributes.addFlashAttribute("searchQuery", searchQuery);

        return "redirect:/users";
    }

}