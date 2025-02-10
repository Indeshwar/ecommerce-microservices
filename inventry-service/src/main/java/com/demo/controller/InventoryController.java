package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InventoryController {
    @GetMapping("/message")
    public String getMessage(){
        return "This is Inventory Service";
    }

    @GetMapping("/inventory")
    public String getInventory(){
        return "phone, laptop";
    }
}
