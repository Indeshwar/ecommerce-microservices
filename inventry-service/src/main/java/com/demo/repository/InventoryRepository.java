package com.demo.repository;

import com.demo.entity.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InventoryRepository {
    private static final Logger log = LoggerFactory.getLogger(InventoryRepository.class);
    private final DynamoDbTable<Inventory> inventoryTable;


    public InventoryRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.inventoryTable = dynamoDbEnhancedClient.table("inventory", TableSchema.fromBean(Inventory.class));
    }

    public void saveInventory(Inventory inventory){
       inventoryTable.putItem(inventory);
    }

    public List<Inventory> findAll() {
        return  inventoryTable.scan().items().stream().collect(Collectors.toList());
    }



    public Optional<Inventory> findById(String inventoryId){
        return  Optional.ofNullable(inventoryTable.getItem(r->r.key(k-> k.partitionValue(inventoryId))));
    }

    public void deleteInventory(String inventoryId){
         inventoryTable.deleteItem(r->r.key(k->k.partitionValue(inventoryId)));
    }



    public List<Inventory> findInventoryByCategoryAndCreatedAt(String category, String createdAt){
        String indexName = "CategoryIndex";
        //Call method to create query condition
        QueryConditional queryConditional = createQueryConditional(category);
        //Invoke method to Create a filter expression
        Expression expression = createExpression(createdAt);

        // Execute the query on the GSI
        List<Inventory> inventories = new ArrayList<>();
        inventoryTable.index(indexName).query(QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .build())
                .stream()
                .forEach(page-> inventories.addAll(page.items()));

        return  inventories;

    }


    public List<Inventory> findInventoryByCategory(String category){
        List<Inventory> inventories = new ArrayList<>();
        inventories = inventoryTable.scan().items().stream().filter(inventory -> inventory.getCategory().equals(category)).toList();

        System.out.println("size is " + inventories.size());
        return  inventories;
    }

    //Create a  query condition for partition key
    private QueryConditional createQueryConditional(String partitionValue){
        return QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(partitionValue)
                .build());
    }

    //Create a filter expression for createdAt > sortKeyValue
    private Expression createExpression(String sortKeyValue){
        return  Expression.builder()
                .expression("createdAt> :createdVal")
                .putExpressionValue(":createdVal", AttributeValue.builder()
                        .s(sortKeyValue).build()) //bind the createdVal with original sortKeyValue
                .build();
    }


}
