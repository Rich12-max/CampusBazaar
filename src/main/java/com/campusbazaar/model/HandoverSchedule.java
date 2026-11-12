package com.campusbazaar.model;

import com.campusbazaar.model.enums.HandoverStatus;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Encapsulates an in-person safe campus exchange appointment with 6-digit OTP verification.
 * Guarantees physical safety at designated campus meeting points.
 */
public class HandoverSchedule {
    private int scheduleId;
    private int listingId;
    private String itemTitle;
    private int buyerId;
    private String buyerName;
    private int sellerId;
    private String sellerName;
    private String campusSpot;
    private String scheduledDateTime;
    private String verificationPin; // 6-digit OTP given to buyer; entered by seller upon physical handover
    private HandoverStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public HandoverSchedule(int scheduleId, int listingId, String itemTitle, int buyerId, String buyerName,
                            int sellerId, String sellerName, String campusSpot, String scheduledDateTime,
                            String notes) {
        this.scheduleId = scheduleId;
        this.listingId = listingId;
        this.itemTitle = itemTitle;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.campusSpot = campusSpot;
        this.scheduledDateTime = scheduledDateTime;
        this.notes = notes;
        this.status = HandoverStatus.SCHEDULED;
        this.verificationPin = generateSecurePin();
        this.createdAt = LocalDateTime.now();
        this.completedAt = null;
    }

    private static String generateSecurePin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

    public boolean verifyAndComplete(String enteredPin) {
        if (enteredPin != null && enteredPin.trim().equals(this.verificationPin)) {
            this.status = HandoverStatus.VERIFIED_COMPLETED;
            this.completedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getListingId() { return listingId; }
    public void setListingId(int listingId) { this.listingId = listingId; }

    public String getItemTitle() { return itemTitle; }
    public void setItemTitle(String itemTitle) { this.itemTitle = itemTitle; }

    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getCampusSpot() { return campusSpot; }
    public void setCampusSpot(String campusSpot) { this.campusSpot = campusSpot; }

    public String getScheduledDateTime() { return scheduledDateTime; }
    public void setScheduledDateTime(String scheduledDateTime) { this.scheduledDateTime = scheduledDateTime; }

    public String getVerificationPin() { return verificationPin; }
    public void setVerificationPin(String verificationPin) { this.verificationPin = verificationPin; }

    public HandoverStatus getStatus() { return status; }
    public void setStatus(HandoverStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}

