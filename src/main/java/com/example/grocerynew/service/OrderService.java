package com.example.grocerynew.service;

import com.example.grocerynew.model.GroceryItem;
import com.example.grocerynew.model.Order;
import com.example.grocerynew.model.OrderItemRequest;
import com.example.grocerynew.model.User;
import com.example.grocerynew.repository.GroceryItemRepository;
import com.example.grocerynew.repository.OrderItemRepository;
import com.example.grocerynew.repository.OrderRepository;
import com.example.grocerynew.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Autowired
    private GroceryItemService groceryItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(List<OrderItemRequest> orderItemRequests) {
        User currentUser = userService.getCurrentUser();  // Get the current logged-in user

        // Fetch all grocery items needed for the order
        List<GroceryItem> groceryItems = groceryItemRepository.findAllById(
                orderItemRequests.stream().map(OrderItemRequest::getGroceryItemId).toList()
        );

        // Check if there are enough items in stock and calculate the total price
        double totalPrice = 0;
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            Optional<GroceryItem> groceryItemOptional = groceryItems.stream()
                    .filter(g -> g.getId().equals(orderItemRequest.getGroceryItemId()))
                    .findFirst();

            if (groceryItemOptional.isEmpty()) {
                throw new RuntimeException("Grocery item with ID " + orderItemRequest.getGroceryItemId() + " not found.");
            }

            GroceryItem groceryItem = groceryItemOptional.get();

            if (groceryItem.getInventoryLevel() < orderItemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + groceryItem.getName());
            }

            totalPrice += groceryItem.getPrice() * orderItemRequest.getQuantity();
            groceryItem.setInventoryLevel(groceryItem.getInventoryLevel() - orderItemRequest.getQuantity());  // Update stock
        }

        // Create and save the order
        Order order = new Order();
        order.setUser(currentUser);
        order.setTotalPrice(totalPrice);
        order.setGroceryItems(groceryItems);  // Associate grocery items with the order
        orderRepository.save(order);

        return order;
    }

    // Method to get available items based on inventory levels
    public List<GroceryItem> getAvailableItems() {
        List<GroceryItem> groceryList = groceryItemRepository.findAll();
        // Filter and return only items with inventory level > 0
        return groceryList.stream()
                .filter(item -> item.getInventoryLevel() > 0)
                .collect(Collectors.toList());
    }

}

