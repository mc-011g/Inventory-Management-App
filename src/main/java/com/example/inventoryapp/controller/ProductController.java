package com.example.inventoryapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.model.Product;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.CategoryService;
import com.example.inventoryapp.service.ProductService;
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/products")
    public String home(Model model, @RequestParam(required = false) String editProductId, Principal principal) {
        List<Product> products = productService.getProducts();
        List<Category> categories = categoryService
                .getCategoriesByUserId(userService.getUserByEmail(principal.getName()).getId());

        if (editProductId != null) {
            Product product = productService.getProduct(editProductId);
            model.addAttribute("editedProduct", product);
        } else {
            model.addAttribute("editedProduct", new Product());
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product());
        model.addAttribute("form", new Product());

        return "products";
    }

    @PostMapping(path = "/products", params = "createProduct")
    public String newProductSubmit(@Valid @ModelAttribute Product newProduct,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        newProduct.setUserId(user.getId());
        newProduct.setId(UUID.randomUUID().toString());
        newProduct.setCreatedAt(new Date().toString());
        newProduct.setUpdatedAt(new Date().toString());

        // Custom validation logic
        if (newProduct.getPrice() < 0) {
            bindingResult.rejectValue("price",
                    "error.newProduct",
                    "Price needs to be a positive number");
        }
        if (newProduct.getName() == null || newProduct.getName().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.newProduct.name",
                    "Name is required");
        }
        if (newProduct.getSKU() == null || newProduct.getSKU().isEmpty()) {
            bindingResult.rejectValue("SKU",
                    "error.newProduct.SKU",
                    "SKU is required");
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
            return "products";
        }

        newProduct.setImages(new ArrayList<String>());
        newProduct.setAttributes(new ArrayList<Map<String, String>>());

        String message = productService.createNewProduct(newProduct, user.getId());
        model.addAttribute("newProduct", newProduct);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @PostMapping("/editProduct")
    public String editProduct(
            @Valid @ModelAttribute Product editedProduct,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Custom validation logic
        if (editedProduct.getPrice() < 0) {
            bindingResult.rejectValue("price",
                    "error.editedProduct",
                    "Price needs to be a positive number");
        }
        if (editedProduct.getName() == null || editedProduct.getName().isEmpty()) {
            bindingResult.rejectValue("name",
                    "error.editedProduct.name",
                    "Name is required");
        }
        if (editedProduct.getSKU() == null || editedProduct.getSKU().isEmpty()) {
            bindingResult.rejectValue("SKU",
                    "error.editedProduct.SKU",
                    "SKU is required");
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
            return "products";
        }

        String message = productService.updateProduct(editedProduct, user.getId());
        redirectAttributes.addFlashAttribute("message", message);
        productService.updateProduct(editedProduct, user.getId());

        return "redirect:/products";
    }

    @PostMapping(path = "/products", params = "deleteProduct")
    public String deleteProduct(@RequestParam String deleteProductId, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", "deleteProduct");
        productService.deleteProduct(deleteProductId);

        return "redirect:/products";
    }

    @PostMapping(path = "/products", params = "searchProducts")
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

        return "redirect:/products";
    }
}
