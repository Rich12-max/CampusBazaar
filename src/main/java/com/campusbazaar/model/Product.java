package com.campusbazaar.model;

import com.campusbazaar.model.enums.ItemCondition;
import java.time.LocalDateTime;

/**
 * Demonstrates Composition.
 * Encapsulates the physical properties of a campus item.
 */
public class Product {
    private int productId;
    private int sellerId;
    private int categoryId;
    private String title;
    private String description;
    private ItemCondition condition;
    private String brand;
    private double originalPrice;
    private String imagePath;
    private LocalDateTime createdAt;

    public Product(int productId, int sellerId, int categoryId, String title, String description,
                   ItemCondition condition, String brand, double originalPrice, String imagePath) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.brand = brand;
        this.originalPrice = originalPrice;
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return title + " [" + condition.getDisplayName() + "]";
    }
}

