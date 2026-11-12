package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.dto.ListingResponseDTO;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.*;
import com.campusbazaar.service.ListingService;
import com.campusbazaar.util.JsonUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for Campus Marketplace Catalogue Operations.
 * Handles search, category filtering, price sorting, and listing creation in Java.
 */
public class ListingController {

    private final ListingService listingService = new ListingService();
    private final DataStore dataStore = DataStore.getInstance();

    public String handleGetListings(Map<String, String> params) {
        String query = params.getOrDefault("query", "").trim().toLowerCase();
        String category = params.getOrDefault("category", "All Categories");
        String type = params.getOrDefault("type", "All Types");
        String sort = params.getOrDefault("sort", "Newest First");

        List<Listing> all = dataStore.getListings();

        List<ListingResponseDTO> results = all.stream()
            .filter(item -> {
                Product p = item.getProduct();
                if (p == null) return false;

                if (!query.isEmpty()) {
                    boolean match = p.getTitle().toLowerCase().contains(query) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(query));
                    if (!match) return false;
                }
                if (!"All Categories".equalsIgnoreCase(category)) {
                    Category cat = dataStore.getCategoryById(p.getCategoryId());
                    if (cat == null || !cat.getCategoryName().equalsIgnoreCase(category)) {
                        return false;
                    }
                }
                if (!"All Types".equalsIgnoreCase(type)) {
                    if ("Outright Sale".equalsIgnoreCase(type) && !(item instanceof SaleListing)) return false;
                    if ("Campus Rental".equalsIgnoreCase(type) && !(item instanceof RentalListing)) return false;
                    if ("Peer Item Swap".equalsIgnoreCase(type) && !(item instanceof BarterListing)) return false;
                }
                return true;
            })
            .map(ListingResponseDTO::fromListing)
            .collect(Collectors.toList());

        if ("Price: Low to High".equalsIgnoreCase(sort)) {
            results.sort(Comparator.comparingDouble(ListingResponseDTO::getPrice));
        } else if ("Price: High to Low".equalsIgnoreCase(sort)) {
            results.sort(Comparator.comparingDouble(ListingResponseDTO::getPrice).reversed());
        }

        List<Map<String, Object>> listMap = results.stream().map(ListingResponseDTO::toMap).collect(Collectors.toList());
        return ApiResponse.ok("Found " + results.size() + " listings", listMap).toJson();
    }

    public String handleGetListingById(int id) {
        for (Listing item : dataStore.getListings()) {
            if (item.getListingId() == id) {
                return ApiResponse.ok("Listing retrieved", ListingResponseDTO.fromListing(item).toMap()).toJson();
            }
        }
        return ApiResponse.error("Listing with ID " + id + " not found").toJson();
    }

    public String handleCreateListing(String jsonBody) {
        String title = JsonUtil.getString(jsonBody, "title", "").trim();
        if (title.isEmpty()) {
            return ApiResponse.error("Title is required").toJson();
        }

        String desc = JsonUtil.getString(jsonBody, "description", "Verified campus product.");
        String typeStr = JsonUtil.getString(jsonBody, "listingType", "SALE").toUpperCase();
        String condStr = JsonUtil.getString(jsonBody, "condition", "LIKE_NEW").toUpperCase();
        String img = JsonUtil.getString(jsonBody, "imageUrl", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&q=80&w=400");
        int catId = JsonUtil.getInt(jsonBody, "categoryId", 1);
        double price = JsonUtil.getDouble(jsonBody, "price", 350.0);

        ItemCondition condition;
        try {
            condition = ItemCondition.valueOf(condStr.replace(" ", "_"));
        } catch (Exception e) {
            condition = ItemCondition.LIKE_NEW;
        }

        int newId = (int) (System.currentTimeMillis() % 100000);
        Student seller = (Student) dataStore.getUserById(1);

        Product prod = new Product(newId, (seller != null ? seller.getUserId() : 1), catId, title, desc, condition, "Campus", price, img);

        Listing created;
        if ("RENTAL".equals(typeStr) || "RENT".equals(typeStr)) {
            double rate = JsonUtil.getDouble(jsonBody, "ratePerDay", 25.0);
            double dep = JsonUtil.getDouble(jsonBody, "deposit", 100.0);
            created = new RentalListing(newId, prod, seller, ListingStatus.AVAILABLE, rate, dep, 1, 30, 15.0);
        } else if ("BARTER".equals(typeStr)) {
            String preferred = JsonUtil.getString(jsonBody, "preferredExchange", "Academic Textbooks / Engineering Tools");
            created = new BarterListing(newId, prod, seller, ListingStatus.AVAILABLE, preferred, 0.0, true);
        } else {
            created = new SaleListing(newId, prod, seller, ListingStatus.AVAILABLE, price, price, true);
        }

        dataStore.addListing(created);
        return ApiResponse.ok("Listing published successfully", ListingResponseDTO.fromListing(created).toMap()).toJson();
    }
}
