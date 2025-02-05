package com.example.inventoryapp.controller;

import java.security.Principal;
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
import com.example.inventoryapp.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.inventoryapp.model.OrderItem;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

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
                orderItem.setProduct(productService.getProduct(orderItem.getProduct().getId()));

                if (orderItem.getQuantity() <= orderItem.getProduct().getQuantity()) {
                    selectedOrderItems.add(orderItem);
                    productService.removeProductQuantityFromOrderItem(orderItem.getProduct(), orderItem.getQuantity());
                    totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
                }
            }
        }

        if (selectedOrderItems.size() != 0) {
            String creationDate = new Date().toString();

            newOrder.setCreatedAt(creationDate);
            newOrder.setUpdatedAt(creationDate);
            newOrder.setStatus("Pending");
            newOrder.setOrderItems(selectedOrderItems);
            newOrder.setTotalPrice(totalPrice);

            String message = orderService.createNewOrder(newOrder);
            model.addAttribute("newOrder", newOrder);
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            redirectAttributes.addFlashAttribute("message", "noItemsInOrder");
        }
        return "redirect:/orders";
    }

    @PostMapping("/editOrder")
    public String editOrder(
            @Valid @ModelAttribute Order editedOrder,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());

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
        if (editedOrder.getCustomerPhone() == null || editedOrder.getCustomerPhone().isEmpty()) {
            bindingResult.rejectValue("customerPhone",
                    "error.editedOrder",
                    "Customer phone is required");
        }
        if (editedOrder.getOrderItems() == null || editedOrder.getOrderItems().isEmpty()) {
            bindingResult.rejectValue("orderItems",
                    "error.editedOrder",
                    "At least one product is required");
        }

        if (bindingResult.hasErrors()) {
            // Re-fetch list of orders
            List<Order> orders = orderService.getOrders();
            List<Product> products = productService.getProducts();

            model.addAttribute("orders", orders);
            model.addAttribute("products", products);
            model.addAttribute("org.springframework.validation.BindingResult.editedOrder", bindingResult);
            model.addAttribute("editedOrder", editedOrder);
            model.addAttribute("newOrder", new Order());
            model.addAttribute("showEditModal", true);

            return "orders";

        }

        redirectAttributes.addFlashAttribute("message", "editOrder");

        Order originalOrder = orderService.getOrder(editedOrder.getId());
        if (editedOrder.getStatus().equals("Pending")) {

            // Update order items and their products
            // Order originalOrder = orderService.getOrder(editedOrder.getId());
            List<OrderItem> editedOrderItems = editedOrder.getOrderItems();
            List<OrderItem> orginalOrderItems = originalOrder.getOrderItems();

            List<OrderItem> selectedOrderItems = new ArrayList<>();

            for (OrderItem editedOrderItem : editedOrderItems) {
                if (editedOrderItem.isSelected()) {
                    for (OrderItem originalOrderItem : orginalOrderItems) {
                        if (originalOrderItem.getProduct().getId().equals(editedOrderItem.getProduct().getId())) {

                            if (originalOrderItem.getQuantity() != editedOrderItem.getQuantity()) {
                                int difference = originalOrderItem.getQuantity() - editedOrderItem.getQuantity();
                                Product productToUpdate = originalOrderItem.getProduct();

                                if (difference > 0) {
                                    productToUpdate.setQuantity(productToUpdate.getQuantity() + difference);
                                } else {
                                    productToUpdate.setQuantity(productToUpdate.getQuantity() - Math.abs(difference));
                                }

                                productService.updateProduct(productToUpdate, user.getId());
                            }

                            // Add product information to edited product
                            editedOrderItem.setProduct(originalOrderItem.getProduct());
                        }
                    }

                    editedOrder.setTotalPrice(editedOrder.getTotalPrice()
                            + (editedOrderItem.getProduct().getPrice() * editedOrderItem.getQuantity()));
                    selectedOrderItems.add(editedOrderItem);
                } else {

                    // Return unselected items to the product quantity
                    for (OrderItem originalOrderItem : orginalOrderItems) {
                        if (originalOrderItem.getProduct().getId().equals(editedOrderItem.getProduct().getId())) {
                            Product productToUpdate = originalOrderItem.getProduct();
                            productToUpdate.setQuantity(productToUpdate.getQuantity() + editedOrderItem.getQuantity());
                            productService.updateProduct(productToUpdate, user.getId());
                        }
                    }
                }
            }

            editedOrder.setOrderItems(selectedOrderItems); 
        } else {
            editedOrder.setOrderItems(originalOrder.getOrderItems());
            editedOrder.setTotalPrice(originalOrder.getTotalPrice());
        }

        orderService.updateOrder(editedOrder);

        return "redirect:/orders";
    }

    @PostMapping(path = "/orders", params = "cancelOrder")
    public String cancelOrder(@RequestParam String cancelOrderId, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(cancelOrderId);
        String message = "";

        if (order.getStatus().equals("Pending")) {
            message = orderService.cancelOrder(order);
        } else {
            message = "cancelOrderFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);

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
            if (order.getCustomerName().toLowerCase().contains(searchQuery.toLowerCase())) {
                searchResults.add(order);
            }
        }

        redirectAttributes.addFlashAttribute("searchResults", searchResults);
        redirectAttributes.addFlashAttribute("searchQuery", searchQuery);

        return "redirect:/orders";
    }

}