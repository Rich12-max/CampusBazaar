package com.campusbazaar.model;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private int transactionId;
    private int reviewerId;
    private String reviewerName;
    private int revieweeId;
    private String revieweeName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public Review(int reviewId, int transactionId, int reviewerId, String reviewerName,
                  int revieweeId, String revieweeName, int rating, String comment) {
        this.reviewId = reviewId;
        this.transactionId = transactionId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.revieweeId = revieweeId;
        this.revieweeName = revieweeName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public int getRevieweeId() {
        return revieweeId;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

