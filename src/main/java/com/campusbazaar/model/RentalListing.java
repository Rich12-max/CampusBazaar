package com.campusbazaar.model;

import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.model.interfaces.Rentable;
import com.campusbazaar.model.interfaces.Searchable;

/**
 * Demonstrates Inheritance, Polymorphism, Multiple Interfaces (Searchable, Rentable).
 * Represents an item available for short-term rental by fellow campus students.
 */
public class RentalListing extends Listing implements Searchable, Rentable {
    private double dailyRentalPrice;
    private double securityDeposit;
    private int minRentalDays;
    private int maxRentalDays;
    private double lateFeePerDay;

    public RentalListing(int listingId, Product product, Student seller, ListingStatus status,
                         double dailyRentalPrice, double securityDeposit, int minRentalDays,
                         int maxRentalDays, double lateFeePerDay) {
        super(listingId, product, seller, ListingType.RENTAL, status);
        this.dailyRentalPrice = dailyRentalPrice;
        this.securityDeposit = securityDeposit;
        this.minRentalDays = minRentalDays;
        this.maxRentalDays = maxRentalDays;
        this.lateFeePerDay = lateFeePerDay;
    }

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public int getMinRentalDays() {
        return minRentalDays;
    }

    public void setMinRentalDays(int minRentalDays) {
        this.minRentalDays = minRentalDays;
    }

    public int getMaxRentalDays() {
        return maxRentalDays;
    }

    public void setMaxRentalDays(int maxRentalDays) {
        this.maxRentalDays = maxRentalDays;
    }

    public double getLateFeePerDay() {
        return lateFeePerDay;
    }

    public void setLateFeePerDay(double lateFeePerDay) {
        this.lateFeePerDay = lateFeePerDay;
    }

    @Override
    public double getEffectivePrice() {
        return dailyRentalPrice;
    }

    @Override
    public String getPricingDisplay() {
        return "₹" + String.format("%.0f", dailyRentalPrice) + "/day (Deposit: ₹" + String.format("%.0f", securityDeposit) + ")";
    }

    @Override
    public double calculateRentalCost(int days) {
        if (days <= 0) return 0.0;
        return days * dailyRentalPrice;
    }

    @Override
    public double calculateLatePenalty(int daysOverdue) {
        if (daysOverdue <= 0) return 0.0;
        return daysOverdue * lateFeePerDay;
    }

    @Override
    public boolean isCurrentlyAvailable() {
        return status == ListingStatus.AVAILABLE;
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
        return dailyRentalPrice >= minPrice && dailyRentalPrice <= maxPrice;
    }
}

