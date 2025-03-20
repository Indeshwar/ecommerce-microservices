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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;



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

        InventoryRequest request1 = new InventoryRequest();
        request1.setInventoryName("Iphone 15");
        request1.setCategory("IPhone");
        request1.setQuantity(4L);
        request1.setPrice(1000.0);

        InventoryRequest request2 = new InventoryRequest();
        request2.setInventoryName("Samsung Galaxy");
        request2.setCategory("Samsung");
        request2.setQuantity(4L);
        request2.setPrice(1000.0);

        //Create an Object of HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        //Set userName and authority in headers
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        //Create a HttpEntity Object
        HttpEntity<InventoryRequest> httpEntity_1 = new HttpEntity<>(request1, headers);
        HttpEntity<InventoryRequest> httpEntity_2 = new HttpEntity<>(request2, headers);
        ResponseEntity<String> response = null;
        for(int i = 0; i < 4; i++){
            if(i == 0){
                response = restTemplate.exchange(
                        baseUrl + "/save", HttpMethod.POST, httpEntity_1, String.class);
            }else{
                response = restTemplate.exchange(
                        baseUrl + "/save", HttpMethod.POST, httpEntity_2, String.class);
            }
        }

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("data successfully saved", response.getBody());

    }

    @Test
    void testFindInventoryByCategory(){
        InventoryRequest request = new InventoryRequest();
        request.setCategory("IPhone");

        HttpHeaders headers = new HttpHeaders();
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        HttpEntity httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(baseUrl + "/category", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Inventory>>() {
        });

        assertEquals(1, response.getBody().size());
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

    @Test
    void TestFindByInventoryId(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("userName", "Hari");
        headers.set("authority", "USER");

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Inventory> response = restTemplate.exchange(baseUrl + "/inventoryId/79b85b84-8a8e-4d07-a56d-8640e9392d9a", HttpMethod.GET, httpEntity, Inventory.class);
        System.out.println("HELLO " + response.getBody().getInventoryName());
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }
}
