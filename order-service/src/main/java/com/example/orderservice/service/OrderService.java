package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(String id);
    List<Order> getOrdersByCustomerId(String customerId);
    Order createOrder(OrderRequest orderRequest);
    Order updateOrderStatus(String id, String status);
    void deleteOrder(String id);
}
