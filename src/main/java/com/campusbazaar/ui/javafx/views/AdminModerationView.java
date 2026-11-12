package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Campus Administrator Moderation Center View in JavaFX.
 * Provides controls for listing takedowns, student account verification, and dispute resolution.
 */
public class AdminModerationView extends VBox {

    public AdminModerationView() {
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(18);
        setPadding(new Insets(10));

        Label header = new Label("🛡️ Campus Security & Moderation Center");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        // Stat Cards Row
        HBox statsRow = new HBox(14);
        statsRow.getChildren().addAll(
            createStatCard("Total Verified Students", "412", "#2563EB"),
            createStatCard("Active Listings", "84", "#10B981"),
            createStatCard("Pending Reports", "2", "#EF4444"),
            createStatCard("Completed Handovers", "156", "#8B5CF6")
        );

        // Moderation Table / Reports List
        VBox reportCard = new VBox(10);
        reportCard.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 18px;");

        Label reportTitle = new Label("🚩 Active Listing Reports Under Review");
        reportTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 14px; -fx-text-fill: -cb-text;");

        HBox reportItem = new HBox(12);
        reportItem.setAlignment(Pos.CENTER_LEFT);
        reportItem.setStyle("-fx-background-color: -cb-primary-light; -fx-padding: 12px; -fx-background-radius: 12px;");

        VBox reportDetails = new VBox(2);
        Label rItemName = new Label("Reported: Scientific Calculator (fx-82MS)");
        rItemName.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: -cb-text;");
        Label rReason = new Label("Reason: Listed price is higher than retail MRP • Reporter: Priya Sharma (2023-IT-019)");
        rReason.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");
        reportDetails.getChildren().addAll(rItemName, rReason);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button dismissBtn = new Button("Dismiss");
        dismissBtn.setStyle("-fx-background-color: #64748B; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        dismissBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report dismissed. No action taken.");
            alert.showAndWait();
        });

        Button removeBtn = new Button("Remove Listing ✕");
        removeBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Listing removed from campus marketplace.");
            alert.showAndWait();
        });

        reportItem.getChildren().addAll(reportDetails, spacer, dismissBtn, removeBtn);
        reportCard.getChildren().addAll(reportTitle, reportItem);

        getChildren().addAll(header, statsRow, reportCard);
    }

    private VBox createStatCard(String label, String value, String colorHex) {
        VBox card = new VBox(4);
        card.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 14px; -fx-background-radius: 14px; -fx-padding: 16px; -fx-min-width: 140px;");

        Label valLbl = new Label(value);
        valLbl.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: " + colorHex + ";");

        Label nameLbl = new Label(label);
        nameLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted; -fx-font-weight: 600;");

        card.getChildren().addAll(valLbl, nameLbl);
        return card;
    }
}
