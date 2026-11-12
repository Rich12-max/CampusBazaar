package com.campusbazaar.ui.javafx.components;

import com.campusbazaar.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import java.util.function.Consumer;

/**
 * Polymorphic Product Card Component in JavaFX.
 * Demonstrates Object-Oriented Polymorphism by dynamically adapting badges,
 * pricing formulas, and action buttons according to the concrete Listing subtype.
 */
public class ProductCardNode extends VBox {

    private final Listing listing;
    private final Consumer<Listing> onActionRequested;
    private final Consumer<Listing> onDetailsRequested;
    private final Consumer<Listing> onWishlistToggled;
    private boolean isWishlisted = false;

    public ProductCardNode(Listing listing,
                           Consumer<Listing> onActionRequested,
                           Consumer<Listing> onDetailsRequested,
                           Consumer<Listing> onWishlistToggled) {
        this.listing = listing;
        this.onActionRequested = onActionRequested;
        this.onDetailsRequested = onDetailsRequested;
        this.onWishlistToggled = onWishlistToggled;

        initializeUI();
    }

    private void initializeUI() {
        getStyleClass().add("product-card");
        setSpacing(10);
        setPrefWidth(290);
        setMaxWidth(340);

        Product prod = listing.getProduct();
        String itemTitle = (prod != null) ? prod.getTitle() : "Campus Resource";
        String imgUrl = (prod != null && prod.getImagePath() != null && !prod.getImagePath().trim().isEmpty())
                ? prod.getImagePath()
                : "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&q=80&w=300";

        // 1. Image Preview with Top Badges Overlay
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(170);
        imageContainer.setMaxHeight(170);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(270);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(270, 170);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imageView.setClip(clip);

        try {
            Image img = new Image(imgUrl, 270, 170, false, true, true);
            imageView.setImage(img);
        } catch (Exception ignored) {}

        // Top Badges Overlay (Type badge on left, Condition on right)
        HBox topBadges = new HBox(8);
        topBadges.setAlignment(Pos.TOP_LEFT);
        topBadges.setStyle("-fx-padding: 8px;");

        Label typeBadge = new Label();
        if (listing instanceof SaleListing) {
            typeBadge.setText("SALE");
            typeBadge.getStyleClass().add("badge-type-sale");
        } else if (listing instanceof RentalListing) {
            typeBadge.setText("RENTAL");
            typeBadge.getStyleClass().add("badge-type-rent");
        } else if (listing instanceof BarterListing) {
            typeBadge.setText("BARTER");
            typeBadge.getStyleClass().add("badge-type-barter");
        } else {
            typeBadge.setText(listing.getListingType().name());
            typeBadge.getStyleClass().add("badge-type-sale");
        }

        Region badgeSpacer = new Region();
        HBox.setHgrow(badgeSpacer, Priority.ALWAYS);

        String condStr = (prod != null && prod.getCondition() != null)
                ? prod.getCondition().name().replace("_", " ")
                : "GOOD";
        Label conditionBadge = new Label(condStr);
        conditionBadge.getStyleClass().add("badge-condition");

        topBadges.getChildren().addAll(typeBadge, badgeSpacer, conditionBadge);
        imageContainer.getChildren().addAll(imageView, topBadges);

        // 2. Card Content (Title, Price, Seller)
        Label titleLabel = new Label(itemTitle);
        titleLabel.getStyleClass().add("card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxHeight(40);

        // Polymorphic Price Display
        Label priceLabel = new Label(listing.getPricingDisplay());
        priceLabel.getStyleClass().add("card-price");

        // Metadata: Seller Name
        String sellerInfo = "🎓 " + ((listing.getSeller() != null) ? listing.getSeller().getFullName() : "Verified Student");
        Label metaLabel = new Label(sellerInfo);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

        // 3. Action Buttons Row
        HBox actionsRow = new HBox(8);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        Button primaryActionBtn = new Button();
        primaryActionBtn.getStyleClass().add("btn-card-action");
        HBox.setHgrow(primaryActionBtn, Priority.ALWAYS);
        primaryActionBtn.setMaxWidth(Double.MAX_VALUE);

        if (listing instanceof RentalListing) {
            primaryActionBtn.setText("⏳ Rent Item");
        } else if (listing instanceof BarterListing) {
            primaryActionBtn.setText("🔄 Propose Swap");
        } else {
            primaryActionBtn.setText("🏷️ Request Buy");
        }

        primaryActionBtn.setOnAction(e -> {
            if (onActionRequested != null) onActionRequested.accept(listing);
        });

        Button detailsBtn = new Button("View");
        detailsBtn.setStyle("-fx-background-color: -cb-primary-light; -fx-text-fill: -cb-primary; -fx-background-radius: 9999px; -fx-font-weight: bold; -fx-font-size: 11px; -fx-cursor: hand;");
        detailsBtn.setOnAction(e -> {
            if (onDetailsRequested != null) onDetailsRequested.accept(listing);
        });

        Button wishlistBtn = new Button("🤍");
        wishlistBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 13px;");
        wishlistBtn.setTooltip(new Tooltip("Save to Wishlist"));
        wishlistBtn.setOnAction(e -> {
            isWishlisted = !isWishlisted;
            wishlistBtn.setText(isWishlisted ? "💖" : "🤍");
            if (onWishlistToggled != null) onWishlistToggled.accept(listing);
        });

        actionsRow.getChildren().addAll(primaryActionBtn, detailsBtn, wishlistBtn);

        getChildren().addAll(imageContainer, titleLabel, priceLabel, metaLabel, actionsRow);
    }

    public Listing getListing() {
        return listing;
    }
}
