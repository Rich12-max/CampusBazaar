package com.campusbazaar.ui.javafx.views;

import com.campusbazaar.model.BarterListing;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Peer-to-Peer Barter & Resource Swaps View in JavaFX.
 * Manages zero-cash direct campus asset trades between university students.
 */
public class BarterSwapsView extends VBox {

    private final List<BarterListing> barterListings;

    public BarterSwapsView(List<BarterListing> barterListings) {
        this.barterListings = barterListings;
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(18);
        setPadding(new Insets(10));

        Label header = new Label("🔄 Peer-to-Peer Direct Resource Swaps");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        VBox infoBox = new VBox(6);
        infoBox.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 16px;");

        Label ruleTitle = new Label("🤝 Zero-Cash Barter Exchange Rules:");
        ruleTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 13px; -fx-text-fill: #10B981;");

        Label ruleDesc = new Label(
            "Trade books, calculators, lab coats, and cycles directly with peers without spending money. " +
            "Propose an exchange item, agree on mutual condition, and complete the swap at a verified campus safe landmark."
        );
        ruleDesc.setWrapText(true);
        ruleDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

        infoBox.getChildren().addAll(ruleTitle, ruleDesc);

        FlowPane grid = new FlowPane(16, 16);
        for (BarterListing item : barterListings) {
            VBox card = new VBox(8);
            card.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 16px;");
            card.setPrefWidth(280);

            String itemTitle = (item.getProduct() != null) ? item.getProduct().getTitle() : "Swap Item";
            Label title = new Label(itemTitle);
            title.setStyle("-fx-font-weight: 800; -fx-font-size: 14px; -fx-text-fill: -cb-text;");

            Label lookingFor = new Label("Looking for: " + item.getPreferredExchangeItem());
            lookingFor.setStyle("-fx-font-weight: bold; -fx-text-fill: #10B981; -fx-font-size: 12px;");

            Label seller = new Label("Offered by: " + (item.getSeller() != null ? item.getSeller().getFullName() : "Student"));
            seller.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

            Button swapBtn = new Button("🔄 Propose Item Swap");
            swapBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #10B981, #059669); -fx-text-fill: white; -fx-background-radius: 9999px; -fx-font-weight: bold; -fx-cursor: hand;");
            swapBtn.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog("My 2nd Sem Engineering Chemistry Book");
                dialog.setTitle("Propose Swap");
                dialog.setHeaderText("Offer an exchange item to " + (item.getSeller() != null ? item.getSeller().getFullName() : "seller"));
                dialog.setContentText("What item would you like to trade?");
                dialog.showAndWait().ifPresent(offer -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Trade proposal sent successfully!");
                    alert.showAndWait();
                });
            });

            card.getChildren().addAll(title, lookingFor, seller, swapBtn);
            grid.getChildren().add(card);
        }

        getChildren().addAll(header, infoBox, grid);
    }
}
