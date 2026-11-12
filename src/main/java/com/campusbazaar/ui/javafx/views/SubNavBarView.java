package com.campusbazaar.ui.javafx.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Secondary Sub-Navbar View in JavaFX.
 * Displays clean, right-aligned, uncongested category & workflow destination tabs
 * with styled counter badges and active selection indicators.
 */
public class SubNavBarView extends HBox {

    public enum TabId {
        MARKETPLACE("marketplace", "🛍️ Browse Catalogue", null),
        BARTER("barter", "🔄 Peer Swaps", null),
        RENTALS("rentals", "⏳ Campus Rentals", null),
        HANDOVERS("handovers", "📍 Safe Handovers", null),
        NEGOTIATIONS("negotiations", "💬 Price Bids", "1"),
        RECEIPTS("receipts", "📄 Clearance Receipts", null),
        WISHLIST("wishlist", "💖 Wishlist", "2"),
        ADMIN("admin", "🛡️ Moderation Center", null);

        private final String key;
        private final String label;
        private final String defaultBadge;

        TabId(String key, String label, String defaultBadge) {
            this.key = key;
            this.label = label;
            this.defaultBadge = defaultBadge;
        }

        public String getKey() { return key; }
        public String getLabel() { return label; }
        public String getDefaultBadge() { return defaultBadge; }
    }

    private final Consumer<TabId> onTabSelected;
    private final Map<TabId, Button> tabButtons = new HashMap<>();
    private final Map<TabId, Label> badgeLabels = new HashMap<>();
    private TabId activeTab = TabId.MARKETPLACE;

    public SubNavBarView(Consumer<TabId> onTabSelected) {
        this.onTabSelected = onTabSelected;
        initializeUI();
    }

    private void initializeUI() {
        getStyleClass().add("sub-navbar");
        setAlignment(Pos.CENTER_RIGHT);
        setSpacing(6);

        for (TabId tab : TabId.values()) {
            HBox btnContent = new HBox(6);
            btnContent.setAlignment(Pos.CENTER);

            Label titleLabel = new Label(tab.getLabel());
            btnContent.getChildren().add(titleLabel);

            if (tab.getDefaultBadge() != null) {
                Label badge = new Label(tab.getDefaultBadge());
                badge.getStyleClass().add("nav-badge-pill");
                badgeLabels.put(tab, badge);
                btnContent.getChildren().add(badge);
            }

            Button btn = new Button();
            btn.setGraphic(btnContent);
            btn.getStyleClass().add("nav-tab-btn");
            if (tab == activeTab) {
                btn.getStyleClass().add("active");
            }

            btn.setOnAction(e -> setActiveTab(tab));
            tabButtons.put(tab, btn);
            getChildren().add(btn);
        }
    }

    public void setActiveTab(TabId tab) {
        if (this.activeTab != null && tabButtons.containsKey(this.activeTab)) {
            tabButtons.get(this.activeTab).getStyleClass().remove("active");
        }
        this.activeTab = tab;
        if (tabButtons.containsKey(tab)) {
            tabButtons.get(tab).getStyleClass().add("active");
        }
        if (onTabSelected != null) {
            onTabSelected.accept(tab);
        }
    }

    public void updateBadge(TabId tab, int count) {
        Label badge = badgeLabels.get(tab);
        if (badge != null) {
            badge.setText(String.valueOf(count));
            badge.setVisible(count > 0);
        }
    }
}
