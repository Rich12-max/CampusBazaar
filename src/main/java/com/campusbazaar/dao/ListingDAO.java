package com.campusbazaar.dao;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Listing;
import java.util.ArrayList;
import java.util.List;

public class ListingDAO {
    private final DataStore dataStore = DataStore.getInstance();

    public List<Listing> findAll() {
        return new ArrayList<>(dataStore.getListings());
    }

    public Listing findById(int id) {
        return dataStore.getListingById(id);
    }

    public void save(Listing listing) {
        dataStore.addListing(listing);
    }

    public boolean delete(int id) {
        return dataStore.removeListing(id);
    }
}

