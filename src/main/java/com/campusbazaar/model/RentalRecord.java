package com.campusbazaar.model;

import com.campusbazaar.model.enums.RentalStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Encapsulates an active or completed rental agreement on campus.
 */
public class RentalRecord {
    private int rentalId;
    private int listingId;
    private String itemTitle;
    private int borrowerId;
    private String borrowerName;
    private int lenderId;
    private String lenderName;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualReturnDate;
    private double dailyRate;
    private double securityDeposit;
    private double lateFeeCalculated;
    private double finalAmountPaid;
    private double depositRefunded;
    private RentalStatus status;
    private LocalDateTime createdAt;

    public RentalRecord(int rentalId, int listingId, String itemTitle, int borrowerId, String borrowerName,
                        int lenderId, String lenderName, LocalDate startDate, LocalDate expectedEndDate,
                        double dailyRate, double securityDeposit, RentalStatus status) {
        this.rentalId = rentalId;
        this.listingId = listingId;
        this.itemTitle = itemTitle;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.lenderId = lenderId;
        this.lenderName = lenderName;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.dailyRate = dailyRate;
        this.securityDeposit = securityDeposit;
        this.lateFeeCalculated = 0.0;
        this.finalAmountPaid = 0.0;
        this.depositRefunded = securityDeposit;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public double getLateFeeCalculated() {
        return lateFeeCalculated;
    }

    public void setLateFeeCalculated(double lateFeeCalculated) {
        this.lateFeeCalculated = lateFeeCalculated;
    }

    public double getFinalAmountPaid() {
        return finalAmountPaid;
    }

    public void setFinalAmountPaid(double finalAmountPaid) {
        this.finalAmountPaid = finalAmountPaid;
    }

    public double getDepositRefunded() {
        return depositRefunded;
    }

    public void setDepositRefunded(double depositRefunded) {
        this.depositRefunded = depositRefunded;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

