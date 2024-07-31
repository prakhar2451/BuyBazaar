package com.buybazaar.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String id;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Sub-category is required")
    private String subCategory;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private Double price;

    @NotNull(message = "Product quantity is required")
    @PositiveOrZero(message = "Product quantity must be zero or positive")
    private Integer quantity;
}