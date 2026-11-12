package com.campusbazaar.ui.javafx.views;

import com.campusbazaar.ui.javafx.theme.ThemeManager;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Top Navigation Bar View in JavaFX.
 * Preserves the brand floral identity, + Sell Item action, Corner Search trigger,
 * 8-theme color palette, and student persona pill.
 */
public class TopNavBarView extends HBox {

    private final Runnable onSellItemClicked;
    private final Runnable onSearchClicked;
    private final Runnable onPricingClicked;
    private final Runnable onPersonaClicked;
    private final Parent rootContainer;

    private Label userNameLabel;
    private Label userRoleLabel;

    public TopNavBarView(Parent rootContainer,
                         Runnable onSellItemClicked,
                         Runnable onSearchClicked,
                         Runnable onPricingClicked,
                         Runnable onPersonaClicked) {
        this.rootContainer = rootContainer;
        this.onSellItemClicked = onSellItemClicked;
        this.onSearchClicked = onSearchClicked;
        this.onPricingClicked = onPricingClicked;
        this.onPersonaClicked = onPersonaClicked;

        initializeUI();
    }

    private void initializeUI() {
        getStyleClass().add("top-navbar");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(16);

        // 1. Brand Section (Logo + Title + Subtitle)
        HBox brandBox = new HBox(10);
        brandBox.getStyleClass().add("brand-box");
        brandBox.setAlignment(Pos.CENTER_LEFT);

        Label logoIcon = new Label("🌸");
        logoIcon.getStyleClass().add("brand-logo-icon");

        VBox titleBox = new VBox(1);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox titleRow = new HBox(3);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label titlePart1 = new Label("Campus");
        titlePart1.getStyleClass().add("brand-title");
        Label titlePart2 = new Label("Bazaar");
        titlePart2.getStyleClass().addAll("brand-title", "brand-accent");
        Label flowerLabel = new Label(" 🌸");
        titleRow.getChildren().addAll(titlePart1, titlePart2, flowerLabel);

        Label subtitleLabel = new Label("PEER-TO-PEER MARKETPLACE");
        subtitleLabel.getStyleClass().add("brand-subtitle");

        titleBox.getChildren().addAll(titleRow, subtitleLabel);
        brandBox.getChildren().addAll(logoIcon, titleBox);

        // Spacer to push right cluster
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 2. Right Actions Cluster
        HBox rightCluster = new HBox(12);
        rightCluster.setAlignment(Pos.CENTER_RIGHT);

        // A. + Sell Item Button (Emerald Gradient)
        Button sellItemBtn = new Button("🏷️ + Sell Item");
        sellItemBtn.getStyleClass().add("btn-sell-action");
        sellItemBtn.setTooltip(new Tooltip("Add an item to sell on campus"));
        sellItemBtn.setOnAction(e -> {
            if (onSellItemClicked != null) onSellItemClicked.run();
        });

        // B. Corner Search Button
        Button searchBtn = new Button("🔍 Find Item");
        searchBtn.getStyleClass().add("corner-search-icon-btn");
        searchBtn.setTooltip(new Tooltip("Find any item you need (books, calculator, cycle...)"));
        searchBtn.setOnAction(e -> {
            if (onSearchClicked != null) onSearchClicked.run();
        });

        // C. Smart Pricing Button
        Button pricingBtn = new Button("💡 Smart Pricing");
        pricingBtn.getStyleClass().add("nav-action-btn");
        pricingBtn.setTooltip(new Tooltip("Calculate rule-based fair campus valuation"));
        pricingBtn.setOnAction(e -> {
            if (onPricingClicked != null) onPricingClicked.run();
        });

        // D. Theme Palette Selector (8 Dots + Reset)
        HBox themeSelector = createThemeSelector();

        // E. Student Persona Pill
        HBox personaPill = createPersonaPill();

        rightCluster.getChildren().addAll(sellItemBtn, searchBtn, pricingBtn, themeSelector, personaPill);

        getChildren().addAll(brandBox, spacer, rightCluster);
    }

    private HBox createThemeSelector() {
        HBox box = new HBox(5);
        box.getStyleClass().add("theme-selector-wrap");
        box.setAlignment(Pos.CENTER);

        Label paletteIcon = new Label("🎨");
        paletteIcon.setStyle("-fx-font-size: 11px;");
        box.getChildren().add(paletteIcon);

        for (ThemeManager.Theme theme : ThemeManager.Theme.values()) {
            Button dot = new Button();
            dot.getStyleClass().add("theme-dot");
            dot.setStyle("-fx-background-color: " + theme.getPrimaryHex() + ";");
            dot.setTooltip(new Tooltip(theme.getDisplayName()));
            dot.setOnAction(e -> ThemeManager.applyTheme(rootContainer, theme));
            box.getChildren().add(dot);
        }

        Button resetBtn = new Button("↺");
        resetBtn.getStyleClass().add("theme-reset-btn");
        resetBtn.setTooltip(new Tooltip("Reset to Campus Classic theme"));
        resetBtn.setOnAction(e -> ThemeManager.resetTheme(rootContainer));
        box.getChildren().add(resetBtn);

        return box;
    }

    private HBox createPersonaPill() {
        HBox pill = new HBox(8);
        pill.getStyleClass().add("user-persona-pill");
        pill.setAlignment(Pos.CENTER_LEFT);
        Tooltip.install(pill, new Tooltip("Click to switch student persona"));

        Label avatar = new Label("A");
        avatar.getStyleClass().add("user-persona-avatar");

        VBox infoBox = new VBox(1);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        userNameLabel = new Label("Aarav Patel");
        userNameLabel.setStyle("-fx-font-weight: 800; -fx-font-size: 11px;");
        userRoleLabel = new Label("🎓 Student Buyer");
        userRoleLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: -cb-text-muted; -fx-font-weight: 600;");
        infoBox.getChildren().addAll(userNameLabel, userRoleLabel);

        Label switchIcon = new Label("⇄");
        switchIcon.setStyle("-fx-font-weight: 800; -fx-font-size: 11px; -fx-text-fill: -cb-primary;");

        pill.getChildren().addAll(avatar, infoBox, switchIcon);
        pill.setOnMouseClicked(e -> {
            if (onPersonaClicked != null) onPersonaClicked.run();
        });

        return pill;
    }

    public void updatePersona(String name, String role, String avatarLetter) {
        if (userNameLabel != null) userNameLabel.setText(name);
        if (userRoleLabel != null) userRoleLabel.setText(role);
    }
}
