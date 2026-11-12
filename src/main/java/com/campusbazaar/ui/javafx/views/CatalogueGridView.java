package com.campusbazaar.ui.javafx.views;

import com.campusbazaar.model.Listing;
import com.campusbazaar.ui.javafx.components.ProductCardNode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.function.Consumer;

/**
 * Catalogue Grid View in JavaFX.
 * Renders the responsive grid of campus listings matching the web marketplace catalogue.
 */
public class CatalogueGridView extends VBox {

    private final Consumer<Listing> onActionRequested;
    private final Consumer<Listing> onDetailsRequested;
    private final Consumer<Listing> onWishlistToggled;

    private Label countBadge;
    private FlowPane gridPane;

    public CatalogueGridView(Consumer<Listing> onActionRequested,
                             Consumer<Listing> onDetailsRequested,
                             Consumer<Listing> onWishlistToggled) {
        this.onActionRequested = onActionRequested;
        this.onDetailsRequested = onDetailsRequested;
        this.onWishlistToggled = onWishlistToggled;
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(16);

        // 1. Section Header Row
        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Available Campus Items");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        countBadge = new Label("0 items");
        countBadge.setStyle("-fx-background-color: -cb-primary-light; -fx-text-fill: -cb-primary; -fx-font-size: 11px; -fx-font-weight: 800; -fx-background-radius: 9999px; -fx-padding: 3px 10px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(titleLabel, countBadge, spacer);

        // 2. FlowPane Grid
        gridPane = new FlowPane(16, 16);
        gridPane.setAlignment(Pos.TOP_LEFT);

        getChildren().addAll(headerRow, gridPane);
    }

    public void renderListings(List<Listing> listings) {
        gridPane.getChildren().clear();

        if (listings == null || listings.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setStyle("-fx-padding: 40px; -fx-background-color: -cb-card-bg; -fx-background-radius: 16px; -fx-border-color: -cb-border; -fx-border-style: dashed; -fx-border-radius: 16px;");

            Label icon = new Label("🔍");
            icon.setStyle("-fx-font-size: 32px;");

            Label msg = new Label("No matching campus items found. Try different keywords or filters!");
            msg.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: -cb-text-muted;");

            emptyBox.getChildren().addAll(icon, msg);
            gridPane.getChildren().add(emptyBox);
            countBadge.setText("0 items");
            return;
        }

        countBadge.setText(listings.size() + " items");

        for (Listing item : listings) {
            ProductCardNode card = new ProductCardNode(item, onActionRequested, onDetailsRequested, onWishlistToggled);
            gridPane.getChildren().add(card);
        }
    }
}
