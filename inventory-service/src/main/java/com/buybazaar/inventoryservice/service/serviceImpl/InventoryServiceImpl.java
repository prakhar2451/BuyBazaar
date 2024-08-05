package com.buybazaar.inventoryservice.service.serviceImpl;

import com.buybazaar.inventoryservice.dto.InventoryDTO;
import com.buybazaar.inventoryservice.dto.ProductDTO;
import com.buybazaar.inventoryservice.entity.Inventory;
import com.buybazaar.inventoryservice.repository.InventoryRepository;
import com.buybazaar.inventoryservice.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {


    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public boolean isProductInStock(String productId) {
        logger.info("Checking stock for productId: {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(null);

        if (inventory == null || inventory.getQuantity() <= 0) {
            logger.info("Product not in stock: {}", productId);
            return false;
        }

        logger.info("Product is in stock: {}", productId);
        return true;
    }

    @Override
    @Transactional
    public void updateStock(String productId, int quantity) {
        try {
            logger.info("Updating stock for product with ID {} to quantity {}", productId, quantity);
            Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
            if (inventoryOptional.isPresent()) {
                Inventory inventory = inventoryOptional.get();
                inventory.setQuantity(quantity);
                inventory.setUpdatedAt(LocalDateTime.now());
                inventoryRepository.save(inventory);
                logger.info("Stock updated successfully for product with ID {}", productId);
            } else {
                logger.warn("Product with ID {} not found", productId);
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating stock for product with ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Error updating product stock", e);
        }
    }

    @Override
    public InventoryDTO getInventoryByProductId(String productId) {
        try {
            logger.info("Fetching inventory for product with ID {}", productId);
            Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
            if (inventoryOptional.isPresent()) {
                Inventory inventory = inventoryOptional.get();
                InventoryDTO inventoryDTO = modelMapper.map(inventory, InventoryDTO.class);
                logger.info("Inventory fetched successfully for product with ID {}", productId);
                return inventoryDTO;
            } else {
                logger.warn("Product with ID {} not found", productId);
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching inventory for product with ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Error fetching product inventory", e);
        }
    }

    @Override
    public void updateInventoryFromProduct(InventoryDTO inventoryDTO) {
        try {
            String productServiceUrl = "http://localhost:8080/api/products/" + inventoryDTO.getProductId();
            ProductDTO productDTO = restTemplate.getForObject(productServiceUrl, ProductDTO.class);

            if (productDTO == null) {
                throw new RuntimeException("Product not found");
            }

            Inventory inventory = inventoryRepository.findByProductId(inventoryDTO.getProductId())
                    .orElseGet(() -> new Inventory(
                            inventoryDTO.getProductId(),
                            inventoryDTO.getProductName(),
                            inventoryDTO.getQuantity()
                    ));

            inventory.setProductName(inventoryDTO.getProductName());
            inventory.setQuantity(inventoryDTO.getQuantity());
            inventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
            logger.info("Inventory updated from product data for product ID: {}", inventoryDTO.getProductId());
        } catch (Exception e) {
            logger.error("Error occurred while updating inventory from product data: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating inventory from product data");
        }
    }

    @Override
    public void createOrUpdateInventory(InventoryDTO inventoryDTO) {
        try {
            logger.info("Received InventoryDTO: productId={}, productName={}, quantity={}",
                    inventoryDTO.getProductId(), inventoryDTO.getProductName(), inventoryDTO.getQuantity());

            Optional<Inventory> existingInventoryOptional = inventoryRepository.findByProductId(inventoryDTO.getProductId());
            if (existingInventoryOptional.isPresent()) {
                Inventory existingInventory = existingInventoryOptional.get();
                existingInventory.setProductId(inventoryDTO.getProductId());
                existingInventory.setProductName(inventoryDTO.getProductName());
                existingInventory.setQuantity(inventoryDTO.getQuantity());
                existingInventory.setUpdatedAt(LocalDateTime.now());
                inventoryRepository.save(existingInventory);
                logger.info("Updated InventoryDTO: productId={}, productName={}, quantity={}",
                        inventoryDTO.getProductId(), inventoryDTO.getProductName(), inventoryDTO.getQuantity());
                logger.info("Updated inventory for product ID: {}", inventoryDTO.getProductId());
            } else {
                Inventory newInventory = new Inventory();
                newInventory.setProductId(inventoryDTO.getProductId());
                newInventory.setProductName(inventoryDTO.getProductName());
                newInventory.setQuantity(inventoryDTO.getQuantity());
                newInventory.setCreatedAt(LocalDateTime.now());
                newInventory.setUpdatedAt(LocalDateTime.now());
                inventoryRepository.save(newInventory);
                logger.info("Created New InventoryDTO: productId={}, productName={}, quantity={}",
                        inventoryDTO.getProductId(), inventoryDTO.getProductName(), inventoryDTO.getQuantity());
                logger.info("Created new inventory for product ID: {}", inventoryDTO.getProductId());
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating/updating inventory for product ID: {}",
                    inventoryDTO.getProductId(), e);
            throw new RuntimeException("Error occurred while creating/updating inventory.", e);
        }
    }
}
