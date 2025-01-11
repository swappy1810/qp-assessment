package com.example.grocerynew.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "orders_new")
    public class Order {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @ManyToOne
            @JoinColumn(name = "user_id")
            private User user;

            @ManyToMany
            @JoinTable(
                    name = "order_grocery_item",
                    joinColumns = @JoinColumn(name = "order_id"),
                    inverseJoinColumns = @JoinColumn(name = "grocery_item_id")
            )
            private List<GroceryItem> groceryItems;

            private double totalPrice;

            // Getters and setters
            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }

            public List<GroceryItem> getGroceryItems() {
                return groceryItems;
            }

            public void setGroceryItems(List<GroceryItem> groceryItems) {
                this.groceryItems = groceryItems;
            }

            public double getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
            }
        }


