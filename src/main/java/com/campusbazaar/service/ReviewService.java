package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Review;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewService {
    private final DataStore dataStore = DataStore.getInstance();

    public Review addReview(int transactionId, Student reviewer, int revieweeId, int stars, String comment) {
        User reviewee = dataStore.getUserById(revieweeId);
        String revieweeName = reviewee != null ? reviewee.getFullName() : "Student";

        int revId = dataStore.nextReviewId();
        Review rev = new Review(revId, transactionId, reviewer.getUserId(), reviewer.getFullName(),
                revieweeId, revieweeName, stars, comment);
        dataStore.addReview(rev);

        if (reviewee instanceof Student) {
            ((Student) reviewee).addRatingScore(stars);
        }

        return rev;
    }

    public List<Review> getReviewsForUser(int userId) {
        return dataStore.getReviews().stream()
                .filter(r -> r.getRevieweeId() == userId)
                .collect(Collectors.toList());
    }
}

