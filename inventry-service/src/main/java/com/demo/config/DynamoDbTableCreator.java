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
                .keySchema(KeySchemaElement.builder() //Build the object of KeySchema
                        .attributeName("inventoryId")  // Set the attribute name as inventoryId
                        .keyType(KeyType.HASH)            //Make  inventoryId as partition key
                        .build())
                .attributeDefinitions(AttributeDefinition.builder() //Build the object of AttributeDefinition that specify the type of attribute
                        .attributeName("inventoryId")
                        .attributeType(ScalarAttributeType.S) //Set the attribute inventoryId type as Scalar String
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        dynamoDbClient.createTable(tableRequest);

        System.out.println("Table " + tableName + " is created");

    }
}
