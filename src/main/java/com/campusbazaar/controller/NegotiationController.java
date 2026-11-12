package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.model.PriceNegotiation;
import com.campusbazaar.model.enums.NegotiationStatus;
import com.campusbazaar.util.JsonUtil;

import java.util.*;

/**
 * REST Controller for 3-Round Counter-Offer Price Bargaining.
 * Enforces multi-round negotiation rules and agreement transitions in Java.
 */
public class NegotiationController {

    private final DataStore dataStore = DataStore.getInstance();

    public String handleGetNegotiations() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (PriceNegotiation neg : dataStore.getPriceNegotiations()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", neg.getNegotiationId());
            map.put("listingId", neg.getListingId());
            map.put("itemTitle", neg.getItemTitle());
            map.put("originalPrice", neg.getListedPrice());
            map.put("currentOfferedPrice", neg.getCurrentOfferPrice());
            map.put("currentRound", neg.getRoundNumber());
            map.put("maxRounds", PriceNegotiation.MAX_ROUNDS);
            map.put("status", neg.getStatus().name());
            map.put("buyerName", neg.getBuyerName());
            map.put("sellerName", neg.getSellerName());
            result.add(map);
        }
        return ApiResponse.ok("Negotiations retrieved", result).toJson();
    }

    public String handleCounterOffer(String jsonBody) {
        int id = JsonUtil.getInt(jsonBody, "negotiationId", 1);
        double counterPrice = JsonUtil.getDouble(jsonBody, "counterPrice", 850.0);

        for (PriceNegotiation neg : dataStore.getPriceNegotiations()) {
            if (neg.getNegotiationId() == id) {
                if (neg.getRoundNumber() >= PriceNegotiation.MAX_ROUNDS) {
                    return ApiResponse.error("Maximum 3 negotiation rounds reached for this item.").toJson();
                }
                neg.sellerCounter(counterPrice, "Counter-offer via CampusBazaar web portal");
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("negotiationId", neg.getNegotiationId());
                map.put("newPrice", counterPrice);
                map.put("currentRound", neg.getRoundNumber());
                map.put("status", neg.getStatus().name());
                return ApiResponse.ok("Counter-offer submitted to buyer", map).toJson();
            }
        }
        return ApiResponse.error("Negotiation thread not found").toJson();
    }

    public String handleAcceptOffer(String jsonBody) {
        int id = JsonUtil.getInt(jsonBody, "negotiationId", 1);
        for (PriceNegotiation neg : dataStore.getPriceNegotiations()) {
            if (neg.getNegotiationId() == id) {
                neg.accept();
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("negotiationId", neg.getNegotiationId());
                map.put("status", neg.getStatus().name());
                map.put("agreedPrice", neg.getCurrentOfferPrice());
                return ApiResponse.ok("Offer accepted! Proceed to safe handover.", map).toJson();
            }
        }
        return ApiResponse.error("Negotiation thread not found").toJson();
    }
}
