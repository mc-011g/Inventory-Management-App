package com.example.inventoryapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.inventoryapp.model.Product;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.ProductService;
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/home")
    public String home(Model model, @RequestParam(required = false) String editProductId) {
        List<Product> products = productService.getProducts();

        if (editProductId != null) {
            Product product = productService.getProduct(new ObjectId(editProductId));
            model.addAttribute("editedProduct", product);
        } else {
            model.addAttribute("editedProduct", new Product());
        }

        model.addAttribute("products", products);
        model.addAttribute("newProduct", new Product());
        model.addAttribute("form", new Product());

        return "home";
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

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
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

        userService.registerNewUser(user.getEmail(), user.getPassword(), "USER");

        return "redirect:/login";
    }

    @PostMapping(path = "/home", params = "createProduct")
    public String newProductSubmit(@Valid @ModelAttribute Product newProduct,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        newProduct.setUserId(user.getId());

        // Custom validation logic
        if (newProduct.getPrice() < 0) {
            bindingResult.rejectValue("price",
                    "error.newProduct",
                    "Price needs to be a positive number");
        }
        if (newProduct.getName() == null || newProduct.getName().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.newProduct",
                    "Name is required");
        }
        if (newProduct.getSKU() == null || newProduct.getSKU().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.newProduct",
                    "SKU is required");
        }
        if (newProduct.getCategory() == null || newProduct.getCategory().isEmpty()) {
            bindingResult.rejectValue("category",
                    "error.newProduct",
                    "Category is required");
        }
        if (newProduct.getQuantity() < 0) {
            bindingResult.rejectValue("quantity",
                    "error.newProduct",
                    "Quantity must be a positive number");
        }

        if (bindingResult.hasErrors()) {
            // Re-fetch list of products
            List<Product> products = productService.getProducts();

            model.addAttribute("products", products);
            model.addAttribute("org.springframework.validation.BindingResult.newProduct", bindingResult);
            model.addAttribute("newProduct", newProduct);
            model.addAttribute("editedProduct", new Product());
            model.addAttribute("showAddModal", true);

            return "home";
        }

        newProduct.setImages(new ArrayList<String>());
        newProduct.setAttributes(new ArrayList<Map<String, String>>());

        String message = productService.createNewProduct(newProduct, user.getId());

        model.addAttribute("newProduct", newProduct);

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/home";
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

        userService.registerNewUser(user.getEmail(), user.getPassword(), user.getRole());

        model.addAttribute("user", userService.getUser(user.getEmail()));

        redirectAttributes.addFlashAttribute("message", "addUser");
        return "redirect:/users";
    }

    @PostMapping("/users/edit")
    public String editUser(
            @Valid @ModelAttribute User user, BindingResult bindingResult,
            Model model, @RequestParam String selectedUserId, RedirectAttributes redirectAttributes) {

        user.set_id(new ObjectId(selectedUserId));

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

        userService.deleteUser(new ObjectId(deleteUserId));

        return "redirect:/users";
    }

    @PostMapping("/editProduct")
    public String editProduct(
            @Valid @ModelAttribute Product editedProduct,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Custom validation logic
        if (editedProduct.getPrice() < 0) {
            bindingResult.rejectValue("price",
                    "error.editedProduct",
                    "Price needs to be a positive number");
        }
        if (editedProduct.getName() == null || editedProduct.getName().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.editedProduct",
                    "Name is required");
        }
        if (editedProduct.getSKU() == null || editedProduct.getSKU().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.editedProduct",
                    "SKU is required");
        }
        if (editedProduct.getCategory() == null || editedProduct.getCategory().isEmpty()) {
            bindingResult.rejectValue("category",
                    "error.editedProduct",
                    "Category is required");
        }
        if (editedProduct.getQuantity() < 0) {
            bindingResult.rejectValue("quantity",
                    "error.editedProduct",
                    "Quantity must be a positive number");
        }

        if (bindingResult.hasErrors()) {
            // Re-fetch list of products
            List<Product> products = productService.getProducts();

            model.addAttribute("products", products);
            model.addAttribute("editedProduct", editedProduct);
            model.addAttribute("org.springframework.validation.BindingResult.editedProduct", bindingResult);
            model.addAttribute("newProduct", new Product());
            model.addAttribute("showEditModal", true);

            return "home";
        }

        redirectAttributes.addFlashAttribute("message", "editProduct");

        productService.updateProduct(editedProduct.getId(), editedProduct.getName(),
                editedProduct.getCategory(), editedProduct.getPrice(), editedProduct.getQuantity(),
                editedProduct.getSKU());

        return "redirect:/home";
    }

    @PostMapping(path = "/home", params = "deleteProduct")
    public String deleteProduct(@RequestParam String deleteProductId, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", "deleteProduct");

        productService.deleteProduct(new ObjectId(deleteProductId));

        return "redirect:/home";
    }

    @PostMapping(path = "/home", params = "searchProducts")
    public String searchProducts(@RequestParam String searchQuery, Model model, RedirectAttributes redirectAttributes) {

        List<Product> products = productService.getProducts();
        List<Product> searchResults = new ArrayList<>();

        for (Product product : products) {
            if ((product.getName().toLowerCase()).contains(searchQuery.toLowerCase())) {
                searchResults.add(product);
            }
        }

        redirectAttributes.addFlashAttribute("searchResults", searchResults);
        redirectAttributes.addFlashAttribute("searchQuery", searchQuery);

        return "redirect:/home";
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
