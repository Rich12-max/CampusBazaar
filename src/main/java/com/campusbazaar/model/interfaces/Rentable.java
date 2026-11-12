package com.campusbazaar.model.interfaces;

public interface Rentable {
    double calculateRentalCost(int days);
    double calculateLatePenalty(int daysOverdue);
    boolean isCurrentlyAvailable();
}

