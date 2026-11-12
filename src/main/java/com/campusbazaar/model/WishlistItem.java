package com.campusbazaar.model;

import java.time.LocalDateTime;

public class WishlistItem {
    private int wishlistId;
    private int studentId;
    private int listingId;
    private LocalDateTime addedAt;

    public WishlistItem(int wishlistId, int studentId, int listingId) {
        this.wishlistId = wishlistId;
        this.studentId = studentId;
        this.listingId = listingId;
        this.addedAt = LocalDateTime.now();
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getListingId() {
        return listingId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}

