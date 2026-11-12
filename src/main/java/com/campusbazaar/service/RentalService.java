package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.InvalidRentalDurationException;
import com.campusbazaar.model.RentalListing;
import com.campusbazaar.model.RentalRecord;
import com.campusbazaar.model.RentalTransaction;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.RentalStatus;
import com.campusbazaar.model.enums.TransactionStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class RentalService {
    private final DataStore dataStore = DataStore.getInstance();

    public RentalRecord requestRental(RentalListing listing, Student borrower, LocalDate startDate, LocalDate expectedEndDate)
            throws InvalidRentalDurationException {
        if (startDate.isAfter(expectedEndDate)) {
            throw new InvalidRentalDurationException("Start date cannot be after expected return date.");
        }
        int days = (int) ChronoUnit.DAYS.between(startDate, expectedEndDate);
        if (days == 0) days = 1;

        if (days < listing.getMinRentalDays()) {
            throw new InvalidRentalDurationException("Rental duration (" + days + " days) is less than the minimum required (" + listing.getMinRentalDays() + " days).");
        }
        if (days > listing.getMaxRentalDays()) {
            throw new InvalidRentalDurationException("Rental duration (" + days + " days) exceeds the maximum allowed (" + listing.getMaxRentalDays() + " days).");
        }

        int rentalId = dataStore.nextRentalId();
        RentalRecord record = new RentalRecord(rentalId, listing.getListingId(), listing.getProduct().getTitle(),
                borrower.getUserId(), borrower.getFullName(),
                listing.getSeller().getUserId(), listing.getSeller().getFullName(),
                startDate, expectedEndDate, listing.getDailyRentalPrice(), listing.getSecurityDeposit(),
                RentalStatus.REQUESTED);

        dataStore.addRentalRecord(record);
        return record;
    }

    public void approveRental(int rentalId) {
        RentalRecord record = dataStore.getRentalRecordById(rentalId);
        if (record != null) {
            record.setStatus(RentalStatus.ACTIVE);
            RentalListing listing = (RentalListing) dataStore.getListingById(record.getListingId());
            if (listing != null) {
                listing.setStatus(ListingStatus.RENTED);
            }
        }
    }

    public RentalTransaction processReturn(int rentalId, LocalDate actualReturnDate) {
        RentalRecord record = dataStore.getRentalRecordById(rentalId);
        if (record == null) return null;

        record.setActualReturnDate(actualReturnDate);

        // Calculate days used
        int totalDays = (int) ChronoUnit.DAYS.between(record.getStartDate(), actualReturnDate);
        if (totalDays <= 0) totalDays = 1;

        int agreedDays = (int) ChronoUnit.DAYS.between(record.getStartDate(), record.getExpectedEndDate());
        if (agreedDays <= 0) agreedDays = 1;

        int overdueDays = Math.max(0, totalDays - agreedDays);

        RentalListing listing = (RentalListing) dataStore.getListingById(record.getListingId());
        double dailyLateFee = listing != null ? listing.getLateFeePerDay() : 25.0;

        double baseRent = totalDays * record.getDailyRate();
        double latePenalty = overdueDays * dailyLateFee;
        double totalOwed = baseRent + latePenalty;
        double refundedDeposit = Math.max(0.0, record.getSecurityDeposit() - latePenalty);

        record.setLateFeeCalculated(latePenalty);
        record.setFinalAmountPaid(totalOwed);
        record.setDepositRefunded(refundedDeposit);
        record.setStatus(RentalStatus.RETURNED);

        if (listing != null) {
            listing.setStatus(ListingStatus.AVAILABLE);
        }

        // Record transaction
        String txnCode = "RENT-TXN-" + System.currentTimeMillis() % 100000;
        RentalTransaction txn = new RentalTransaction(dataStore.nextTransactionId(), txnCode,
                record.getListingId(), record.getItemTitle(),
                record.getBorrowerId(), record.getBorrowerName(),
                record.getLenderId(), record.getLenderName(),
                totalOwed, PaymentMethod.CASH_ON_HANDOVER, TransactionStatus.COMPLETED,
                totalDays, overdueDays, latePenalty, refundedDeposit);
        dataStore.addTransaction(txn);

        return txn;
    }

    public List<RentalRecord> getRentalsForUser(int userId) {
        return dataStore.getRentalRecords().stream()
                .filter(r -> r.getBorrowerId() == userId || r.getLenderId() == userId)
                .collect(Collectors.toList());
    }
}

