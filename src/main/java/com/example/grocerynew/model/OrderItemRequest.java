package com.example.grocerynew.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderItems")
public class OrderItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
                private Long groceryItemId;
                private int quantity;

                // Getters and setters
                public Long getGroceryItemId() {
                        return groceryItemId;
                }

                public void setGroceryItemId(Long groceryItemId) {
                        this.groceryItemId = groceryItemId;
                }

                public int getQuantity() {
                        return quantity;
                }

                public void setQuantity(int quantity) {
                        this.quantity = quantity;
                }
        }
