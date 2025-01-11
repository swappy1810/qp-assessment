package com.example.grocerynew.controller;

import com.example.grocerynew.model.GroceryItem;
import com.example.grocerynew.model.InventoryUpdateRequest;
import com.example.grocerynew.repository.GroceryItemRepository;
import com.example.grocerynew.service.GroceryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    private final GroceryItemService groceryItemService;

    public AdminController(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    @PostMapping("/groceryitems")
    public ResponseEntity<GroceryItem> addGroceryItem(@RequestBody GroceryItem groceryItem) {
        groceryItemService.addGroceryItem(groceryItem);
        return ResponseEntity.status(201).body(groceryItem);
    }

    @GetMapping("/groceryitems")
    public ResponseEntity<?> getAllGroceryItems() {
        return ResponseEntity.ok(groceryItemService.getAllGroceryItems());
    }

    @GetMapping("/groceryitems/{id}")
    public ResponseEntity<GroceryItem> getGroceryItemById(@PathVariable Long id) {
        Optional<GroceryItem> groceryItem = groceryItemService.getGroceryItemById(id);

        if (groceryItem.isPresent()) {
            return ResponseEntity.ok(groceryItem.get());  // Return the found grocery item
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if item not found
        }
    }

//    // Endpoint to update inventory level (quantity) of a grocery item (only for admin)
//    @PutMapping("/{id}/inventory")
//    public ResponseEntity<GroceryItem> updateInventory(@PathVariable Long id, @RequestBody InventoryUpdateRequest updateRequest) {
//        Optional<GroceryItem> groceryItemOptional = groceryItemService.getGroceryItemById(id);
//
//        if (groceryItemOptional.isPresent()) {
//            GroceryItem groceryItem = groceryItemOptional.get();
//            groceryItem.setInventoryLevel(updateRequest.getInventoryLevel());  // Update the quantity
//            groceryItemService.saveGroceryItem(groceryItem);  // Save the updated item
//
//            return ResponseEntity.ok(groceryItem);  // Return updated grocery item
//        } else {
//            return ResponseEntity.notFound().build();  // Return 404 if item not found
//        }
//    }

    // Endpoint to update the grocery item's name, price, and quantity (only for admin)
    @PutMapping("/{id}")
    public ResponseEntity<GroceryItem> updateGroceryItem(@PathVariable Long id, @RequestBody GroceryItem updateRequest) {
        Optional<GroceryItem> groceryItemOptional = groceryItemService.getGroceryItemById(id);

        if (groceryItemOptional.isPresent()) {
            GroceryItem groceryItem = groceryItemOptional.get();
            groceryItem.setName(updateRequest.getName());
            groceryItem.setPrice(updateRequest.getPrice());
            groceryItem.setInventoryLevel(updateRequest.getInventoryLevel());

            groceryItemService.saveGroceryItem(groceryItem);  // Save the updated grocery item

            return ResponseEntity.ok(groceryItem);  // Return updated grocery item
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if item not found
        }
    }

    @DeleteMapping("groceryitems/{id}")
    public ResponseEntity<?> deleteGroceryItem(@PathVariable Long id) {
        if (!groceryItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        groceryItemService.deleteGroceryItem(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("groceryitems/{id}/inventory")
    public ResponseEntity<GroceryItem> updateInventory(@PathVariable Long id, @RequestBody int quantity) {
        GroceryItem item = groceryItemRepository.findById(id).orElseThrow();
        item.setInventoryLevel(quantity);
        return ResponseEntity.ok(groceryItemRepository.save(item));
    }
}

