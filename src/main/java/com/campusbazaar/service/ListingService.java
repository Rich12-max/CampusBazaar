package com.campusbazaar.service;

import com.campusbazaar.dao.ListingDAO;
import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.ListingNotFoundException;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.model.interfaces.Searchable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListingService {
    private final ListingDAO listingDAO = new ListingDAO();
    private final DataStore dataStore = DataStore.getInstance();

    public List<Listing> getAllListings() {
        return listingDAO.findAll();
    }

    public Listing getListingById(int id) throws ListingNotFoundException {
        Listing listing = listingDAO.findById(id);
        if (listing == null) {
            throw new ListingNotFoundException(id);
        }
        return listing;
    }

    public List<Listing> searchAndFilter(String query, int categoryId, String listingTypeStr,
                                         Double minPrice, Double maxPrice, String sortBy) {
        List<Listing> all = listingDAO.findAll();

        return all.stream()
                .filter(l -> l.getStatus() == ListingStatus.AVAILABLE || l.getStatus() == ListingStatus.RESERVED)
                .filter(l -> {
                    if (l instanceof Searchable) {
                        return ((Searchable) l).matchesSearch(query);
                    }
                    return true;
                })
                .filter(l -> {
                    if (categoryId > 0 && l instanceof Searchable) {
                        return ((Searchable) l).matchesCategory(categoryId);
                    }
                    return true;
                })
                .filter(l -> {
                    if (listingTypeStr == null || listingTypeStr.equalsIgnoreCase("ALL")) return true;
                    if (listingTypeStr.equalsIgnoreCase("SALE")) return l.getListingType() == ListingType.SALE;
                    if (listingTypeStr.equalsIgnoreCase("RENTAL")) return l.getListingType() == ListingType.RENTAL;
                    return true;
                })
                .filter(l -> {
                    double effective = l.getEffectivePrice();
                    if (minPrice != null && effective < minPrice) return false;
                    if (maxPrice != null && effective > maxPrice) return false;
                    return true;
                })
                .sorted((a, b) -> {
                    if ("PRICE_ASC".equalsIgnoreCase(sortBy)) {
                        return Double.compare(a.getEffectivePrice(), b.getEffectivePrice());
                    } else if ("PRICE_DESC".equalsIgnoreCase(sortBy)) {
                        return Double.compare(b.getEffectivePrice(), a.getEffectivePrice());
                    } else {
                        // NEWEST
                        return b.getPostedAt().compareTo(a.getPostedAt());
                    }
                })
                .collect(Collectors.toList());
    }

    public SaleListing createSaleListing(Student seller, int categoryId, String title, String description,
                                         ItemCondition condition, String brand, double originalPrice,
                                         double sellingPrice, boolean isNegotiable, String imagePath) {
        int prodId = dataStore.nextProductId();
        int listId = dataStore.nextListingId();

        Product prod = new Product(prodId, seller.getUserId(), categoryId, title, description,
                condition, brand, originalPrice, imagePath);
        SaleListing listing = new SaleListing(listId, prod, seller, ListingStatus.AVAILABLE,
                sellingPrice, originalPrice, isNegotiable);
        listingDAO.save(listing);
        return listing;
    }

    public RentalListing createRentalListing(Student seller, int categoryId, String title, String description,
                                             ItemCondition condition, String brand, double originalPrice,
                                             double dailyRate, double deposit, int minDays, int maxDays,
                                             double lateFeePerDay, String imagePath) {
        int prodId = dataStore.nextProductId();
        int listId = dataStore.nextListingId();

        Product prod = new Product(prodId, seller.getUserId(), categoryId, title, description,
                condition, brand, originalPrice, imagePath);
        RentalListing listing = new RentalListing(listId, prod, seller, ListingStatus.AVAILABLE,
                dailyRate, deposit, minDays, maxDays, lateFeePerDay);
        listingDAO.save(listing);
        return listing;
    }

    public PriceSuggestionEngine.PriceSuggestion getPriceGuidance(int categoryId, double originalPrice, ItemCondition condition) {
        return PriceSuggestionEngine.calculatePrice(categoryId, originalPrice, condition, listingDAO.findAll());
    }

    public boolean deleteListing(int listingId) {
        return listingDAO.delete(listingId);
    }
}

