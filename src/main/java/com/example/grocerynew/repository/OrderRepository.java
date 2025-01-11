package com.example.grocerynew.repository;


import com.example.grocerynew.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

