package com.buybazaar.product_service.service.serviceimpl;

import com.buybazaar.product_service.dto.InventoryDTO;
import com.buybazaar.product_service.dto.ProductDTO;
import com.buybazaar.product_service.entity.Product;
import com.buybazaar.product_service.exceptions.ResourceNotFoundExcpetion;
import com.buybazaar.product_service.repository.ProductRepository;
import com.buybazaar.product_service.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final String INVENTORY_SERVICE_URL = "http://localhost:8082/api/inventory/updateFromProduct";



    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            // Map ProductDTO to Product entity
            Product product = modelMapper.map(productDTO, Product.class);

            // Save the product in the repository
            Product savedProduct = productRepository.save(product);

            // Map the saved product to ProductDTO for response
            ProductDTO createdProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

            // Log product creation success
            logger.info("Product created successfully with ID: {}", createdProductDTO.getProductId());
            // Update or create inventory for the new product
            autoUpdateInventory(createdProductDTO);

            // Return the created product DTO
            return createdProductDTO;
        } catch (Exception e) {
            // Log error details
            logger.error("Error occurred while creating product: {}", e.getMessage(), e);

            // Optionally, you might throw a custom exception or handle it differently
            throw new RuntimeException("Error occurred while creating product.", e);
        }
    }
    private void autoUpdateInventory(ProductDTO productDTO) {
        try {
            // Define the URL for the inventory service endpoint
            String url = "http://localhost:8082/api/inventory/createOrUpdateInventory";

            // Create an InventoryDTO with correct values
            InventoryDTO inventoryDTO = new InventoryDTO(
                    productDTO.getProductId(),  // Set product ID
                    productDTO.getProductName(), // Set product name
                    0                           // Initialize quantity
            );

            logger.info("Fetched ProductDTO: productId={}, productName={}",
                    productDTO.getProductId(), productDTO.getProductName());

            // Send a POST request to update or create inventory
            ResponseEntity<Void> response = restTemplate.postForEntity(url, inventoryDTO, Void.class);

            // Check if the request was successful
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Inventory updated/created successfully for product ID: {}", productDTO.getProductId());
            } else {
                logger.warn("Failed to update/create inventory for product ID: {}. Status code: {}",
                        productDTO.getProductId(), response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating/creating inventory for product ID: {}",
                    productDTO.getProductId(), e);
            throw new RuntimeException("Error occurred while updating/creating inventory.", e);
        }
    }

    @Override
    public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
        try {
            Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExcpetion("Product", "ID", productId));
            existingProduct.setProductName(productDTO.getProductName());
            existingProduct.setCategory(productDTO.getCategory());
            existingProduct.setSubCategory(productDTO.getSubCategory());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setUpdatedAt(LocalDateTime.now());

            Product updatedProduct = productRepository.save(existingProduct);
            ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
            logger.info("Updated product with ID: {}", productId);

            // Update inventory
            autoUpdateInventory(updatedProductDTO);

            return updatedProductDTO;
        } catch (ResourceNotFoundExcpetion e) {
            logger.error("Product not found with given ID: {}", productId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while updating product ID:{}", productId, e);
            throw new RuntimeException("Error occurred while updating product.");
        }
    }

    @Override
    public Optional<ProductDTO> getProductById(String productId) {
        try{
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExcpetion("Product", "ID",productId));

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            logger.info("Fetched product with ID: {}", productId);
            return Optional.of(productDTO);
        } catch (ResourceNotFoundExcpetion e) {
            logger.error("Product not found with ID: {}", productId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching product with ID: {}", productId, e);
            throw new RuntimeException("Error occurred while fetching product.");
        }
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        try {
            List<Product> products = productRepository.findByCategory(category);
            List<ProductDTO> productDTOs = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            logger.info("Fetched {} products for category {}", productDTOs.size(), category);
            return productDTOs;
        } catch (Exception e) {
            logger.error("Error occurred while fetching products for category {}: {}", category, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching products.");
        }
    }

    @Override
    public List<ProductDTO> getProductsBySubCategory(String subCategory) {

        try {
            List<Product> products = productRepository.findBySubCategory(subCategory);
            List<ProductDTO> productDTOs = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            logger.info("Fetched {} products for subcategory {}", productDTOs.size(), subCategory);
            return productDTOs;
        } catch (Exception e) {
            logger.error("Error occurred while fetching products for category {}: {}", subCategory, e.getMessage());
            throw new RuntimeException("Error occurred while fetching products.");
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOs = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            logger.info("fetched all products successfully");
            return productDTOs;
        }catch (Exception e) {
            logger.error("Error occurred while fetching all products {}", e.getMessage(),e);
            throw new RuntimeException("Error occurred while fetching all products");
        }
    }

    @Override
    public void deleteProduct(String productId) {

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(()-> new ResourceNotFoundExcpetion("Product","ID", productId));
            productRepository.delete(product);
            logger.info("Deleted product with ID: {}", productId);
        } catch (ResourceNotFoundExcpetion e) {
            logger.error("Product not found with ID: {}", productId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting product.");
        }

    }
}
