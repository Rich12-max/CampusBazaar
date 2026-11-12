package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.dto.ListingResponseDTO;
import com.campusbazaar.dto.RentalCalculationDTO;
import com.campusbazaar.model.RentalListing;
import com.campusbazaar.util.JsonUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for Campus Micro-Rentals & Deposit Escrow.
 * Executes rental duration calculations and rental agreements in Java.
 */
public class RentalController {

    private final DataStore dataStore = DataStore.getInstance();

    public String handleCalculate(String jsonBody) {
        int days = JsonUtil.getInt(jsonBody, "days", 7);
        if (days < 1) days = 1;
        if (days > 30) days = 30;

        double dailyRate = JsonUtil.getDouble(jsonBody, "dailyRate", 25.0);
        double deposit = JsonUtil.getDouble(jsonBody, "deposit", 100.0);
        double lateFee = JsonUtil.getDouble(jsonBody, "lateFeePerDay", 15.0);

        double baseRent = dailyRate * days;
        double grandTotal = baseRent + deposit;

        RentalCalculationDTO dto = new RentalCalculationDTO(days, dailyRate, baseRent, deposit, grandTotal, lateFee);
        return ApiResponse.ok("Rental calculation complete", dto.toMap()).toJson();
    }

    public String handleGetRentals() {
        List<Map<String, Object>> list = dataStore.getListings().stream()
            .filter(l -> l instanceof RentalListing)
            .map(ListingResponseDTO::fromListing)
            .map(ListingResponseDTO::toMap)
            .collect(Collectors.toList());

        return ApiResponse.ok("Found " + list.size() + " active rentals", list).toJson();
    }
}
