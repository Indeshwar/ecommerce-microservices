package com.demo.controller;

import com.demo.config.TestSecurityConfig;
import com.demo.dto.InventoryRequest;
import com.demo.entity.Inventory;
import com.demo.service.InventoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TestInventoryController {
    @Autowired
    InventoryService inventoryService;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost:";
    private static RestTemplate restTemplate;

    @BeforeAll
    public static  void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setup(){
        baseUrl = baseUrl + port + "/api/v1";
    }
    @Test
    void testSaveInventory(){

        InventoryRequest request = new InventoryRequest();
        request.setInventoryName("Iphone");
        request.setCategory("Phone");
        request.setQuantity(4L);
        request.setPrice(1000.0);

        //Create an Object of HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        //Set userName and authority in headers
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        //Create a HttpEntity Object
        HttpEntity<InventoryRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/save", HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("data successfully saved", response.getBody());



    }

    @Test
    void testGetMessage(){

        //Create an Object of HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        //Set userName and authority in headers
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        //Create a HttpEntity Object
        HttpEntity entityRequest = new HttpEntity(headers);


        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/message", HttpMethod.GET, entityRequest, String.class);


        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }

    @Test
    void  TestGetAllInventory(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<List<Inventory>> response = restTemplate.exchange(baseUrl + "/inventory", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Inventory>>() {
        });

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
