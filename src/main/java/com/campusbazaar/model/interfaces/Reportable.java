package com.campusbazaar.model.interfaces;

public interface Reportable {
    void incrementReportCount();
    int getReportCount();
    boolean isFlaggedForReview();
}

