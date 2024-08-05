package com.buybazaar.inventoryservice.controller;

import com.buybazaar.inventoryservice.dto.InventoryDTO;
import com.buybazaar.inventoryservice.exception.ProductNotFoundException;
import com.buybazaar.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProductId(@PathVariable String productId) {
        try {
            InventoryDTO inventoryDTO = inventoryService.getInventoryByProductId(productId);
            return ResponseEntity.ok(inventoryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/updateFromProduct")
    public ResponseEntity<String> updateInventoryFromProduct(@RequestBody InventoryDTO inventoryDTO) {
        try {
            inventoryService.updateInventoryFromProduct(inventoryDTO);
            return ResponseEntity.ok("Inventory updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error updating inventory");
        }
    }


    @PutMapping("/updateStock")
    public ResponseEntity<String> updateStock(@RequestParam String productId, @RequestParam int quantity) {
        try {
            logger.info("Received request to update stock for productId: {}, quantity: {}", productId, quantity);
            inventoryService.updateStock(productId, quantity);
            logger.info("Stock updated successfully for productId: {}", productId);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (ProductNotFoundException e) {
            logger.error("ProductNotFoundException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update stock");
        }
    }

    @GetMapping("/isProductInStock")
    public ResponseEntity<Boolean> isProductInStock(@RequestParam String productId) {
        try {
            logger.info("Received request to check stock for productId: {}", productId);
            boolean isInStock = inventoryService.isProductInStock(productId);
            logger.info("Stock check for productId: {} returned: {}", productId, isInStock);
            return ResponseEntity.ok(isInStock);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/createOrUpdateInventory")
    public ResponseEntity<String> createOrUpdateInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            inventoryService.createOrUpdateInventory(inventoryDTO);
            return ResponseEntity.ok("Inventory created/updated successfully");
        } catch (Exception e) {
            logger.error("Error creating/updating inventory", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating/updating inventory");
        }
    }
}
