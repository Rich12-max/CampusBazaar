package com.campusbazaar.ui.javafx.dialogs;

import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.service.PriceSuggestionEngine;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.Collections;

/**
 * Smart Price Suggestion Modal in JavaFX.
 * Demonstrates mathematical and business rule calculation in Core Java.
 */
public class SmartPricingModal extends Dialog<Void> {

    public SmartPricingModal() {
        setTitle("💡 Rule-Based Smart Campus Pricing Engine");
        setHeaderText(null);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        pane.setStyle("-fx-background-color: -cb-card-bg; -fx-padding: 16px;");
        pane.getButtonTypes().add(ButtonType.CLOSE);

        initializeUI(pane);
    }

    private void initializeUI(DialogPane pane) {
        VBox container = new VBox(14);
        container.setPrefWidth(460);

        Label title = new Label("💡 Rule-Based Fair Valuation Engine");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        Label desc = new Label(
            "Prevent price gouging and ensure students get fair returns. " +
            "Valuations combine category baseline retention with cosmetic condition multipliers."
        );
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));

        TextField origPriceField = new TextField("1200");
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll(
            "Academic Textbooks", "Electronics & Calculators", "Lab & Drawing Tools",
            "Hostel Essentials", "Cycles & Mobility"
        );
        catCombo.setValue("Electronics & Calculators");

        ComboBox<ItemCondition> condCombo = new ComboBox<>();
        condCombo.getItems().addAll(ItemCondition.values());
        condCombo.setValue(ItemCondition.LIKE_NEW);

        grid.add(new Label("Original MRP / Price (₹):"), 0, 0);
        grid.add(origPriceField, 1, 0);
        grid.add(new Label("Product Category:"), 0, 1);
        grid.add(catCombo, 1, 1);
        grid.add(new Label("Cosmetic Condition:"), 0, 2);
        grid.add(condCombo, 1, 2);

        // Result Box
        VBox resultBox = new VBox(6);
        resultBox.setStyle("-fx-background-color: -cb-primary-light; -fx-border-color: -cb-border; -fx-border-radius: 14px; -fx-background-radius: 14px; -fx-padding: 14px;");

        Label recPriceLabel = new Label("Recommended Price: ₹648");
        recPriceLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: -cb-primary;");

        Label rangeLabel = new Label("Fair Campus Range: ₹583 - ₹713");
        rangeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: -cb-text;");

        Label formulaLabel = new Label("Applied 60% category baseline × 90% condition score = 54% retention value.");
        formulaLabel.setWrapText(true);
        formulaLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: -cb-text-muted;");

        resultBox.getChildren().addAll(recPriceLabel, rangeLabel, formulaLabel);

        // Live calculation listener
        Runnable recalculate = () -> {
            try {
                double orig = Double.parseDouble(origPriceField.getText().trim());
                int catId = catCombo.getSelectionModel().getSelectedIndex() + 1;
                ItemCondition cond = condCombo.getValue();

                PriceSuggestionEngine.PriceSuggestion sug = PriceSuggestionEngine.calculatePrice(
                    catId, orig, cond, Collections.emptyList()
                );

                recPriceLabel.setText(String.format("Recommended Price: ₹%.0f", sug.getRecommendedFair()));
                rangeLabel.setText(String.format("Fair Campus Range: ₹%.0f - ₹%.0f", sug.getSuggestedMin(), sug.getSuggestedMax()));
                formulaLabel.setText(sug.getReasoning());
            } catch (Exception ignored) {}
        };

        origPriceField.textProperty().addListener((obs, o, n) -> recalculate.run());
        catCombo.setOnAction(e -> recalculate.run());
        condCombo.setOnAction(e -> recalculate.run());

        container.getChildren().addAll(title, desc, grid, resultBox);
        pane.setContent(container);
    }
}
