package com.campusbazaar.model.interfaces;

public interface Reviewable {
    void addRatingScore(int stars);
    double getTrustRating();
    int getTotalReviewsCount();
}

