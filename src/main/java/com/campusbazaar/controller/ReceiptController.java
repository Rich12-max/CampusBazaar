package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.model.ClearanceReceipt;

import java.util.*;

/**
 * REST Controller for Digital Clearance Receipts.
 * Issues cryptographic vouchers with unique serial numbers for campus checkouts.
 */
public class ReceiptController {

    private final DataStore dataStore = DataStore.getInstance();

    public String handleGetReceipts() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ClearanceReceipt r : dataStore.getClearanceReceipts()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", r.getReceiptNumber());
            map.put("receiptNumber", r.getReceiptNumber());
            map.put("itemTitle", r.getItemTitle());
            map.put("buyerName", r.getBuyerName());
            map.put("buyerRoll", r.getBuyerRollNo());
            map.put("sellerName", r.getSellerName());
            map.put("sellerRoll", r.getSellerRollNo());
            map.put("finalPaid", r.getFinalNetPaid());
            map.put("handoverLocation", r.getHandoverSpot());
            map.put("verificationHash", r.getVerificationHash());
            map.put("issuingAuthority", r.getClearanceAuthority());
            result.add(map);
        }
        return ApiResponse.ok("Clearance receipts retrieved", result).toJson();
    }
}
