package com.demo.repository;

import com.demo.entity.Inventory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InventoryRepository {
    private final DynamoDbTable<Inventory> inventoryTable;

    public InventoryRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.inventoryTable = dynamoDbEnhancedClient.table("inventory", TableSchema.fromBean(Inventory.class));
    }

    public void saveInventory(Inventory inventory){
       inventoryTable.putItem(inventory);
    }

    public Optional<Inventory> findById(String inventoryId){
        return  Optional.ofNullable(inventoryTable.getItem(r->r.key(k-> k.partitionValue(inventoryId))));
    }


    public List<Inventory> findAll() {
        return  inventoryTable.scan().items().stream().collect(Collectors.toList());
    }
}
