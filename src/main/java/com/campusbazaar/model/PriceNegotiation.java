package com.campusbazaar.model;

import com.campusbazaar.model.enums.NegotiationStatus;

import java.time.LocalDateTime;

/**
 * Encapsulates a multi-round (max 3 rounds) bargaining negotiation between buyer and seller.
 */
public class PriceNegotiation {
    public static final int MAX_ROUNDS = 3;

    private int negotiationId;
    private int listingId;
    private String itemTitle;
    private double listedPrice;
    private int buyerId;
    private String buyerName;
    private int sellerId;
    private String sellerName;
    private double currentOfferPrice;
    private int lastOfferedBy; // userId who made currentOfferPrice
    private int roundNumber;   // 1 to 3
    private NegotiationStatus status;
    private String buyerNote;
    private String sellerNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PriceNegotiation(int negotiationId, int listingId, String itemTitle, double listedPrice,
                            int buyerId, String buyerName, int sellerId, String sellerName,
                            double initialOffer, String buyerNote) {
        this.negotiationId = negotiationId;
        this.listingId = listingId;
        this.itemTitle = itemTitle;
        this.listedPrice = listedPrice;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.currentOfferPrice = initialOffer;
        this.lastOfferedBy = buyerId;
        this.roundNumber = 1;
        this.status = NegotiationStatus.PENDING_SELLER;
        this.buyerNote = buyerNote;
        this.sellerNote = "";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canCounter() {
        return roundNumber < MAX_ROUNDS &&
                (status == NegotiationStatus.PENDING_SELLER || status == NegotiationStatus.COUNTERED_BY_SELLER);
    }

    public void sellerCounter(double counterPrice, String note) {
        if (!canCounter()) {
            throw new IllegalStateException("Maximum negotiation rounds reached or negotiation closed.");
        }
        this.roundNumber++;
        this.currentOfferPrice = counterPrice;
        this.lastOfferedBy = this.sellerId;
        this.sellerNote = note;
        this.status = NegotiationStatus.COUNTERED_BY_SELLER;
        this.updatedAt = LocalDateTime.now();
    }

    public void buyerCounter(double counterPrice, String note) {
        if (!canCounter()) {
            throw new IllegalStateException("Maximum negotiation rounds reached or negotiation closed.");
        }
        this.roundNumber++;
        this.currentOfferPrice = counterPrice;
        this.lastOfferedBy = this.buyerId;
        this.buyerNote = note;
        this.status = NegotiationStatus.PENDING_SELLER;
        this.updatedAt = LocalDateTime.now();
    }

    public void accept() {
        this.status = NegotiationStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void decline(String note) {
        this.status = NegotiationStatus.DECLINED;
        if (sellerNote == null || sellerNote.isEmpty()) this.sellerNote = note;
        this.updatedAt = LocalDateTime.now();
    }

    public int getNegotiationId() { return negotiationId; }
    public void setNegotiationId(int negotiationId) { this.negotiationId = negotiationId; }

    public int getListingId() { return listingId; }
    public String getItemTitle() { return itemTitle; }
    public double getListedPrice() { return listedPrice; }

    public int getBuyerId() { return buyerId; }
    public String getBuyerName() { return buyerName; }

    public int getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }

    public double getCurrentOfferPrice() { return currentOfferPrice; }
    public int getLastOfferedBy() { return lastOfferedBy; }
    public int getRoundNumber() { return roundNumber; }
    public NegotiationStatus getStatus() { return status; }

    public String getBuyerNote() { return buyerNote; }
    public void setBuyerNote(String buyerNote) { this.buyerNote = buyerNote; }

    public String getSellerNote() { return sellerNote; }
    public void setSellerNote(String sellerNote) { this.sellerNote = sellerNote; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

