package com.example.grocerynew.repository;


import com.example.grocerynew.model.OrderItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemRequest, Long> {
}
