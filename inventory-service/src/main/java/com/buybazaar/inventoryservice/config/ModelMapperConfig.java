package com.buybazaar.inventoryservice.config;

import com.buybazaar.inventoryservice.dto.InventoryDTO;
import com.buybazaar.inventoryservice.entity.Inventory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(InventoryDTO.class, Inventory.class).addMappings(mapper -> {
            mapper.map(InventoryDTO::getProductId, Inventory::setProductId);
            mapper.map(InventoryDTO::getProductName, Inventory::setProductName);
            mapper.map(InventoryDTO::getQuantity, Inventory::setQuantity);
            mapper.map(InventoryDTO::getCreatedAt, Inventory::setCreatedAt);
            mapper.map(InventoryDTO::getUpdatedAt, Inventory::setUpdatedAt);
        });
        return modelMapper;
    }

}
