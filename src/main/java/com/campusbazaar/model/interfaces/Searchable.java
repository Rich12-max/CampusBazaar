package com.campusbazaar.model.interfaces;

public interface Searchable {
    boolean matchesSearch(String query);
    boolean matchesCategory(int categoryId);
    boolean matchesPriceRange(double minPrice, double maxPrice);
}

