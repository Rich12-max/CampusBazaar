package com.campusbazaar.model;

import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.TransactionStatus;

public class PurchaseTransaction extends Transaction {
    private String campusMeetingSpot;

    public PurchaseTransaction(int transactionId, String transactionCode, int listingId, String itemTitle,
                               int buyerId, String buyerName, int sellerId, String sellerName,
                               double totalAmount, PaymentMethod paymentMethod, TransactionStatus status,
                               String campusMeetingSpot) {
        super(transactionId, transactionCode, listingId, itemTitle, buyerId, buyerName, sellerId, sellerName,
                totalAmount, paymentMethod, status);
        this.campusMeetingSpot = campusMeetingSpot;
    }

    public String getCampusMeetingSpot() {
        return campusMeetingSpot;
    }

    public void setCampusMeetingSpot(String campusMeetingSpot) {
        this.campusMeetingSpot = campusMeetingSpot;
    }

    @Override
    public String getTransactionType() {
        return "OUTRIGHT_PURCHASE";
    }

    @Override
    public String generateReceiptSummary() {
        return "PURCHASE RECEIPT #" + transactionCode + "\n" +
                "Item: " + itemTitle + "\n" +
                "Buyer: " + buyerName + " | Seller: " + sellerName + "\n" +
                "Handover Spot: " + campusMeetingSpot + "\n" +
                "Settled Amount: ₹" + String.format("%.2f", totalAmount) + " via " + paymentMethod;
    }
}

