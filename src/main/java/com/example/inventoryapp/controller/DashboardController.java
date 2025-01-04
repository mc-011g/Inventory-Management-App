package com.example.inventoryapp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.inventoryapp.model.Order;
import com.example.inventoryapp.model.Product;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.OrderService;
import com.example.inventoryapp.service.ProductService;
import com.example.inventoryapp.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        String userId = user.getId();

        List<Order> orders = orderService.getUserOrders(userId);
        List<Order> shortenedOrders;

        if (orders.size() > 10) {
            shortenedOrders = orders.subList(0, 10);
        } else {
            shortenedOrders = orders.subList(0, orders.size());
        }

        List<Product> products = productService.getUserProducts(userId);

        model.addAttribute("orders", shortenedOrders);
        model.addAttribute("products", products);
        model.addAttribute("totalSales", orderService.getTotalSalesValue(userId));
        model.addAttribute("lowStockProducts", productService.getLowStockProducts(userId));
        model.addAttribute("totalStock", productService.getTotalStock(userId));
        model.addAttribute("user", user);

        return "dashboard";
    }

}
