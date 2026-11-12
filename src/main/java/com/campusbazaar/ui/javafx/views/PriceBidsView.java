package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Counter-Offer & Price Bids View in JavaFX.
 * Manages 3-round bargaining negotiations between campus buyers and sellers.
 */
public class PriceBidsView extends VBox {

    public PriceBidsView() {
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(18);
        setPadding(new Insets(10));

        Label header = new Label("💬 Student Counter-Offer & Price Bargaining Threads");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        VBox threadCard = new VBox(12);
        threadCard.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 18px;");

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label itemTitle = new Label("Casio fx-991EX ClassWiz Scientific Calculator");
        itemTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 15px; -fx-text-fill: -cb-text;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label roundBadge = new Label("Round 1 of 3");
        roundBadge.setStyle("-fx-background-color: -cb-primary-light; -fx-text-fill: -cb-primary; -fx-font-weight: bold; -fx-padding: 3px 10px; -fx-background-radius: 9999px;");

        titleRow.getChildren().addAll(itemTitle, spacer, roundBadge);

        Label details = new Label("Original Listing Price: ₹950\nBuyer's Offer: ₹800 (Submitted by Aarav Patel)");
        details.setStyle("-fx-font-size: 12px; -fx-text-fill: -cb-text-muted; -fx-line-spacing: 4px;");

        HBox actionsRow = new HBox(10);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        Button acceptBtn = new Button("Accept ₹800 ✅");
        acceptBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        acceptBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Offer accepted! Meetup scheduled at Library Steps.");
            alert.showAndWait();
        });

        Button counterBtn = new Button("Counter-Offer 🔄");
        counterBtn.setStyle("-fx-background-color: -cb-primary; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        counterBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("880");
            dialog.setTitle("Submit Counter-Offer");
            dialog.setHeaderText("Make a counter-offer for Casio fx-991EX");
            dialog.setContentText("Your price (₹):");
            dialog.showAndWait().ifPresent(price -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Counter-offer of ₹" + price + " sent to buyer.");
                alert.showAndWait();
            });
        });

        Button declineBtn = new Button("Decline ✕");
        declineBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        declineBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Offer declined.");
            alert.showAndWait();
        });

        actionsRow.getChildren().addAll(acceptBtn, counterBtn, declineBtn);
        threadCard.getChildren().addAll(titleRow, details, actionsRow);

        getChildren().addAll(header, threadCard);
    }
}
