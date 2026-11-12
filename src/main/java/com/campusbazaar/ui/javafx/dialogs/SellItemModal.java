package com.campusbazaar.ui.javafx.dialogs;

import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ListingType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

/**
 * Sell Item Modal Dialog in JavaFX.
 * Allows students to post outright sales, daily micro-rentals, or peer item exchanges.
 */
public class SellItemModal extends Dialog<Listing> {

    private TextField titleField;
    private TextArea descField;
    private ComboBox<String> categoryCombo;
    private ComboBox<ListingType> typeCombo;
    private ComboBox<ItemCondition> conditionCombo;
    private TextField priceField;
    private TextField rateField;
    private TextField depositField;
    private TextField imageField;
    private TextField exchangeForField;

    private HBox salePriceRow;
    private HBox rentalRatesRow;
    private HBox barterRow;

    public SellItemModal(Student seller, Consumer<Listing> onListingCreated) {
        setTitle("🏷️ Add Item to Sell or Rent on Campus");
        setHeaderText(null);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        pane.setStyle("-fx-background-color: -cb-card-bg; -fx-padding: 16px;");

        ButtonType postBtnType = new ButtonType("Publish Listing", ButtonBar.ButtonData.OK_DONE);
        pane.getButtonTypes().addAll(postBtnType, ButtonType.CANCEL);

        initializeUI(pane, seller, onListingCreated, postBtnType);
    }

    private void initializeUI(DialogPane pane, Student seller, Consumer<Listing> onListingCreated, ButtonType postBtnType) {
        VBox container = new VBox(12);
        container.setPrefWidth(480);

        Label headerTitle = new Label("🏷️ Post a Campus Resource");
        headerTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));

        // Listing Type
        Label typeLbl = new Label("Listing Type:");
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(ListingType.SALE, ListingType.RENTAL, ListingType.BARTER);
        typeCombo.setValue(ListingType.SALE);
        grid.add(typeLbl, 0, 0);
        grid.add(typeCombo, 1, 0);

        // Title
        Label titleLbl = new Label("Item Title:");
        titleField = new TextField();
        titleField.setPromptText("e.g. Casio fx-991EX Scientific Calculator");
        grid.add(titleLbl, 0, 1);
        grid.add(titleField, 1, 1);

        // Category
        Label catLbl = new Label("Category:");
        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(
            "Academic Textbooks", "Electronics & Calculators", "Lab & Drawing Tools",
            "Hostel Essentials", "Cycles & Mobility"
        );
        categoryCombo.setValue("Academic Textbooks");
        grid.add(catLbl, 0, 2);
        grid.add(categoryCombo, 1, 2);

        // Condition
        Label condLbl = new Label("Condition:");
        conditionCombo = new ComboBox<>();
        conditionCombo.getItems().addAll(ItemCondition.values());
        conditionCombo.setValue(ItemCondition.LIKE_NEW);
        grid.add(condLbl, 0, 3);
        grid.add(conditionCombo, 1, 3);

        // Pricing Rows
        Label priceLbl = new Label("Selling Price (₹):");
        priceField = new TextField("350");
        salePriceRow = new HBox(8, priceLbl, priceField);
        salePriceRow.setAlignment(Pos.CENTER_LEFT);
        grid.add(salePriceRow, 0, 4, 2, 1);

        Label rateLbl = new Label("Daily Rate (₹):");
        rateField = new TextField("25");
        Label depLbl = new Label("Deposit (₹):");
        depositField = new TextField("100");
        rentalRatesRow = new HBox(8, rateLbl, rateField, depLbl, depositField);
        rentalRatesRow.setAlignment(Pos.CENTER_LEFT);
        rentalRatesRow.setVisible(false);
        rentalRatesRow.setManaged(false);
        grid.add(rentalRatesRow, 0, 5, 2, 1);

        Label barterLbl = new Label("Looking For:");
        exchangeForField = new TextField("Engineering Books / Tools");
        barterRow = new HBox(8, barterLbl, exchangeForField);
        barterRow.setAlignment(Pos.CENTER_LEFT);
        barterRow.setVisible(false);
        barterRow.setManaged(false);
        grid.add(barterRow, 0, 6, 2, 1);

        typeCombo.setOnAction(e -> {
            boolean isRent = typeCombo.getValue() == ListingType.RENTAL;
            boolean isSale = typeCombo.getValue() == ListingType.SALE;
            boolean isBarter = typeCombo.getValue() == ListingType.BARTER;

            salePriceRow.setVisible(isSale);
            salePriceRow.setManaged(isSale);

            rentalRatesRow.setVisible(isRent);
            rentalRatesRow.setManaged(isRent);

            barterRow.setVisible(isBarter);
            barterRow.setManaged(isBarter);
        });

        // Description
        Label descLbl = new Label("Description:");
        descField = new TextArea();
        descField.setPromptText("Mention semesters used, edition, inclusions...");
        descField.setPrefRowCount(3);
        grid.add(descLbl, 0, 7);
        grid.add(descField, 1, 7);

        // Image URL
        Label imgLbl = new Label("Image URL:");
        imageField = new TextField("https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&q=80&w=600");
        grid.add(imgLbl, 0, 8);
        grid.add(imageField, 1, 8);

        container.getChildren().addAll(headerTitle, grid);
        pane.setContent(container);

        setResultConverter(dialogButton -> {
            if (dialogButton == postBtnType) {
                String title = titleField.getText().trim();
                if (title.isEmpty()) return null;

                String desc = descField.getText().trim();
                int catId = categoryCombo.getSelectionModel().getSelectedIndex() + 1;
                ItemCondition cond = conditionCombo.getValue();
                String img = imageField.getText().trim();
                ListingType type = typeCombo.getValue();

                int newId = (int) (System.currentTimeMillis() % 100000);
                double basePrice = 350.0;
                try {
                    basePrice = Double.parseDouble(priceField.getText().trim());
                } catch (Exception ignored) {}

                Product prod = new Product(newId, (seller != null ? seller.getUserId() : 1), catId, title, desc, cond, "Campus", basePrice, img);

                Listing created;
                if (type == ListingType.RENTAL) {
                    double rate = Double.parseDouble(rateField.getText().trim());
                    double dep = Double.parseDouble(depositField.getText().trim());
                    created = new RentalListing(newId, prod, seller, ListingStatus.AVAILABLE, rate, dep, 1, 30, 15.0);
                } else if (type == ListingType.BARTER) {
                    String pref = exchangeForField.getText().trim();
                    created = new BarterListing(newId, prod, seller, ListingStatus.AVAILABLE, pref, 0.0, true);
                } else {
                    created = new SaleListing(newId, prod, seller, ListingStatus.AVAILABLE, basePrice, basePrice, true);
                }

                if (onListingCreated != null) {
                    onListingCreated.accept(created);
                }
                return created;
            }
            return null;
        });
    }
}
