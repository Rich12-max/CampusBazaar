package com.campusbazaar.exception;

public class ListingNotFoundException extends CampusMarketplaceException {
    public ListingNotFoundException(int listingId) {
        super("Listing with ID #" + listingId + " was not found or is no longer available.");
    }
}

