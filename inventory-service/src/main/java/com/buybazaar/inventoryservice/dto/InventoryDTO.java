package com.buybazaar.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private String productId;
    private String productName;
    private int quantity;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

}
