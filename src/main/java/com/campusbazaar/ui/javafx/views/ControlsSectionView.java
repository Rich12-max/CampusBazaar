package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.function.Consumer;

/**
 * Filter and Controls Bar View in JavaFX.
 * Provides live search, category filtering, listing type filters, and price sorting.
 */
public class ControlsSectionView extends HBox {

    public static class FilterCriteria {
        public String query = "";
        public String category = "All Categories";
        public String listingType = "All Types";
        public String sortBy = "Newest First";
    }

    private final Consumer<FilterCriteria> onFilterChanged;
    private final FilterCriteria criteria = new FilterCriteria();

    private TextField searchField;
    private ComboBox<String> categoryCombo;
    private ComboBox<String> typeCombo;
    private ComboBox<String> sortCombo;

    public ControlsSectionView(Consumer<FilterCriteria> onFilterChanged) {
        this.onFilterChanged = onFilterChanged;
        initializeUI();
    }

    private void initializeUI() {
        getStyleClass().add("controls-card");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);

        // 1. Search Field
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by title, subject, branch or equipment...");
        searchField.getStyleClass().add("search-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            criteria.query = newVal.trim();
            triggerFilter();
        });

        // 2. Category Filter
        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(
            "All Categories",
            "Academic Textbooks",
            "Electronics & Calculators",
            "Lab & Drawing Tools",
            "Hostel Essentials",
            "Cycles & Mobility"
        );
        categoryCombo.setValue("All Categories");
        categoryCombo.getStyleClass().add("filter-combo");
        categoryCombo.setOnAction(e -> {
            criteria.category = categoryCombo.getValue();
            triggerFilter();
        });

        // 3. Listing Type Filter
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("All Types", "Outright Sale", "Campus Rental", "Peer Item Swap");
        typeCombo.setValue("All Types");
        typeCombo.getStyleClass().add("filter-combo");
        typeCombo.setOnAction(e -> {
            criteria.listingType = typeCombo.getValue();
            triggerFilter();
        });

        // 4. Sort By Filter
        sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Newest First", "Price: Low to High", "Price: High to Low");
        sortCombo.setValue("Newest First");
        sortCombo.getStyleClass().add("filter-combo");
        sortCombo.setOnAction(e -> {
            criteria.sortBy = sortCombo.getValue();
            triggerFilter();
        });

        getChildren().addAll(searchField, categoryCombo, typeCombo, sortCombo);
    }

    private void triggerFilter() {
        if (onFilterChanged != null) {
            onFilterChanged.accept(criteria);
        }
    }

    public void setSearchQuery(String query) {
        if (searchField != null) {
            searchField.setText(query);
        }
    }

    public TextField getSearchField() {
        return searchField;
    }
}

