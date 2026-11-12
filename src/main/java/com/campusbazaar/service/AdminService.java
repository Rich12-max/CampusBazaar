package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Listing;
import com.campusbazaar.model.User;
import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.ListingStatus;

import java.util.HashMap;
import java.util.Map;

public class AdminService {
    private final DataStore dataStore = DataStore.getInstance();

    public void suspendUser(int userId) {
        User u = dataStore.getUserById(userId);
        if (u != null) {
            u.setStatus(AccountStatus.SUSPENDED);
        }
    }

    public void activateUser(int userId) {
        User u = dataStore.getUserById(userId);
        if (u != null) {
            u.setStatus(AccountStatus.ACTIVE);
        }
    }

    public void delistListing(int listingId) {
        Listing l = dataStore.getListingById(listingId);
        if (l != null) {
            l.setStatus(ListingStatus.DELISTED);
        }
    }

    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", dataStore.getAllUsers().size());
        stats.put("totalListings", dataStore.getListings().size());
        stats.put("activeRentals", dataStore.getRentalRecords().stream().filter(r -> r.getStatus() == com.campusbazaar.model.enums.RentalStatus.ACTIVE).count());
        stats.put("completedTransactions", dataStore.getTransactions().size());
        stats.put("pendingReports", dataStore.getReports().stream().filter(r -> r.getStatus() == com.campusbazaar.model.enums.ReportStatus.OPEN).count());
        return stats;
    }
}

