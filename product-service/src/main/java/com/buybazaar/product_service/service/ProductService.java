package com.buybazaar.product_service.service;

import com.buybazaar.product_service.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(String productId, ProductDTO productDTO);
    Optional<ProductDTO> getProductById(String productId);
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> getProductsBySubCategory(String subCategory);
    List<ProductDTO> getAllProducts();
    void deleteProduct(String productId);

}
