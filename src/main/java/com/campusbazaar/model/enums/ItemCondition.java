package com.campusbazaar.model.enums;

public enum ItemCondition {
    BRAND_NEW("Brand New", 1.00),
    LIKE_NEW("Like New", 0.85),
    GOOD("Good", 0.70),
    FAIR("Fair", 0.50);

    private final String displayName;
    private final double valueMultiplier;

    ItemCondition(String displayName, double valueMultiplier) {
        this.displayName = displayName;
        this.valueMultiplier = valueMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getValueMultiplier() {
        return valueMultiplier;
    }
}

