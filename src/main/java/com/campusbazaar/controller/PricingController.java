package com.campusbazaar.controller;

import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.dto.PricingEstimateDTO;
import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.service.PriceSuggestionEngine;
import com.campusbazaar.util.JsonUtil;

import java.util.Collections;

/**
 * REST Controller for Rule-Based Campus Price Suggestion Engine.
 * Executes fair valuation formulas in Core Java backend.
 */
public class PricingController {

    public String handleEstimate(String jsonBody) {
        double originalPrice = JsonUtil.getDouble(jsonBody, "originalPrice", 1000.0);
        int categoryId = JsonUtil.getInt(jsonBody, "categoryId", 2);
        String condStr = JsonUtil.getString(jsonBody, "condition", "LIKE_NEW").toUpperCase().replace(" ", "_");

        ItemCondition condition;
        try {
            condition = ItemCondition.valueOf(condStr);
        } catch (Exception e) {
            condition = ItemCondition.LIKE_NEW;
        }

        PriceSuggestionEngine.PriceSuggestion sug = PriceSuggestionEngine.calculatePrice(
            categoryId, originalPrice, condition, Collections.emptyList()
        );

        double catBaseline;
        switch (categoryId) {
            case 1: catBaseline = 0.60; break;
            case 2: catBaseline = 0.50; break;
            case 3: catBaseline = 0.65; break;
            case 4: catBaseline = 0.45; break;
            case 5: catBaseline = 0.55; break;
            case 6: catBaseline = 0.40; break;
            default: catBaseline = 0.50; break;
        }

        double condMultiplier = condition.getValueMultiplier();

        PricingEstimateDTO dto = new PricingEstimateDTO(
            sug.getRecommendedFair(),
            sug.getSuggestedMin(),
            sug.getSuggestedMax(),
            sug.getReasoning(),
            catBaseline,
            condMultiplier
        );

        return ApiResponse.ok("Valuation estimated successfully", dto.toMap()).toJson();
    }
}
