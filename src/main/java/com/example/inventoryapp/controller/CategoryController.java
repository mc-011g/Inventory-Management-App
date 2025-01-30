package com.example.inventoryapp.controller;

import java.security.Principal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.inventoryapp.model.Category;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.CategoryService;
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/categories")
    public String categories(Model model, @RequestParam(required = false) String editCategoryId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Category> categories = categoryService.getCategoriesByUserId(user.getId());

        if (editCategoryId != null) {
            Category category = categoryService.getCategory(editCategoryId);
            model.addAttribute("editedCategory", category);
        } else {
            model.addAttribute("editedCategory", new Category());
        }

        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category());

        return "categories";
    }

    @PostMapping(path = "/categories", params = "createCategory")
    public String newCategorySubmit(@Valid @ModelAttribute Category newCategory, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        newCategory.setUserId(user.getId());
        newCategory.setId(UUID.randomUUID().toString());
        newCategory.setCreatedAt(new Date().toString());
        newCategory.setUpdatedAt(new Date().toString());

        // Custom validation logic
        if (newCategory.getName() == null || newCategory.getName().length() < 1) {
            bindingResult.rejectValue("name",
                    "error.newCategory",
                    "Name needs to be 1 or more characters.");
        }
        if (newCategory.getDescription().length() > 200) {
            bindingResult.rejectValue("description",
                    "error.newCategory",
                    "Description needs to be 200 or less characters.");
        }

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getCategoriesByUserId(user.getId());
            model.addAttribute("categories", categories);
            model.addAttribute("org.springframework.validation.BindingResult.newCategory", bindingResult);

            model.addAttribute("newCategory", newCategory);
            model.addAttribute("editedCategory", new Category());
            model.addAttribute("showAddModal", true);
            return "categories";
        }

        String message = categoryService.createNewCategory(newCategory, user.getId());
        model.addAttribute("newCategory", newCategory);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @PostMapping(path = "/categories", params = "editCategory")
    public String editCategorySubmit(@Valid @ModelAttribute Category editedCategory, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Category originalCategory = categoryService.getCategory(editedCategory.getId());
        editedCategory.setCreatedAt(originalCategory.getCreatedAt());
        editedCategory.setUserId(originalCategory.getUserId());

        // Custom validation logic
        if (editedCategory.getName() == null || editedCategory.getName().length() < 1) {
            bindingResult.rejectValue("name",
                    "error.editedCategory",
                    "Name needs to be more than 1 character.");
        }
        if (editedCategory.getDescription().length() > 200) {
            bindingResult.rejectValue("description",
                    "error.editedCategory",
                    "Description needs to be 200 or less characters.");
        }

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getCategoriesByUserId(user.getId());
            model.addAttribute("categories", categories);
            model.addAttribute("org.springframework.validation.BindingResult.editedCategory", bindingResult);
            model.addAttribute("editedCategory", editedCategory);
            model.addAttribute("newCategory", new Category());
            model.addAttribute("showEditModal", true);
            return "categories";
        }

        String message = categoryService.updateCategory(editedCategory, user.getId());
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @PostMapping(path = "/categories", params = "deleteCategory")
    public String deleteCategory(@RequestParam String deleteCategoryId, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", "deleteCategory");
        categoryService.deleteCategory(deleteCategoryId);

        return "redirect:/categories";
    }

    @PostMapping(path = "/categories", params = "searchCategories")
    public String searchCategories(@RequestParam String searchQuery, Model model, RedirectAttributes redirectAttributes,
            Principal principal) {

        List<Category> categories = categoryService
                .getCategoriesByUserId(userService.getUserByEmail(principal.getName()).getId());
        List<Category> searchResults = new ArrayList<>();

        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                searchResults.add(category);
            }
        }

        redirectAttributes.addFlashAttribute("searchResults", searchResults);
        redirectAttributes.addFlashAttribute("searchQuery", searchQuery);

        return "redirect:/categories";
    }

}
