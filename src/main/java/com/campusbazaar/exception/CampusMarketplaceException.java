package com.campusbazaar.exception;

public class CampusMarketplaceException extends Exception {
    public CampusMarketplaceException(String message) {
        super(message);
    }

    public CampusMarketplaceException(String message, Throwable cause) {
        super(message, cause);
    }
}

