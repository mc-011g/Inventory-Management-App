package com.example.inventoryapp.service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.inventoryapp.model.Order;
import com.example.inventoryapp.model.OrderItem;
import com.example.inventoryapp.model.User;
import com.example.inventoryapp.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(String id) {
        return orderRepository.findOrderById(id);
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<Order>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                User user = (User) authentication.getPrincipal();
                orders = getUserOrders(user.getId());
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                orders = getAllOrders();
            }
        }

        return orders;
    }

    // Automatically update order status after 5 minutes
    @Scheduled(fixedRate = 300000)
    public void updatePendingOrderStatus() {

        List<Order> pendingOrders = orderRepository.findByStatus("Pending");

        if (pendingOrders != null) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            for (Order order : pendingOrders) {
                Date orderCreationDate;
                try {
                    orderCreationDate = dateFormat.parse(order.getCreatedAt());
                    long millisecondDifference = Math.abs(currentDate.getTime() - orderCreationDate.getTime());
                    long minuteDifference = millisecondDifference / (60 * 1000);

                    if (order.getStatus().equals("Pending") && minuteDifference >= 5) {
                        order.setStatus("Processed");
                        orderRepository.save(order);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTotalSalesValue(String userId) {
        List<Order> orders = getUserOrders(userId);
        double totalSalesValue = 0;

        for (Order order : orders) {
            if (order.getStatus().equals("Processed")) {
                for (OrderItem orderItem : order.getOrderItems()) {
                    totalSalesValue += orderItem.getProduct().getPrice() * orderItem.getQuantity();
                }
            }
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(totalSalesValue);
    }

    public String createNewOrder(Order newOrder) {
        newOrder.setId(UUID.randomUUID().toString());
        orderRepository.save(newOrder);
        return "addOrder";
    }

    public void updateOrder(Order order) {
        order.setUpdatedAt(new Date().toString());
        orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}