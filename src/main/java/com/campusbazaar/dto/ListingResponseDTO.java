package com.campusbazaar.dto;

import com.campusbazaar.model.*;
import com.campusbazaar.util.JsonUtil;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data Transfer Object for Listing Entities.
 * Polymorphically extracts price displays, deposit details, and seller credentials.
 */
public class ListingResponseDTO {
    private int id;
    private String title;
    private String description;
    private double price;
    private String pricingDisplay;
    private String category;
    private String condition;
    private String sellerName;
    private String sellerRoll;
    private String sellerDept;
    private String imageUrl;
    private String listingType;
    private boolean isAvailable;
    private double ratePerDay;
    private double deposit;
    private String preferredExchange;

    public ListingResponseDTO() {}

    public static ListingResponseDTO fromListing(Listing listing) {
        ListingResponseDTO dto = new ListingResponseDTO();
        dto.id = listing.getListingId();
        dto.listingType = listing.getListingType().name();
        dto.isAvailable = listing.getStatus() == com.campusbazaar.model.enums.ListingStatus.AVAILABLE;
        dto.pricingDisplay = listing.getPricingDisplay();
        dto.price = listing.getEffectivePrice();

        Product prod = listing.getProduct();
        if (prod != null) {
            dto.title = prod.getTitle();
            dto.description = prod.getDescription();
            dto.condition = (prod.getCondition() != null) ? prod.getCondition().name().replace("_", " ") : "GOOD";
            dto.imageUrl = (prod.getImagePath() != null && !prod.getImagePath().trim().isEmpty())
                    ? prod.getImagePath()
                    : "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&q=80&w=400";
            Category cat = com.campusbazaar.database.DataStore.getInstance().getCategoryById(prod.getCategoryId());
            dto.category = (cat != null) ? cat.getCategoryName() : "General";
        }

        Student seller = listing.getSeller();
        if (seller != null) {
            dto.sellerName = seller.getFullName();
            dto.sellerRoll = seller.getStudentIdNumber();
            dto.sellerDept = seller.getDepartment();
        } else {
            dto.sellerName = "Verified Campus Student";
            dto.sellerRoll = "2023-CS-042";
            dto.sellerDept = "Computer Science & Engg";
        }

        if (listing instanceof RentalListing) {
            RentalListing rent = (RentalListing) listing;
            dto.ratePerDay = rent.getDailyRentalPrice();
            dto.deposit = rent.getSecurityDeposit();
        } else if (listing instanceof BarterListing) {
            BarterListing barter = (BarterListing) listing;
            dto.preferredExchange = barter.getPreferredExchangeItem();
        }

        return dto;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("title", title);
        m.put("description", description);
        m.put("price", price);
        m.put("pricingDisplay", pricingDisplay);
        m.put("category", category);
        m.put("condition", condition);
        m.put("sellerName", sellerName);
        m.put("sellerRoll", sellerRoll);
        m.put("sellerDept", sellerDept);
        m.put("imageUrl", imageUrl);
        m.put("listingType", listingType);
        m.put("isAvailable", isAvailable);
        m.put("ratePerDay", ratePerDay);
        m.put("deposit", deposit);
        m.put("preferredExchange", preferredExchange);
        return m;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getPricingDisplay() { return pricingDisplay; }
    public String getCategory() { return category; }
    public String getCondition() { return condition; }
    public String getSellerName() { return sellerName; }
    public String getSellerRoll() { return sellerRoll; }
    public String getImageUrl() { return imageUrl; }
    public String getListingType() { return listingType; }
    public boolean isAvailable() { return isAvailable; }
}
