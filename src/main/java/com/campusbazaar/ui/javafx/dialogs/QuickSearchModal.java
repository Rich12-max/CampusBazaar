package com.campusbazaar.ui.javafx.dialogs;

import com.campusbazaar.model.Listing;
import com.campusbazaar.model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Quick Search Modal Dialog in JavaFX.
 * Provides instant live keyboard-driven item searching matching the web floating search tool.
 */
public class QuickSearchModal extends Dialog<Listing> {

    private final List<Listing> allListings;
    private final Consumer<Listing> onItemSelected;
    private final Runnable onOpenSellItem;

    private TextField searchInput;
    private VBox resultsBox;

    public QuickSearchModal(List<Listing> allListings,
                            Consumer<Listing> onItemSelected,
                            Runnable onOpenSellItem) {
        this.allListings = allListings;
        this.onItemSelected = onItemSelected;
        this.onOpenSellItem = onOpenSellItem;

        setTitle("🔍 Find Any Campus Item");
        setHeaderText(null);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        pane.setStyle("-fx-background-color: -cb-card-bg; -fx-padding: 20px;");
        pane.getButtonTypes().add(ButtonType.CLOSE);

        initializeUI(pane);
    }

    private void initializeUI(DialogPane pane) {
        VBox container = new VBox(12);
        container.setPrefWidth(540);

        Label titleLabel = new Label("🔍 Find Any Campus Item");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        searchInput = new TextField();
        searchInput.setPromptText("Search books, calculators, drafters, kettles, cycles...");
        searchInput.getStyleClass().add("search-field");
        searchInput.setStyle("-fx-font-size: 14px; -fx-padding: 10px 14px;");

        searchInput.textProperty().addListener((obs, oldVal, newVal) -> {
            renderResults(newVal.trim());
        });

        HBox tipRow = new HBox(10);
        tipRow.setAlignment(Pos.CENTER_LEFT);
        Label tipLabel = new Label("💡 Press ESC to close");
        tipLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button sellBtn = new Button("🏷️ + Sell Item Instead");
        sellBtn.getStyleClass().add("btn-sell-action");
        sellBtn.setStyle("-fx-font-size: 11px; -fx-padding: 4px 10px;");
        sellBtn.setOnAction(e -> {
            close();
            if (onOpenSellItem != null) onOpenSellItem.run();
        });

        tipRow.getChildren().addAll(tipLabel, spacer, sellBtn);

        resultsBox = new VBox(8);
        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(320);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        container.getChildren().addAll(titleLabel, searchInput, tipRow, scrollPane);
        pane.setContent(container);

        renderResults("");
    }

    private void renderResults(String query) {
        resultsBox.getChildren().clear();

        String q = query.toLowerCase();
        List<Listing> matched = allListings.stream()
            .filter(l -> {
                Product p = l.getProduct();
                if (p == null) return false;
                if (q.isEmpty()) return true;
                return p.getTitle().toLowerCase().contains(q) ||
                       (p.getDescription() != null && p.getDescription().toLowerCase().contains(q));
            })
            .collect(Collectors.toList());

        if (matched.isEmpty()) {
            VBox emptyBox = new VBox(6);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(30));
            Label msg = new Label("No items found matching \"" + query + "\"");
            msg.setStyle("-fx-font-weight: bold; -fx-text-fill: -cb-text-muted;");
            emptyBox.getChildren().add(msg);
            resultsBox.getChildren().add(emptyBox);
            return;
        }

        for (Listing item : matched) {
            Product p = item.getProduct();
            HBox itemRow = new HBox(12);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-padding: 8px 12px; -fx-cursor: hand;");

            VBox infoBox = new VBox(2);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Label title = new Label(p != null ? p.getTitle() : "Item");
            title.setStyle("-fx-font-weight: 800; -fx-font-size: 13px; -fx-text-fill: -cb-text;");

            Label meta = new Label(item.getListingType().name() + " • " + (item.getSeller() != null ? item.getSeller().getFullName() : "Student"));
            meta.setStyle("-fx-font-size: 10px; -fx-text-fill: -cb-text-muted;");

            infoBox.getChildren().addAll(title, meta);

            Label price = new Label(item.getPricingDisplay());
            price.setStyle("-fx-font-weight: 800; -fx-font-size: 13px; -fx-text-fill: -cb-primary;");

            itemRow.getChildren().addAll(infoBox, price);

            itemRow.setOnMouseClicked(e -> {
                close();
                if (onItemSelected != null) onItemSelected.accept(item);
            });

            resultsBox.getChildren().add(itemRow);
        }
    }
}
