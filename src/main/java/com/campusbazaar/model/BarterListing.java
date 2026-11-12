package com.campusbazaar.model;

import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.model.interfaces.Searchable;

/**
 * Demonstrates Polymorphism and Inheritance.
 * Represents a peer-to-peer barter listing where students exchange academic resources
 * (e.g., Book-for-Book or Calculator-for-Drafter) with optional cash difference adjustments.
 */
public class BarterListing extends Listing implements Searchable {
    private String preferredExchangeItem;
    private double cashAdjustment; // Positive = requester pays extra, Negative = poster offers extra, 0 = even swap
    private boolean isCashAdjustmentNegotiable;

    public BarterListing(int listingId, Product product, Student seller, ListingStatus status,
                         String preferredExchangeItem, double cashAdjustment, boolean isCashAdjustmentNegotiable) {
        super(listingId, product, seller, ListingType.BARTER, status);
        this.preferredExchangeItem = preferredExchangeItem;
        this.cashAdjustment = cashAdjustment;
        this.isCashAdjustmentNegotiable = isCashAdjustmentNegotiable;
    }

    public String getPreferredExchangeItem() {
        return preferredExchangeItem;
    }

    public void setPreferredExchangeItem(String preferredExchangeItem) {
        this.preferredExchangeItem = preferredExchangeItem;
    }

    public double getCashAdjustment() {
        return cashAdjustment;
    }

    public void setCashAdjustment(double cashAdjustment) {
        this.cashAdjustment = cashAdjustment;
    }

    public boolean isCashAdjustmentNegotiable() {
        return isCashAdjustmentNegotiable;
    }

    public void setCashAdjustmentNegotiable(boolean cashAdjustmentNegotiable) {
        isCashAdjustmentNegotiable = cashAdjustmentNegotiable;
    }

    @Override
    public double getEffectivePrice() {
        return Math.max(0.0, cashAdjustment);
    }

    @Override
    public String getPricingDisplay() {
        if (cashAdjustment == 0.0) {
            return "SWAP (Even Exchange: " + preferredExchangeItem + ")";
        } else if (cashAdjustment > 0.0) {
            return "SWAP + ₹" + (int) cashAdjustment + " (" + preferredExchangeItem + ")";
        } else {
            return "SWAP - ₹" + (int) Math.abs(cashAdjustment) + " (" + preferredExchangeItem + ")";
        }
    }

    @Override
    public boolean matchesSearch(String query) {
        if (query == null || query.trim().isEmpty()) return true;
        String q = query.toLowerCase().trim();
        return (product != null && (
                (product.getTitle() != null && product.getTitle().toLowerCase().contains(q)) ||
                (product.getDescription() != null && product.getDescription().toLowerCase().contains(q)) ||
                (preferredExchangeItem != null && preferredExchangeItem.toLowerCase().contains(q))
        ));
    }

    @Override
    public boolean matchesCategory(int categoryId) {
        return categoryId <= 0 || (product != null && product.getCategoryId() == categoryId);
    }

    @Override
    public boolean matchesPriceRange(double minPrice, double maxPrice) {
        return Math.abs(cashAdjustment) >= minPrice && Math.abs(cashAdjustment) <= maxPrice;
    }
}
