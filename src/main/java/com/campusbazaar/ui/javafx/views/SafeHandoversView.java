package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Campus Safe Landmark Handovers & OTP Verification View in JavaFX.
 * Coordinates verified physical product exchanges at campus safe zones.
 */
public class SafeHandoversView extends VBox {

    public SafeHandoversView() {
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(18);
        setPadding(new Insets(10));

        Label header = new Label("📍 Campus Safe Handover & OTP Verification Hub");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        // Landmark Safe Zones Info
        VBox zoneCard = new VBox(10);
        zoneCard.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 18px;");

        Label zoneTitle = new Label("🏛️ Official Campus Verified Exchange Landmarks:");
        zoneTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 14px; -fx-text-fill: -cb-primary;");

        FlowPane spots = new FlowPane(10, 10);
        String[] locations = {
            "📚 Central Library Steps",
            "☕ Student Canteen Safe Zone",
            "🏟️ Student Activity Centre (SAC)",
            "🚪 North Hostel Security Gate"
        };
        for (String spot : locations) {
            Label spotPill = new Label(spot);
            spotPill.setStyle("-fx-background-color: -cb-primary-light; -fx-text-fill: -cb-primary; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 9999px;");
            spots.getChildren().add(spotPill);
        }

        zoneCard.getChildren().addAll(zoneTitle, spots);

        // Active Handover Schedule Card
        VBox scheduleCard = new VBox(12);
        scheduleCard.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 18px;");

        Label activeTitle = new Label("🤝 Pending Handover: Engineering Mathematics Vol. 1");
        activeTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 15px; -fx-text-fill: -cb-text;");

        Label details = new Label("Buyer: Aarav Patel (2023-CS-042) • Seller: Rohan Verma (2024-ME-088)\nLocation: Central Library Steps • Scheduled Time: Today, 4:30 PM");
        details.setStyle("-fx-font-size: 12px; -fx-text-fill: -cb-text-muted; -fx-line-spacing: 4px;");

        // OTP verification box
        HBox otpBox = new HBox(12);
        otpBox.setAlignment(Pos.CENTER_LEFT);
        otpBox.setStyle("-fx-background-color: -cb-primary-light; -fx-padding: 12px; -fx-background-radius: 12px;");

        Label otpLabel = new Label("One-Time Passcode (OTP):");
        otpLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: -cb-text;");

        TextField otpInput = new TextField("492815");
        otpInput.setStyle("-fx-font-weight: 800; -fx-font-size: 16px; -fx-alignment: CENTER; -fx-pref-width: 100px; -fx-background-radius: 8px;");

        Button verifyBtn = new Button("Confirm Handover ✅");
        verifyBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9999px; -fx-cursor: hand;");
        verifyBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Handover verified successfully! Transaction is marked COMPLETE.");
            alert.showAndWait();
        });

        otpBox.getChildren().addAll(otpLabel, otpInput, verifyBtn);
        scheduleCard.getChildren().addAll(activeTitle, details, otpBox);

        getChildren().addAll(header, zoneCard, scheduleCard);
    }
}
