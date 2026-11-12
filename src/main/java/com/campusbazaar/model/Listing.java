package com.campusbazaar.model;

import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.model.interfaces.Reportable;
import java.time.LocalDateTime;

/**
 * Demonstrates Abstraction, Polymorphism, Association and Composition.
 * Abstract class representing a generic marketplace posting.
 */
public abstract class Listing implements Reportable {
    protected int listingId;
    protected Product product;
    protected Student seller;
    protected ListingType listingType;
    protected ListingStatus status;
    protected LocalDateTime postedAt;
    protected int reportCount;

    public Listing(int listingId, Product product, Student seller, ListingType listingType, ListingStatus status) {
        this.listingId = listingId;
        this.product = product;
        this.seller = seller;
        this.listingType = listingType;
        this.status = status;
        this.postedAt = LocalDateTime.now();
        this.reportCount = 0;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Student getSeller() {
        return seller;
    }

    public void setSeller(Student seller) {
        this.seller = seller;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    @Override
    public void incrementReportCount() {
        this.reportCount++;
    }

    @Override
    public int getReportCount() {
        return reportCount;
    }

    @Override
    public boolean isFlaggedForReview() {
        return reportCount >= 2;
    }

    /**
     * Polymorphic method implemented differently by SaleListing and RentalListing.
     */
    public abstract double getEffectivePrice();

    public abstract String getPricingDisplay();
}

