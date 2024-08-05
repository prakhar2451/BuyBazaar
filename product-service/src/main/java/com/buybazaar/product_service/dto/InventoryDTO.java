package com.buybazaar.product_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryDTO {
    private String productId;
    private String productName;
    private Integer quantity;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    public InventoryDTO(String productId, String productName, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
    }
}
