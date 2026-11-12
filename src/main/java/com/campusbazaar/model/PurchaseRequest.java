package com.campusbazaar.model;

import com.campusbazaar.model.enums.RequestStatus;
import java.time.LocalDateTime;

/**
 * Tracks a formal purchase intent from a student buyer to a seller.
 */
public class PurchaseRequest {
    private int requestId;
    private int listingId;
    private int buyerId;
    private String buyerName;
    private double offeredPrice;
    private String proposedSpot;
    private String buyerNotes;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public PurchaseRequest(int requestId, int listingId, int buyerId, String buyerName,
                           double offeredPrice, String proposedSpot, String buyerNotes,
                           RequestStatus status) {
        this.requestId = requestId;
        this.listingId = listingId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.offeredPrice = offeredPrice;
        this.proposedSpot = proposedSpot;
        this.buyerNotes = buyerNotes;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public String getProposedSpot() {
        return proposedSpot;
    }

    public void setProposedSpot(String proposedSpot) {
        this.proposedSpot = proposedSpot;
    }

    public String getBuyerNotes() {
        return buyerNotes;
    }

    public void setBuyerNotes(String buyerNotes) {
        this.buyerNotes = buyerNotes;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

