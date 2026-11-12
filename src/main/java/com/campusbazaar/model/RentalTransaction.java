package com.campusbazaar.model;

import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.TransactionStatus;

public class RentalTransaction extends Transaction {
    private int daysRented;
    private int daysOverdue;
    private double latePenaltyAmount;
    private double depositRefunded;

    public RentalTransaction(int transactionId, String transactionCode, int listingId, String itemTitle,
                             int buyerId, String buyerName, int sellerId, String sellerName,
                             double totalAmount, PaymentMethod paymentMethod, TransactionStatus status,
                             int daysRented, int daysOverdue, double latePenaltyAmount, double depositRefunded) {
        super(transactionId, transactionCode, listingId, itemTitle, buyerId, buyerName, sellerId, sellerName,
                totalAmount, paymentMethod, status);
        this.daysRented = daysRented;
        this.daysOverdue = daysOverdue;
        this.latePenaltyAmount = latePenaltyAmount;
        this.depositRefunded = depositRefunded;
    }

    public int getDaysRented() {
        return daysRented;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public double getLatePenaltyAmount() {
        return latePenaltyAmount;
    }

    public double getDepositRefunded() {
        return depositRefunded;
    }

    @Override
    public String getTransactionType() {
        return "PRODUCT_RENTAL";
    }

    @Override
    public String generateReceiptSummary() {
        return "RENTAL SETTLEMENT RECEIPT #" + transactionCode + "\n" +
                "Item: " + itemTitle + "\n" +
                "Borrower: " + buyerName + " | Lender: " + sellerName + "\n" +
                "Duration: " + daysRented + " days | Overdue: " + daysOverdue + " days\n" +
                "Rental Charge: ₹" + String.format("%.2f", totalAmount - latePenaltyAmount) + "\n" +
                "Late Penalty: ₹" + String.format("%.2f", latePenaltyAmount) + "\n" +
                "Refunded Deposit: ₹" + String.format("%.2f", depositRefunded);
    }
}

