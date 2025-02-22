package com.demo.config;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

public class DynamoDbTableCreator {
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbTableCreator(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public  void createTableInventory(){
        String tableName = "inventory";

        //Check if table already exist
        try{
            //Fetch the metadata of specific dynamoDb table
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName(tableName).build());
            System.out.println("Table " + tableName + " is already exist");
            return;
        }catch (ResourceNotFoundException e){
            System.out.println("Table " + tableName + " is creating");
        }

        CreateTableRequest tableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("inventoryId")  // Primary Key
                                .keyType(KeyType.HASH)
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("inventoryId") // Primary Key attribute
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("category") // GSI Partition Key
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("createdAt") // GSI Sort Key
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("categoryIndex") // GSI Name
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName("category") // GSI Partition Key
                                                .keyType(KeyType.HASH)
                                                .build(),
                                        KeySchemaElement.builder()
                                                .attributeName("createdAt") // GSI Sort Key
                                                .keyType(KeyType.RANGE)
                                                .build()
                                )
                                .projection(Projection.builder()
                                        .projectionType(ProjectionType.ALL) // Store all attributes
                                        .build())
                                .provisionedThroughput(ProvisionedThroughput.builder() // Required if using Provisioned mode
                                        .readCapacityUnits(5L) // Adjust as needed
                                        .writeCapacityUnits(5L)
                                        .build())
                                .build()
                )
                .billingMode(BillingMode.PAY_PER_REQUEST) // Set billing mode at table level
                .build();

        dynamoDbClient.createTable(tableRequest);

        System.out.println("Table " + tableName + " is created");

    }
}
