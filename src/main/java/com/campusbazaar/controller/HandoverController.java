package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.model.HandoverSchedule;
import com.campusbazaar.model.enums.HandoverStatus;
import com.campusbazaar.util.JsonUtil;

import java.util.*;

/**
 * REST Controller for Safe Landmark Handovers and OTP Verification.
 * Enforces verified physical exchange security in Java.
 */
public class HandoverController {

    private final DataStore dataStore = DataStore.getInstance();

    public String handleGetHandovers() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (HandoverSchedule h : dataStore.getHandoverSchedules()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", h.getScheduleId());
            map.put("itemTitle", h.getItemTitle());
            map.put("buyerName", h.getBuyerName());
            map.put("sellerName", h.getSellerName());
            map.put("location", h.getCampusSpot());
            map.put("status", h.getStatus().name());
            map.put("otpCode", h.getVerificationPin());
            result.add(map);
        }
        return ApiResponse.ok("Scheduled handovers retrieved", result).toJson();
    }

    public String handleVerifyOtp(String jsonBody) {
        int id = JsonUtil.getInt(jsonBody, "scheduleId", 1);
        String enteredOtp = JsonUtil.getString(jsonBody, "otp", "").trim();

        for (HandoverSchedule h : dataStore.getHandoverSchedules()) {
            if (h.getScheduleId() == id) {
                if (h.verifyAndComplete(enteredOtp)) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("scheduleId", h.getScheduleId());
                    map.put("status", h.getStatus().name());
                    map.put("message", "Handover verified successfully! Transaction is marked COMPLETE.");
                    return ApiResponse.ok("Handover verified", map).toJson();
                } else {
                    return ApiResponse.error("Incorrect verification PIN. Handover cannot be completed.").toJson();
                }
            }
        }
        return ApiResponse.error("Handover schedule record not found").toJson();
    }
}
