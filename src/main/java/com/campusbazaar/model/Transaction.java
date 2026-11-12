package com.campusbazaar.model;

import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.TransactionStatus;
import java.time.LocalDateTime;

/**
 * Demonstrates Abstraction & Inheritance.
 * Base class for all settled campus transactions.
 */
public abstract class Transaction {
    protected int transactionId;
    protected String transactionCode;
    protected int listingId;
    protected String itemTitle;
    protected int buyerId;
    protected String buyerName;
    protected int sellerId;
    protected String sellerName;
    protected double totalAmount;
    protected PaymentMethod paymentMethod;
    protected TransactionStatus status;
    protected LocalDateTime transactionTime;

    public Transaction(int transactionId, String transactionCode, int listingId, String itemTitle,
                       int buyerId, String buyerName, int sellerId, String sellerName,
                       double totalAmount, PaymentMethod paymentMethod, TransactionStatus status) {
        this.transactionId = transactionId;
        this.transactionCode = transactionCode;
        this.listingId = listingId;
        this.itemTitle = itemTitle;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionTime = LocalDateTime.now();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public int getListingId() {
        return listingId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public abstract String getTransactionType();
    public abstract String generateReceiptSummary();
}

