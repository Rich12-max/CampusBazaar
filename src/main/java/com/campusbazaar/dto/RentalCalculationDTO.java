package com.campusbazaar.dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data Transfer Object for Campus Micro-Rental and Escrow Calculations.
 */
public class RentalCalculationDTO {
    private final int days;
    private final double dailyRate;
    private final double baseRent;
    private final double deposit;
    private final double grandTotal;
    private final double lateFeePerDay;

    public RentalCalculationDTO(int days, double dailyRate, double baseRent, double deposit, double grandTotal, double lateFeePerDay) {
        this.days = days;
        this.dailyRate = dailyRate;
        this.baseRent = baseRent;
        this.deposit = deposit;
        this.grandTotal = grandTotal;
        this.lateFeePerDay = lateFeePerDay;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("days", days);
        map.put("dailyRate", dailyRate);
        map.put("baseRent", baseRent);
        map.put("deposit", deposit);
        map.put("grandTotal", grandTotal);
        map.put("lateFeePerDay", lateFeePerDay);
        return map;
    }

    public int getDays() { return days; }
    public double getDailyRate() { return dailyRate; }
    public double getBaseRent() { return baseRent; }
    public double getDeposit() { return deposit; }
    public double getGrandTotal() { return grandTotal; }
}
