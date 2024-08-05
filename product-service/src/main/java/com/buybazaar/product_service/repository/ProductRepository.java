package com.buybazaar.product_service.repository;

import com.buybazaar.product_service.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByProductName(String productName);
    Optional<Product> findById(String productId);
    List<Product> findByCategory(String category);

    List<Product> findBySubCategory(String subCategory);
}
