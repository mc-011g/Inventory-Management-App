package com.example.inventoryapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.example.inventoryapp.model.Order;
import com.example.inventoryapp.model.Product;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.service.OrderService;
import com.example.inventoryapp.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.inventoryapp.model.OrderItem;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/orders")
    public String orders(Model model, @RequestParam(required = false) String editOrderId) {
        List<Order> orders = orderService.getOrders();
        List<Product> products = productService.getProducts();

        Order newOrder = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product : products) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(0);
            orderItem.setSelected(false);
            orderItems.add(orderItem);
        }
        newOrder.setOrderItems(orderItems);

        if (editOrderId != null) {
            Order order = orderService.getOrder(editOrderId);
            model.addAttribute("editedOrder", order);
        } else {
            model.addAttribute("editedOrder", new Order());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("newOrder", newOrder);
        model.addAttribute("orderItem", new OrderItem());
        model.addAttribute("form", new Order());
        model.addAttribute("products", products);

        return "orders";
    }

    @PostMapping(path = "/orders", params = "createOrder")
    public String newOrderSubmit(@Valid @ModelAttribute Order newOrder,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        newOrder.setUserId(user.getId());

        // Custom validation logic
        if (newOrder.getTotalPrice() < 0) {
            bindingResult.rejectValue("totalPrice",
                    "error.newOrder",
                    "Order total needs to be a positive number");
        }
        if (newOrder.getCustomerName() == null || newOrder.getCustomerName().isEmpty()) {
            bindingResult.rejectValue("customerName",
                    "error.newOrder",
                    "Customer name is required");
        }
        if (newOrder.getCustomerEmail() == null || newOrder.getCustomerEmail().isEmpty()) {
            bindingResult.rejectValue("customerEmail",
                    "error.newOrder",
                    "Customer email is required");
        }
        if (newOrder.getCustomerAddress() == null || newOrder.getCustomerAddress().isEmpty()) {
            bindingResult.rejectValue("customerAddress",
                    "error.newOrder",
                    "Customer address is required");
        }
        if (newOrder.getCustomerPhone() == null || newOrder.getCustomerPhone().isEmpty()) {
            bindingResult.rejectValue("customerPhone",
                    "error.newOrder",
                    "Customer phone is required");
        }
        if (newOrder.getOrderItems() == null || newOrder.getOrderItems().isEmpty()) {
            bindingResult.rejectValue("orderItems",
                    "error.newOrder",
                    "At least one order item is required");
        }

        if (bindingResult.hasErrors()) {
            // Re-fetch list of orders
            List<Order> orders = orderService.getOrders();
            List<Product> products = productService.getProducts();

            model.addAttribute("orders", orders);
            model.addAttribute("products", products);
            model.addAttribute("org.springframework.validation.BindingResult.newOrder", bindingResult);
            model.addAttribute("newOrder", newOrder);
            model.addAttribute("editedOrder", new Order());
            model.addAttribute("showAddModal", true);

            return "orders";
        }

        List<OrderItem> orderItems = newOrder.getOrderItems();
        List<OrderItem> selectedOrderItems = new ArrayList<>();
        double totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            if (orderItem.isSelected()) {
                orderItem.setProduct(productService.getProduct(orderItem.getProduct().getId())); // get rest of
                                                                                                 // information besides
                                                                                                 // id
                selectedOrderItems.add(orderItem);
                totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
            }
        }

        // Processed by default for now
        String creationDate = new Date().toString();

        newOrder.setCreatedAt(creationDate);
        newOrder.setUpdatedAt(creationDate);
        newOrder.setStatus("Pending");
        newOrder.setOrderItems(selectedOrderItems);
        newOrder.setTotalPrice(totalPrice);

        String message = orderService.createNewOrder(newOrder);
        model.addAttribute("newOrder", newOrder);

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/orders";
    }

    @PostMapping("/editOrder")
    public String editOrder(
            @Valid @ModelAttribute Order editedOrder,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Custom validation logic
        if (editedOrder.getTotalPrice() < 0) {
            bindingResult.rejectValue("totalPrice",
                    "error.editedOrder",
                    "Order total needs to be a positive number");
        }
        if (editedOrder.getCustomerName() == null || editedOrder.getCustomerName().isEmpty()) {
            bindingResult.rejectValue("customerName",
                    "error.editedOrder",
                    "Customer name is required");
        }
        if (editedOrder.getCustomerEmail() == null || editedOrder.getCustomerEmail().isEmpty()) {
            bindingResult.rejectValue("customerEmail",
                    "error.editedOrder",
                    "Customer email is required");
        }
        if (editedOrder.getCustomerAddress() == null || editedOrder.getCustomerAddress().isEmpty()) {
            bindingResult.rejectValue("customerAddress",
                    "error.editedOrder",
                    "Customer address is required");
        }
        if (editedOrder.getOrderItems() == null || editedOrder.getOrderItems().isEmpty()) {
            bindingResult.rejectValue("orderItems",
                    "error.editedOrder",
                    "At least one product is required");
        }

        if (bindingResult.hasErrors()) {
            // Re-fetch list of orders
            List<Order> orders = orderService.getOrders();

            model.addAttribute("orders", orders);
            model.addAttribute("editedOrder", editedOrder);
            model.addAttribute("org.springframework.validation.BindingResult.editedOrder", bindingResult);
            model.addAttribute("newOrder", new Order());
            model.addAttribute("showEditModal", true);

            return "orders";
        }

        redirectAttributes.addFlashAttribute("message", "editOrder");

        List<OrderItem> orderItems = editedOrder.getOrderItems();
        List<OrderItem> selectedOrderItems = new ArrayList<>();
        double totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            if (orderItem.isSelected()) {
                orderItem.setProduct(productService.getProduct(orderItem.getProduct().getId()));
                selectedOrderItems.add(orderItem);
                totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
            }
        }

        editedOrder.setOrderItems(selectedOrderItems);
        editedOrder.setTotalPrice(totalPrice);
        orderService.updateOrder(editedOrder);

        return "redirect:/orders";
    }

    @PostMapping(path = "/orders", params = "deleteOrder")
    public String deleteOrder(@RequestParam String deleteOrderId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "deleteOrder");
        orderService.deleteOrder(deleteOrderId);
        return "redirect:/orders";
    }

    @PostMapping(path = "/orders", params = "searchOrders")
    public String searchOrders(@RequestParam String searchQuery, Model model, RedirectAttributes redirectAttributes) {

        List<Order> orders = orderService.getOrders();
        List<Order> searchResults = new ArrayList<>();

        for (Order order : orders) {
            if (order.getId().toString().contains(searchQuery)) {
                searchResults.add(order);
            }
        }

        redirectAttributes.addFlashAttribute("searchResults", searchResults);
        redirectAttributes.addFlashAttribute("searchQuery", searchQuery);

        return "redirect:/orders";
    }

}