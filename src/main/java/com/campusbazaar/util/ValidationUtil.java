package com.campusbazaar.util;

import com.campusbazaar.exception.ValidationException;

public class ValidationUtil {

    public static void requireNonEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        requireNonEmpty(email, "Email address");
        String trimmed = email.trim();
        if (!trimmed.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new ValidationException("Invalid email format. Please provide a valid student/campus email.");
        }
    }

    public static void validatePositive(double value, String fieldName) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero.");
        }
    }

    public static void validateRentalDates(int days, int minDays, int maxDays) throws ValidationException {
        if (days < minDays) {
            throw new ValidationException("Rental duration (" + days + " days) is less than minimum allowed (" + minDays + " days).");
        }
        if (days > maxDays) {
            throw new ValidationException("Rental duration (" + days + " days) exceeds maximum allowed (" + maxDays + " days).");
        }
    }
}

