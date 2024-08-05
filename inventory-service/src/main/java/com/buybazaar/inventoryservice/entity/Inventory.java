package com.buybazaar.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt ;

    public Inventory(String productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.createdAt= getCreatedAt();
        this.updatedAt = getUpdatedAt();
    }
}