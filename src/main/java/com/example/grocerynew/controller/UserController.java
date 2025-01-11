package com.example.grocerynew.controller;

import com.example.grocerynew.model.GroceryItem;
import com.example.grocerynew.model.Order;
import com.example.grocerynew.model.OrderItemRequest;
import com.example.grocerynew.model.User;
import com.example.grocerynew.service.GroceryItemService;
import com.example.grocerynew.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final GroceryItemService groceryItemService;
    private final OrderService orderService;

    public UserController(GroceryItemService groceryItemService, OrderService orderService) {
        this.groceryItemService = groceryItemService;
        this.orderService = orderService;
    }

    /**
     * Endpoint to view the list of available grocery items.
     * This is accessible only by users with ROLE_USER.
     */
    @GetMapping("/grocery-items")// Role-based access control
    public List<GroceryItem> getAvailableGroceryItems() {
        return orderService.getAvailableItems();
    }

    /**
     * Endpoint to place an order with multiple grocery items.
     * This is accessible only by users with ROLE_USER.
     */
    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody List<OrderItemRequest> orderItemRequests) {
        Order order = orderService.createOrder(orderItemRequests);
        return ResponseEntity.ok(order);
    }



}
