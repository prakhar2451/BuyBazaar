package com.buybazaar.product_service.controller;
import com.buybazaar.product_service.dto.ProductDTO;
import com.buybazaar.product_service.entity.Product;
import com.buybazaar.product_service.exceptions.ResourceNotFoundExcpetion;
import com.buybazaar.product_service.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProductDTO = productService.createProduct(productDTO);
            logger.info("Product created successfully with ID: {}", createdProductDTO.getId());
            return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating product: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String productId, @RequestBody ProductDTO productDTO){
        try {
            ProductDTO updatedProduct = productService.updateProduct(productId,productDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundExcpetion e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
        try {
            Optional<ProductDTO> productDTO = productService.getProductById(productId);
            return productDTO.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        try {
            List<ProductDTO> products = productService.getAllProducts();
            logger.info("Fetched all products successfully");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all products: {}", e.getMessage(),e);
            return ResponseEntity.status(500).build();
        }

    }

    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        try {
            productService.deleteProduct(productId);
        } catch (ResourceNotFoundExcpetion e) {
            throw new ResourceNotFoundExcpetion("Product with given id {} not found.", "id",productId);
        }
    }

}