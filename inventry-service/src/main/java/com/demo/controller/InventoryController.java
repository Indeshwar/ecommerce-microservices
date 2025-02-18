package com.demo.controller;

import com.demo.dto.InventoryRequest;
import com.demo.entity.Inventory;
import com.demo.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/message")
    public String getMessage(){
        return "This is Inventory Service";
    }


    @PostMapping("/save")
    public ResponseEntity<String> saveInventory(@Valid @RequestBody InventoryRequest request){
        String message = inventoryService.saveInventory(request);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllInventory(){
        List<Inventory> inventories = inventoryService.getAllInventory();
        return  new ResponseEntity<>(inventories, HttpStatus.OK);
    }


}
