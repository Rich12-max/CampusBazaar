package com.campusbazaar.ui.javafx.views;

import com.campusbazaar.model.RentalListing;
import com.campusbazaar.service.RentalService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Dedicated Campus Rentals & Calculator View in JavaFX.
 * Demonstrates daily micro-rental calculation, deposit escrow, and late penalty tracking.
 */
public class RentalsView extends VBox {

    private final RentalService rentalService;
    private final List<RentalListing> rentalListings;

    private Slider durationSlider;
    private Label durationValueLabel;
    private Label rentTotalLabel;
    private Label depositLabel;
    private Label grandTotalLabel;
    private FlowPane rentalsGrid;

    public RentalsView(RentalService rentalService, List<RentalListing> rentalListings) {
        this.rentalService = rentalService;
        this.rentalListings = rentalListings;
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(20);
        setPadding(new Insets(10));

        // 1. Header
        Label header = new Label("⏳ Campus Micro-Rentals & Security Escrow Hub");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        // 2. Interactive Rental Calculator Card
        VBox calcCard = new VBox(12);
        calcCard.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 18px; -fx-background-radius: 18px; -fx-padding: 20px;");

        Label calcTitle = new Label("🧮 Interactive Daily Rental & Escrow Estimator");
        calcTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: -cb-primary;");

        HBox sliderRow = new HBox(12);
        sliderRow.setAlignment(Pos.CENTER_LEFT);
        Label sliderLabel = new Label("Select Rental Duration:");
        sliderLabel.setStyle("-fx-font-weight: bold;");

        durationSlider = new Slider(1, 30, 7);
        durationSlider.setShowTickMarks(true);
        durationSlider.setShowTickLabels(true);
        durationSlider.setMajorTickUnit(5);
        durationSlider.setBlockIncrement(1);
        HBox.setHgrow(durationSlider, Priority.ALWAYS);

        durationValueLabel = new Label("7 Days");
        durationValueLabel.setStyle("-fx-font-weight: 800; -fx-text-fill: -cb-primary; -fx-font-size: 13px;");

        sliderRow.getChildren().addAll(sliderLabel, durationSlider, durationValueLabel);

        // Price Breakdown
        HBox breakdownBox = new HBox(20);
        breakdownBox.setStyle("-fx-background-color: -cb-primary-light; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-padding: 14px;");

        VBox c1 = new VBox(2, new Label("Base Rental Rate:"), (rentTotalLabel = new Label("₹175 (₹25/day × 7 days)")));
        VBox c2 = new VBox(2, new Label("Refundable Escrow Deposit:"), (depositLabel = new Label("₹100")));
        VBox c3 = new VBox(2, new Label("Total Upfront Payment:"), (grandTotalLabel = new Label("₹275")));

        c1.getChildren().get(1).setStyle("-fx-font-weight: 800; -fx-text-fill: -cb-text;");
        c2.getChildren().get(1).setStyle("-fx-font-weight: 800; -fx-text-fill: #10B981;");
        c3.getChildren().get(1).setStyle("-fx-font-weight: 800; -fx-text-fill: -cb-primary; -fx-font-size: 14px;");

        breakdownBox.getChildren().addAll(c1, c2, c3);

        durationSlider.valueProperty().addListener((obs, oldV, newV) -> {
            int days = newV.intValue();
            durationValueLabel.setText(days + " Days");
            double baseRent = 25.0 * days;
            double deposit = 100.0;
            rentTotalLabel.setText(String.format("₹%.0f (₹25/day × %d days)", baseRent, days));
            depositLabel.setText(String.format("₹%.0f", deposit));
            grandTotalLabel.setText(String.format("₹%.0f", baseRent + deposit));
        });

        calcCard.getChildren().addAll(calcTitle, sliderRow, breakdownBox);

        // 3. Available Rental Items
        Label itemsTitle = new Label("Available For Rent on Campus");
        itemsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        rentalsGrid = new FlowPane(16, 16);
        renderRentalCards();

        getChildren().addAll(header, calcCard, itemsTitle, rentalsGrid);
    }

    private void renderRentalCards() {
        rentalsGrid.getChildren().clear();
        for (RentalListing rental : rentalListings) {
            VBox card = new VBox(8);
            card.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 14px;");
            card.setPrefWidth(260);

            String itemTitle = (rental.getProduct() != null) ? rental.getProduct().getTitle() : "Rental Item";
            Label title = new Label(itemTitle);
            title.setStyle("-fx-font-weight: 800; -fx-font-size: 13px; -fx-text-fill: -cb-text;");

            Label rate = new Label(String.format("₹%.0f/day • Deposit: ₹%.0f", rental.getDailyRentalPrice(), rental.getSecurityDeposit()));
            rate.setStyle("-fx-font-weight: 800; -fx-text-fill: -cb-primary;");

            Label duration = new Label(String.format("Duration: %d to %d days", rental.getMinRentalDays(), rental.getMaxRentalDays()));
            duration.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");

            Button rentBtn = new Button("Rent Now");
            rentBtn.getStyleClass().add("btn-card-action");
            rentBtn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rental booking requested for: " + itemTitle);
                alert.showAndWait();
            });

            card.getChildren().addAll(title, rate, duration, rentBtn);
            rentalsGrid.getChildren().add(card);
        }
    }
}
