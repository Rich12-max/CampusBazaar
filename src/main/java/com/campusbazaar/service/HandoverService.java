package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.UnauthorizedOperationException;
import com.campusbazaar.exception.ValidationException;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.HandoverStatus;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.TransactionStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service orchestrating in-person campus safe meetup appointments and OTP handovers.
 */
public class HandoverService {
    private final DataStore dataStore = DataStore.getInstance();

    public static final String[] SAFE_CAMPUS_SPOTS = {
            "Central Library Ground Floor Entrance",
            "Student Activity Center (SAC) Main Desk",
            "Campus Security Gate #1 Information Kiosk",
            "Central Cafeteria Main Seating Area",
            "Tech Block Auditorium Steps",
            "Hostel Quadrangle Badminton Court"
    };

    public HandoverSchedule scheduleHandover(int listingId, Student buyer, Student seller,
                                            String campusSpot, String scheduledDateTime, String notes)
            throws ValidationException, UnauthorizedOperationException {
        if (buyer.getUserId() == seller.getUserId()) {
            throw new UnauthorizedOperationException("Buyer and seller cannot be the same student.");
        }
        if (campusSpot == null || campusSpot.trim().isEmpty()) {
            throw new ValidationException("Designated campus safe meetup spot must be selected.");
        }
        if (scheduledDateTime == null || scheduledDateTime.trim().isEmpty()) {
            throw new ValidationException("Scheduled date and time cannot be empty.");
        }

        Listing listing = dataStore.getListingById(listingId);
        String itemTitle = listing != null ? listing.getProduct().getTitle() : "Campus Item #" + listingId;

        int scheduleId = dataStore.nextScheduleId();
        HandoverSchedule schedule = new HandoverSchedule(scheduleId, listingId, itemTitle,
                buyer.getUserId(), buyer.getFullName(), seller.getUserId(), seller.getFullName(),
                campusSpot.trim(), scheduledDateTime.trim(), notes != null ? notes.trim() : "");
        dataStore.addHandoverSchedule(schedule);
        return schedule;
    }

    public boolean verifyAndCompleteHandover(int scheduleId, int sellerId, String enteredPin)
            throws UnauthorizedOperationException, ValidationException {
        HandoverSchedule schedule = dataStore.getHandoverScheduleById(scheduleId);
        if (schedule == null) {
            throw new ValidationException("Handover schedule appointment not found.");
        }
        if (schedule.getSellerId() != sellerId) {
            throw new UnauthorizedOperationException("Only the designated seller can verify buyer handover OTP.");
        }
        if (schedule.getStatus() != HandoverStatus.SCHEDULED) {
            throw new ValidationException("Handover appointment is not in SCHEDULED state.");
        }

        boolean matched = schedule.verifyAndComplete(enteredPin);
        if (!matched) {
            return false;
        }

        // Successfully verified: complete listing & generate transaction + clearance receipt
        Listing listing = dataStore.getListingById(schedule.getListingId());
        if (listing != null) {
            listing.setStatus(ListingStatus.SOLD);
        }

        String txnCode = "TXN-HANDOVER-" + System.currentTimeMillis() % 100000;
        double amount = listing != null ? listing.getEffectivePrice() : 0.0;
        PurchaseTransaction pt = new PurchaseTransaction(dataStore.nextTransactionId(), txnCode,
                schedule.getListingId(), schedule.getItemTitle(),
                schedule.getBuyerId(), schedule.getBuyerName(),
                schedule.getSellerId(), schedule.getSellerName(),
                amount, PaymentMethod.CASH_ON_HANDOVER, TransactionStatus.COMPLETED, schedule.getCampusSpot());
        dataStore.addTransaction(pt);

        // Generate official clearance receipt
        String receiptNumber = "CB-REC-2026-" + (1000 + dataStore.getClearanceReceipts().size() + 1);
        Student seller = (Student) dataStore.getUserById(schedule.getSellerId());
        Student buyer = (Student) dataStore.getUserById(schedule.getBuyerId());

        ClearanceReceipt cr = new ClearanceReceipt(receiptNumber, txnCode, "PURCHASE_CLEARANCE",
                schedule.getItemTitle(), listing != null ? listing.getProduct().getTitle() : "Campus Resource",
                seller != null ? seller.getFullName() : schedule.getSellerName(),
                seller != null ? seller.getStudentIdNumber() : "COL-STU",
                seller != null ? seller.getDepartment() : "General",
                buyer != null ? buyer.getFullName() : schedule.getBuyerName(),
                buyer != null ? buyer.getStudentIdNumber() : "COL-STU",
                buyer != null ? buyer.getDepartment() : "General",
                amount, 0.0, 0.0, amount, schedule.getCampusSpot());
        dataStore.addClearanceReceipt(cr);

        return true;
    }

    public void cancelHandover(int scheduleId, int userId, String reason) throws UnauthorizedOperationException {
        HandoverSchedule schedule = dataStore.getHandoverScheduleById(scheduleId);
        if (schedule == null) return;
        if (schedule.getBuyerId() != userId && schedule.getSellerId() != userId) {
            throw new UnauthorizedOperationException("Only the buyer or seller can cancel this handover appointment.");
        }
        schedule.setStatus(HandoverStatus.CANCELLED);
        schedule.setNotes((schedule.getNotes() != null ? schedule.getNotes() + " | " : "") + "Cancelled: " + reason);
    }

    public List<HandoverSchedule> getSchedulesForUser(int userId) {
        return dataStore.getHandoverSchedules().stream()
                .filter(s -> s.getBuyerId() == userId || s.getSellerId() == userId)
                .collect(Collectors.toList());
    }

    public HandoverSchedule getScheduleById(int scheduleId) {
        return dataStore.getHandoverScheduleById(scheduleId);
    }
}

