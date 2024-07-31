package com.buybazaar.product_service.service.serviceimpl;

import com.buybazaar.product_service.dto.ProductDTO;
import com.buybazaar.product_service.entity.Product;
import com.buybazaar.product_service.exceptions.ResourceNotFoundExcpetion;
import com.buybazaar.product_service.repository.ProductRepository;
import com.buybazaar.product_service.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            Product product = modelMapper.map(productDTO, Product.class);
            Product savedProduct = productRepository.save(product);
            ProductDTO createdProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
            logger.info("Product created successfully.");
            return createdProductDTO;
        } catch (Exception e) {
            logger.error("Error occurred while creating product: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while creating product.");
        }
    }

    @Override
    public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
        try{
            Product existingProduct = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundExcpetion("Product","ID",productId));
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setQuantity(productDTO.getQuantity());

            existingProduct.setUpdatedAt(LocalDateTime.now());

            Product updatedProduct = productRepository.save(existingProduct);
            ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
            logger.info("Updated product with ID: {}",productId);
            return updatedProductDTO;
        } catch (ResourceNotFoundExcpetion e) {
            logger.error("Product not found with given ID: {}",productId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while updating user ID:{}",productId, e);
            throw new RuntimeException("Error occurred while updating user.");
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
            logger.info("Deleted product with ID: {}", productId);;
        } catch (ResourceNotFoundExcpetion e) {
            logger.error("Product not found with ID: {}", productId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting product.");
        }

    }
}
