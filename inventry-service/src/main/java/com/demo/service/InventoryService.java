package com.demo.service;

import com.demo.dto.InventoryRequest;
import com.demo.entity.Inventory;
import com.demo.exception.InventoryNotFoundException;
import com.demo.filter.JWTVerifierFilter;
import com.demo.repository.InventoryRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    @Autowired
    private InventoryRepository inventoryRepository;

    public String saveInventory(InventoryRequest request) {
        log.info("inventory name is : {}", request.getInventoryName());
        Inventory inventory = new Inventory();
        inventory.setInventoryId(UUID.randomUUID().toString());
        inventory.setInventoryName(request.getInventoryName());
        inventory.setCategory(request.getCategory());
        inventory.setQuantity(request.getQuantity());
        inventory.setPrice(request.getPrice());
        inventoryRepository.saveInventory(inventory);
        return "data successfully saved";
    }



    public Inventory findInventoryById(String inventoryId){
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if(inventory.isEmpty()){
            throw new InventoryNotFoundException(inventoryId +  "Not found");
        }
        return inventory.get();
    }

    public List<Inventory> getAllInventory() {
       return inventoryRepository.findAll();
    }
}
