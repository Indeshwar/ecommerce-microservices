package com.demo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@DynamoDbBean
public class Inventory {
    private String inventoryId;
    private String inventoryName;
    private String category;
    private Long quantity;
    private Double price;
    private Date createdAt;

    @DynamoDbPartitionKey       //Primary Key
    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }


    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "CategoryIndex") //GSI Partition Key
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @DynamoDbSecondarySortKey(indexNames = "CategoryIndex") //GSI Sort Key
    @DynamoDbAttribute("createdAt")
    public String getCreatedAt() {
        return formatDate(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Convert Date to ISO 8601 format
    private String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}
