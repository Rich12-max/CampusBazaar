package com.campusbazaar.model;

/**
 * Standardized marketplace categorization for campus items.
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String iconKey;
    private String description;

    public Category(int categoryId, String categoryName, String iconKey, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconKey = iconKey;
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}

