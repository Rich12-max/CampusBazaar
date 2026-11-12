package com.campusbazaar.ui.javafx.dialogs;

import com.campusbazaar.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import java.util.function.Consumer;

/**
 * Product Details Modal Dialog in JavaFX.
 * Displays high-resolution item views, verified seller credentials, and action triggers.
 */
public class ProductDetailsModal extends Dialog<Void> {

    public ProductDetailsModal(Listing item, Consumer<Listing> onActionTriggered) {
        Product prod = item.getProduct();
        String itemTitle = (prod != null) ? prod.getTitle() : "Campus Item";

        setTitle("Campus Item Details • " + itemTitle);
        setHeaderText(null);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        pane.setStyle("-fx-background-color: -cb-card-bg; -fx-padding: 16px;");

        ButtonType actionBtnType = new ButtonType(
            item instanceof RentalListing ? "Rent This Item" : (item instanceof BarterListing ? "Propose Swap" : "Request to Buy"),
            ButtonBar.ButtonData.OK_DONE
        );
        pane.getButtonTypes().addAll(actionBtnType, ButtonType.CLOSE);

        initializeUI(pane, item, prod, onActionTriggered, actionBtnType);
    }

    private void initializeUI(DialogPane pane, Listing item, Product prod, Consumer<Listing> onActionTriggered, ButtonType actionBtnType) {
        VBox container = new VBox(12);
        container.setPrefWidth(500);

        String itemTitle = (prod != null) ? prod.getTitle() : "Campus Item";
        String imgUrl = (prod != null && prod.getImagePath() != null && !prod.getImagePath().trim().isEmpty())
                ? prod.getImagePath()
                : "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&q=80&w=600";

        // Image
        ImageView imgView = new ImageView();
        imgView.setFitWidth(470);
        imgView.setFitHeight(220);
        imgView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(470, 220);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imgView.setClip(clip);

        try {
            imgView.setImage(new Image(imgUrl, 470, 220, false, true, true));
        } catch (Exception ignored) {}

        // Title & Price
        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(itemTitle);
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");
        HBox.setHgrow(title, Priority.ALWAYS);

        Label price = new Label(item.getPricingDisplay());
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: -cb-primary;");

        titleRow.getChildren().addAll(title, price);

        // Tags Row
        HBox tagsRow = new HBox(8);
        tagsRow.setAlignment(Pos.CENTER_LEFT);

        Label typeTag = new Label(item.getListingType().name());
        typeTag.getStyleClass().add("hero-chip");
        typeTag.setStyle("-fx-background-color: -cb-primary-light; -fx-text-fill: -cb-primary; -fx-font-weight: bold;");

        String condStr = (prod != null && prod.getCondition() != null) ? prod.getCondition().name().replace("_", " ") : "GOOD";
        Label condTag = new Label(condStr);
        condTag.getStyleClass().add("badge-condition");

        tagsRow.getChildren().addAll(typeTag, condTag);

        // Description
        String descStr = (prod != null && prod.getDescription() != null) ? prod.getDescription() : "No detailed description provided.";
        Label desc = new Label(descStr);
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 12px; -fx-line-spacing: 3px; -fx-text-fill: -cb-text;");

        // Verified Seller Information Box
        VBox sellerBox = new VBox(6);
        sellerBox.setStyle("-fx-background-color: -cb-primary-light; -fx-border-color: -cb-border; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-padding: 12px;");

        Label sellerHeader = new Label("🎓 Verified Student Seller Information");
        sellerHeader.setStyle("-fx-font-weight: 800; -fx-font-size: 12px; -fx-text-fill: -cb-primary;");

        Student s = item.getSeller();
        String sellerText = (s != null)
            ? String.format("Name: %s\nRoll Number: %s\nDepartment: %s\nInstitution: College Campus (Verified)",
                s.getFullName(), s.getStudentIdNumber(), s.getDepartment())
            : "Seller: Verified Campus Student";

        Label sellerDetails = new Label(sellerText);
        sellerDetails.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text;");

        sellerBox.getChildren().addAll(sellerHeader, sellerDetails);

        container.getChildren().addAll(imgView, titleRow, tagsRow, desc, sellerBox);
        pane.setContent(container);

        setResultConverter(btn -> {
            if (btn == actionBtnType && onActionTriggered != null) {
                onActionTriggered.accept(item);
            }
            return null;
        });
    }
}
