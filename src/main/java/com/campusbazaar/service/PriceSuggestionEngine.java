package com.campusbazaar.service;

import com.campusbazaar.model.Listing;
import com.campusbazaar.model.SaleListing;
import com.campusbazaar.model.enums.ItemCondition;
import java.util.List;

/**
 * Rule-Based Smart Pricing Engine.
 * Calculates recommended fair campus valuation based on:
 * 1. Category depreciation baseline
 * 2. Product physical condition multiplier
 * 3. Market average of comparable active listings
 */
public class PriceSuggestionEngine {

    public static class PriceSuggestion {
        private final double suggestedMin;
        private final double suggestedMax;
        private final double recommendedFair;
        private final String reasoning;

        public PriceSuggestion(double suggestedMin, double suggestedMax, double recommendedFair, String reasoning) {
            this.suggestedMin = suggestedMin;
            this.suggestedMax = suggestedMax;
            this.recommendedFair = recommendedFair;
            this.reasoning = reasoning;
        }

        public double getSuggestedMin() {
            return suggestedMin;
        }

        public double getSuggestedMax() {
            return suggestedMax;
        }

        public double getRecommendedFair() {
            return recommendedFair;
        }

        public String getReasoning() {
            return reasoning;
        }

        @Override
        public String toString() {
            return "Suggested: ₹" + String.format("%.0f", suggestedMin) + " - ₹" + String.format("%.0f", suggestedMax) +
                    " (Fair Target: ₹" + String.format("%.0f", recommendedFair) + ") | " + reasoning;
        }
    }

    public static PriceSuggestion calculatePrice(int categoryId, double originalPrice, ItemCondition condition, List<Listing> activeListings) {
        if (originalPrice <= 0) {
            originalPrice = 500.0; // fallback reasonable reference
        }

        // 1. Category Base Depreciation Factor (campus specific)
        // Category 1: Academic Books (retains 60% base)
        // Category 2: Electronics (retains 50% base)
        // Category 3: Lab Equipment (retains 65% base)
        // Category 4: Hostel & Furniture (retains 45% base)
        // Category 5: Sports & Cycles (retains 55% base)
        // Category 6: Stationery (retains 40% base)
        double categoryBaseFactor = 0.55;
        String catName = "General Goods";
        switch (categoryId) {
            case 1:
                categoryBaseFactor = 0.60;
                catName = "Academic Books";
                break;
            case 2:
                categoryBaseFactor = 0.50;
                catName = "Electronics";
                break;
            case 3:
                categoryBaseFactor = 0.65;
                catName = "Lab Equipment";
                break;
            case 4:
                categoryBaseFactor = 0.45;
                catName = "Hostel Living";
                break;
            case 5:
                categoryBaseFactor = 0.55;
                catName = "Cycles & Sports";
                break;
            case 6:
                categoryBaseFactor = 0.40;
                catName = "Stationery";
                break;
            default:
                categoryBaseFactor = 0.50;
        }

        // 2. Condition Multiplier
        double conditionMultiplier = condition != null ? condition.getValueMultiplier() : 0.70;

        // Baseline calculated price
        double baseCalculated = originalPrice * categoryBaseFactor * conditionMultiplier;

        // 3. Comparable Active Listings adjustment
        double marketSum = 0;
        int marketCount = 0;
        if (activeListings != null) {
            for (Listing l : activeListings) {
                if (l instanceof SaleListing && l.getProduct() != null && l.getProduct().getCategoryId() == categoryId) {
                    marketSum += ((SaleListing) l).getSellingPrice();
                    marketCount++;
                }
            }
        }

        double finalFair;
        String compReason;
        if (marketCount > 0) {
            double marketAvg = marketSum / marketCount;
            // 70% model base, 30% active campus market average
            finalFair = (baseCalculated * 0.70) + (marketAvg * 0.30);
            compReason = "Based on " + condition.getDisplayName() + " condition in " + catName +
                    " and " + marketCount + " existing campus listing(s) averaging ₹" + String.format("%.0f", marketAvg) + ".";
        } else {
            finalFair = baseCalculated;
            compReason = "Based on " + condition.getDisplayName() + " condition in " + catName +
                    " with " + String.format("%.0f", (categoryBaseFactor * conditionMultiplier * 100)) + "% depreciation retention.";
        }

        double min = Math.max(50.0, finalFair * 0.85);
        double max = Math.min(originalPrice * 0.95, finalFair * 1.15);

        return new PriceSuggestion(min, max, finalFair, compReason);
    }
}

