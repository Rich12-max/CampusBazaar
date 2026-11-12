package com.campusbazaar.model;

import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.model.interfaces.Searchable;

/**
 * Demonstrates Inheritance, Polymorphism, and Interface Implementation.
 * Represents an item listed for outright peer-to-peer sale.
 */
public class SaleListing extends Listing implements Searchable {
    private double sellingPrice;
    private double originalPrice;
    private boolean isNegotiable;

    public SaleListing(int listingId, Product product, Student seller, ListingStatus status,
                       double sellingPrice, double originalPrice, boolean isNegotiable) {
        super(listingId, product, seller, ListingType.SALE, status);
        this.sellingPrice = sellingPrice;
        this.originalPrice = originalPrice;
        this.isNegotiable = isNegotiable;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public boolean isNegotiable() {
        return isNegotiable;
    }

    public void setNegotiable(boolean negotiable) {
        isNegotiable = negotiable;
    }

    @Override
    public double getEffectivePrice() {
        return sellingPrice;
    }

    @Override
    public String getPricingDisplay() {
        return "₹" + String.format("%.0f", sellingPrice) + (isNegotiable ? " (Negotiable)" : " (Fixed)");
    }

    @Override
    public boolean matchesSearch(String query) {
        if (query == null || query.trim().isEmpty()) return true;
        String q = query.toLowerCase().trim();
        return (product != null && (
                (product.getTitle() != null && product.getTitle().toLowerCase().contains(q)) ||
                (product.getDescription() != null && product.getDescription().toLowerCase().contains(q)) ||
                (product.getBrand() != null && product.getBrand().toLowerCase().contains(q))
        ));
    }

    @Override
    public boolean matchesCategory(int categoryId) {
        return categoryId <= 0 || (product != null && product.getCategoryId() == categoryId);
    }

    @Override
    public boolean matchesPriceRange(double minPrice, double maxPrice) {
        return sellingPrice >= minPrice && sellingPrice <= maxPrice;
    }
}

