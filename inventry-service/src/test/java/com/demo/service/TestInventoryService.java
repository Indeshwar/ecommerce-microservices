package com.demo.service;

import com.demo.dto.InventoryRequest;
import com.demo.entity.Inventory;
import com.demo.repository.InventoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestInventoryService {
    @Mock //Mock the inventoryRepository
    private InventoryRepository inventoryRepository;
    @InjectMocks //Inject the mock inventoryRepository in inventoryService
    private InventoryService inventoryService;
    @Test

    void test(){
        System.out.println("Test");
    }

    @Test
    void testSaveInventory(){
        InventoryRequest request = new InventoryRequest();
        request.setInventoryName("Sun Silk Shampoo");
        request.setCategory("Shampoo");
        request.setPrice(5.00);
        request.setQuantity(5L);

        //Call the method
        inventoryService.saveInventory(request);

        //Assert (Verify the behaviour)
        verify(inventoryRepository, times(1)).saveInventory(any(Inventory.class));


    }

    @Test
    void testGetAllInventory(){
        List<Inventory> inventories = new ArrayList<>();
        Inventory i1 = new Inventory();
        i1.setInventoryId(UUID.randomUUID().toString());
        Inventory i2 = new Inventory();
        i1.setInventoryId(UUID.randomUUID().toString());
        inventories.add(i1);
        inventories.add(i2);
        //Mock the InventoryRepository
        when(inventoryRepository.findAll()).thenReturn(inventories);

        //call the method
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        //assert
        assertEquals(2, inventoryList.size());
        //verify
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void testFindInventoryByCategory(){
        List<Inventory> inventories = new ArrayList<>();
        Inventory i1 = new Inventory();
        i1.setInventoryId(UUID.randomUUID().toString());
        i1.setCategory("Iphone");
        Inventory i2 = new Inventory();
        i2.setInventoryId(UUID.randomUUID().toString());
        i2.setCategory("Iphone");
        Inventory i3 = new Inventory();
        i3.setInventoryId(UUID.randomUUID().toString());
        i3.setCategory("Samsung");

        //Adding inventories in list
        inventories.add(i1);
        inventories.add(i2);
        inventories.add(i3);

        //Mock the inventory repository
        when(inventoryRepository.findInventoryByCategory(any(String.class))).thenReturn(inventories);

        //Call the method
        List<Inventory> result = inventoryService.findInventoryByCategory("Iphone");
        //assert
        assertEquals(2, result.size());
        //verify
        verify(inventoryRepository, times(1)).findInventoryByCategory(any(String.class));



    }

}
