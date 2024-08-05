package com.buybazaar.inventoryservice.service;

import com.buybazaar.inventoryservice.dto.InventoryDTO;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    boolean isProductInStock(String productId);
    void updateStock(String productId, int quantity);
    InventoryDTO getInventoryByProductId(String productId);
    void updateInventoryFromProduct(InventoryDTO inventoryDTO);
    void createOrUpdateInventory(InventoryDTO inventoryDTO);
}
