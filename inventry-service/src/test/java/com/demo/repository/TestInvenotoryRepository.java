package com.demo.repository;

import com.demo.entity.Inventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.UUID;

@SpringBootTest
public class TestInvenotoryRepository {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private DynamoDbClient dynamoDbClient;



    @Test
    void testSaveInventory(){
        Inventory inventory = new Inventory();
        inventory.setInventoryId(UUID.randomUUID().toString());
        inventory.setInventoryName("Dove Shampoo");
        inventory.setCategory("Shampoo");
        inventory.setQuantity(5L);
        inventory.setPrice(5.00);

        inventoryRepository.saveInventory(inventory);
    }

    @Test
    void testDeleteTable(){
        String tableName = "user";
        try{
            DeleteTableRequest deleteTableRequest = DeleteTableRequest.builder().tableName(tableName).build();
            dynamoDbClient.deleteTable(deleteTableRequest);
        }catch (ResourceNotFoundException e){
            System.out.println("Table " + tableName + " does not exist");
        }
    }

}
