package com.example.grocerynew.service;

import com.example.grocerynew.model.GroceryItem;
import com.example.grocerynew.repository.GroceryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryItemService {

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    public Optional<GroceryItem> getGroceryItemById(Long id) {
        return groceryItemRepository.findById(id);
    }

    public GroceryItem addGroceryItem(GroceryItem groceryItem) {
        return groceryItemRepository.save(groceryItem);
    }

    public GroceryItem updateGroceryItem(Long id, GroceryItem groceryItem) {
        if (!groceryItemRepository.existsById(id)) {
            return null;
        }
        GroceryItem groceryItem1 = new GroceryItem();
        groceryItem1.setId(id);
        groceryItem1.setName(groceryItem.getName());

        return groceryItemRepository.save(groceryItem);
    }

    public boolean deleteGroceryItem(Long id) {
        if (!groceryItemRepository.existsById(id)) {
            return false;
        }
        groceryItemRepository.deleteById(id);
        return true;
    }

    public GroceryItem updateInventory(Long id, int inventoryLevel) {
        Optional<GroceryItem> itemOptional = groceryItemRepository.findById(id);
        if (itemOptional.isPresent()) {
            GroceryItem item = itemOptional.get();
            item.setInventoryLevel(inventoryLevel);
            return groceryItemRepository.save(item);
        }
        return null;
    }

    // Save the grocery item (used after updating quantity)
    public GroceryItem saveGroceryItem(GroceryItem groceryItem) {
        return groceryItemRepository.save(groceryItem);
    }


}

