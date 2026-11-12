package com.campusbazaar.model;

import com.campusbazaar.util.PasswordHasher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates an official university campus clearance receipt.
 * Provides a cryptographic digital integrity hash and printable clearance certificate.
 */
public class ClearanceReceipt {
    private String receiptNumber;
    private String transactionCode;
    private String transactionType; // PURCHASE_CLEARANCE, RENTAL_RETURN_CLEARANCE, BARTER_SWAP_CLEARANCE
    private LocalDateTime issuedAt;
    private String itemTitle;
    private String categoryName;
    private String sellerName;
    private String sellerRollNo;
    private String sellerDepartment;
    private String buyerName;
    private String buyerRollNo;
    private String buyerDepartment;
    private double baseAmount;
    private double depositRefunded;
    private double lateFeeDeducted;
    private double finalNetPaid;
    private String handoverSpot;
    private String verificationHash;
    private String clearanceAuthority;

    public ClearanceReceipt(String receiptNumber, String transactionCode, String transactionType,
                            String itemTitle, String categoryName,
                            String sellerName, String sellerRollNo, String sellerDepartment,
                            String buyerName, String buyerRollNo, String buyerDepartment,
                            double baseAmount, double depositRefunded, double lateFeeDeducted, double finalNetPaid,
                            String handoverSpot) {
        this.receiptNumber = receiptNumber;
        this.transactionCode = transactionCode;
        this.transactionType = transactionType;
        this.issuedAt = LocalDateTime.now();
        this.itemTitle = itemTitle;
        this.categoryName = categoryName;
        this.sellerName = sellerName;
        this.sellerRollNo = sellerRollNo;
        this.sellerDepartment = sellerDepartment;
        this.buyerName = buyerName;
        this.buyerRollNo = buyerRollNo;
        this.buyerDepartment = buyerDepartment;
        this.baseAmount = baseAmount;
        this.depositRefunded = depositRefunded;
        this.lateFeeDeducted = lateFeeDeducted;
        this.finalNetPaid = finalNetPaid;
        this.handoverSpot = handoverSpot;
        this.clearanceAuthority = "CampusBazaar Student Affairs Escrow & Resource Verification Council";
        this.verificationHash = computeDigitalSignature();
    }

    private String computeDigitalSignature() {
        String payload = receiptNumber + ":" + transactionCode + ":" + itemTitle + ":" +
                sellerRollNo + ":" + buyerRollNo + ":" + finalNetPaid;
        return PasswordHasher.hash(payload).substring(0, 24).toUpperCase();
    }

    public String getReceiptNumber() { return receiptNumber; }
    public String getTransactionCode() { return transactionCode; }
    public String getTransactionType() { return transactionType; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public String getItemTitle() { return itemTitle; }
    public String getCategoryName() { return categoryName; }
    public String getSellerName() { return sellerName; }
    public String getSellerRollNo() { return sellerRollNo; }
    public String getSellerDepartment() { return sellerDepartment; }
    public String getBuyerName() { return buyerName; }
    public String getBuyerRollNo() { return buyerRollNo; }
    public String getBuyerDepartment() { return buyerDepartment; }
    public double getBaseAmount() { return baseAmount; }
    public double getDepositRefunded() { return depositRefunded; }
    public double getLateFeeDeducted() { return lateFeeDeducted; }
    public double getFinalNetPaid() { return finalNetPaid; }
    public String getHandoverSpot() { return handoverSpot; }
    public String getVerificationHash() { return verificationHash; }
    public String getClearanceAuthority() { return clearanceAuthority; }

    public String toFormattedTextSlip() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================\n");
        sb.append("      CAMPUS PEER-TO-PEER MARKETPLACE - CLEARANCE CERTIFICATE        \n");
        sb.append("====================================================================\n");
        sb.append("Receipt Number : ").append(receiptNumber).append("\n");
        sb.append("Transaction ID : ").append(transactionCode).append("\n");
        sb.append("Date & Time    : ").append(issuedAt.format(dtf)).append("\n");
        sb.append("Clearance Type : ").append(transactionType.replace("_", " ")).append("\n");
        sb.append("Item Title     : ").append(itemTitle).append(" (").append(categoryName).append(")\n");
        sb.append("--------------------------------------------------------------------\n");
        sb.append("SELLER / LENDER : ").append(sellerName).append(" [").append(sellerRollNo).append(" - ").append(sellerDepartment).append("]\n");
        sb.append("BUYER / BORROWER: ").append(buyerName).append(" [").append(buyerRollNo).append(" - ").append(buyerDepartment).append("]\n");
        sb.append("Handover Spot   : ").append(handoverSpot).append("\n");
        sb.append("--------------------------------------------------------------------\n");
        sb.append("Base Amount Paid         : ₹").append(String.format("%.2f", baseAmount)).append("\n");
        if (depositRefunded > 0) {
            sb.append("Security Deposit Refund  : ₹").append(String.format("%.2f", depositRefunded)).append("\n");
        }
        if (lateFeeDeducted > 0) {
            sb.append("Late Penalty Deducted    : ₹").append(String.format("%.2f", lateFeeDeducted)).append("\n");
        }
        sb.append("FINAL SETTLED AMOUNT     : ₹").append(String.format("%.2f", finalNetPaid)).append("\n");
        sb.append("--------------------------------------------------------------------\n");
        sb.append("Digital Verification Hash: ").append(verificationHash).append("\n");
        sb.append("Issuing Authority        : ").append(clearanceAuthority).append("\n");
        sb.append("STATUS                   : VERIFIED & CLEARED FOR CAMPUS RECORDS\n");
        sb.append("====================================================================\n");
        return sb.toString();
    }
}

