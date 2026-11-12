package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Hero Banner View in JavaFX.
 * Recreates the vibrant university banner, floral identity badge,
 * campus trust highlights, and instant call-to-action buttons.
 */
public class HeroBannerView extends VBox {

    private final Runnable onSellItemClicked;
    private final Runnable onFindItemClicked;

    public HeroBannerView(Runnable onSellItemClicked, Runnable onFindItemClicked) {
        this.onSellItemClicked = onSellItemClicked;
        this.onFindItemClicked = onFindItemClicked;
        initializeUI();
    }

    private void initializeUI() {
        getStyleClass().add("hero-banner");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(14);

        // 1. Floral Badge
        HBox badgeBox = new HBox(6);
        badgeBox.setAlignment(Pos.CENTER_LEFT);
        Label badgeLabel = new Label("🌸 Official University Peer-to-Peer Student Exchange 🌸");
        badgeLabel.getStyleClass().add("hero-floral-badge");
        badgeBox.getChildren().add(badgeLabel);

        // 2. Main Title
        Label titleLabel = new Label("Campus Peer-to-Peer Marketplace 🌸");
        titleLabel.getStyleClass().add("hero-title");

        // 3. Subtitle
        Label subtitleLabel = new Label(
            "Centralized university resource sharing — buy, sell, exchange and rent academic textbooks, " +
            "scientific calculators, mini drafters, hostel essentials and bicycles safely with verified students on campus."
        );
        subtitleLabel.getStyleClass().add("hero-subtitle");
        subtitleLabel.setWrapText(true);

        // 4. Trust Chips (FlowPane for responsiveness)
        FlowPane chipsPane = new FlowPane(8, 8);
        chipsPane.setAlignment(Pos.CENTER_LEFT);

        String[] chips = {
            "🛡️ Verified College Students Only",
            "🤝 Safe Campus Handover Spots",
            "💡 Rule-Based Fair Price Guidance",
            "🔄 Zero-Fee Direct Peer Swaps"
        };

        for (String chipText : chips) {
            Label chip = new Label(chipText);
            chip.getStyleClass().add("hero-chip");
            chipsPane.getChildren().add(chip);
        }

        // 5. Call To Action Buttons
        HBox ctaBox = new HBox(12);
        ctaBox.setAlignment(Pos.CENTER_LEFT);

        Button sellBtn = new Button("🏷️ + Add Sell Item");
        sellBtn.getStyleClass().add("hero-sell-btn");
        sellBtn.setOnAction(e -> {
            if (onSellItemClicked != null) onSellItemClicked.run();
        });

        Button findBtn = new Button("🔍 Find Any Item");
        findBtn.getStyleClass().add("hero-find-btn");
        findBtn.setOnAction(e -> {
            if (onFindItemClicked != null) onFindItemClicked.run();
        });

        ctaBox.getChildren().addAll(sellBtn, findBtn);

        getChildren().addAll(badgeBox, titleLabel, subtitleLabel, chipsPane, ctaBox);
    }
}

