package com.campusbazaar.dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data Transfer Object for Smart Valuation Estimation Results.
 */
public class PricingEstimateDTO {
    private final double recommendedFair;
    private final double suggestedMin;
    private final double suggestedMax;
    private final String reasoning;
    private final double categoryBaseline;
    private final double conditionMultiplier;

    public PricingEstimateDTO(double recommendedFair, double suggestedMin, double suggestedMax,
                              String reasoning, double categoryBaseline, double conditionMultiplier) {
        this.recommendedFair = recommendedFair;
        this.suggestedMin = suggestedMin;
        this.suggestedMax = suggestedMax;
        this.reasoning = reasoning;
        this.categoryBaseline = categoryBaseline;
        this.conditionMultiplier = conditionMultiplier;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("recommendedFair", recommendedFair);
        map.put("suggestedMin", suggestedMin);
        map.put("suggestedMax", suggestedMax);
        map.put("reasoning", reasoning);
        map.put("categoryBaseline", categoryBaseline);
        map.put("conditionMultiplier", conditionMultiplier);
        return map;
    }

    public double getRecommendedFair() { return recommendedFair; }
    public double getSuggestedMin() { return suggestedMin; }
    public double getSuggestedMax() { return suggestedMax; }
    public String getReasoning() { return reasoning; }
}
