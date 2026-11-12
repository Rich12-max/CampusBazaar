package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service generating and formatting official academic clearance certificates and printable receipts.
 */
public class ReceiptService {
    private final DataStore dataStore = DataStore.getInstance();

    public ClearanceReceipt generatePurchaseReceipt(PurchaseTransaction txn, String categoryName, String handoverSpot) {
        String receiptNumber = "CB-REC-2026-" + (1000 + dataStore.getClearanceReceipts().size() + 1);
        Student seller = (Student) dataStore.getUserById(txn.getSellerId());
        Student buyer = (Student) dataStore.getUserById(txn.getBuyerId());

        ClearanceReceipt receipt = new ClearanceReceipt(
                receiptNumber,
                txn.getTransactionCode(),
                "PURCHASE_CLEARANCE",
                txn.getItemTitle(),
                categoryName != null ? categoryName : "Campus Academic Asset",
                seller != null ? seller.getFullName() : txn.getSellerName(),
                seller != null ? seller.getStudentIdNumber() : "COL-STU",
                seller != null ? seller.getDepartment() : "General",
                buyer != null ? buyer.getFullName() : txn.getBuyerName(),
                buyer != null ? buyer.getStudentIdNumber() : "COL-STU",
                buyer != null ? buyer.getDepartment() : "General",
                txn.getTotalAmount(),
                0.0,
                0.0,
                txn.getTotalAmount(),
                handoverSpot != null ? handoverSpot : "Designated Campus Point"
        );
        dataStore.addClearanceReceipt(receipt);
        return receipt;
    }

    public ClearanceReceipt generateRentalReturnReceipt(RentalRecord record, String itemTitle, String categoryName, String handoverSpot) {
        String receiptNumber = "CB-REC-2026-" + (1000 + dataStore.getClearanceReceipts().size() + 1);
        Student lender = (Student) dataStore.getUserById(record.getLenderId());
        Student borrower = (Student) dataStore.getUserById(record.getBorrowerId());

        ClearanceReceipt receipt = new ClearanceReceipt(
                receiptNumber,
                "RENTAL-RET-" + record.getRentalId(),
                "RENTAL_RETURN_CLEARANCE",
                itemTitle != null ? itemTitle : record.getItemTitle(),
                categoryName != null ? categoryName : "Academic Rental Asset",
                lender != null ? lender.getFullName() : record.getLenderName(),
                lender != null ? lender.getStudentIdNumber() : "COL-LEND",
                lender != null ? lender.getDepartment() : "General",
                borrower != null ? borrower.getFullName() : record.getBorrowerName(),
                borrower != null ? borrower.getStudentIdNumber() : "COL-BORR",
                borrower != null ? borrower.getDepartment() : "General",
                record.getFinalAmountPaid(),
                record.getDepositRefunded(),
                record.getLateFeeCalculated(),
                record.getFinalAmountPaid(),
                handoverSpot != null ? handoverSpot : "Designated Campus Point"
        );
        dataStore.addClearanceReceipt(receipt);
        return receipt;
    }

    public ClearanceReceipt getReceiptByNumber(String receiptNumber) {
        return dataStore.getClearanceReceiptByNumber(receiptNumber);
    }

    public List<ClearanceReceipt> getAllReceipts() {
        return dataStore.getClearanceReceipts();
    }

    public List<ClearanceReceipt> getReceiptsForUser(String studentRollOrName) {
        if (studentRollOrName == null || studentRollOrName.trim().isEmpty()) {
            return getAllReceipts();
        }
        String q = studentRollOrName.toLowerCase().trim();
        return dataStore.getClearanceReceipts().stream()
                .filter(r -> r.getBuyerName().toLowerCase().contains(q) ||
                        r.getBuyerRollNo().toLowerCase().contains(q) ||
                        r.getSellerName().toLowerCase().contains(q) ||
                        r.getSellerRollNo().toLowerCase().contains(q) ||
                        r.getReceiptNumber().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}
