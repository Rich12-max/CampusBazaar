package com.campusbazaar.ui.javafx.theme;

import javafx.scene.Parent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Aesthetic Theme Manager for CampusBazaar JavaFX Application.
 * Manages runtime swapping of 8 university color palettes matching the web design tokens.
 */
public class ThemeManager {

    public enum Theme {
        CAMPUS("campus", "Campus Classic (Royal Blue)", "#2563EB", "#FFFFFF"),
        SLATE("slate", "Slate Dark (Sky Cyan)", "#38BDF8", "#111827"),
        MATCHA("matcha", "Matcha Mint (Emerald)", "#10B981", "#FFFFFF"),
        SAKURA("sakura", "Sakura Blossom (Rose)", "#F43F5E", "#FFFFFF"),
        LAVENDER("lavender", "Royal Lavender (Violet)", "#A855F7", "#18132B"),
        HONEY("honey", "Honey Amber (Sunset)", "#D97706", "#FFFFFF"),
        GOLD("gold", "Imperial Gold (Luxe Amber)", "#D4AF37", "#1C1810"),
        MIDNIGHT("midnight", "Neon Midnight (Cyberpunk)", "#8B5CF6", "#12111F");

        private final String id;
        private final String displayName;
        private final String primaryHex;
        private final String cardBgHex;

        Theme(String id, String displayName, String primaryHex, String cardBgHex) {
            this.id = id;
            this.displayName = displayName;
            this.primaryHex = primaryHex;
            this.cardBgHex = cardBgHex;
        }

        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getPrimaryHex() { return primaryHex; }
        public String getCardBgHex() { return cardBgHex; }
        public String getStyleClass() { return "theme-" + id; }
    }

    private static Theme currentTheme = Theme.CAMPUS;
    private static final List<Consumer<Theme>> listeners = new ArrayList<>();

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void applyTheme(Parent root, Theme theme) {
        if (root == null) return;
        currentTheme = theme;

        // Remove all previous theme classes
        for (Theme t : Theme.values()) {
            root.getStyleClass().remove(t.getStyleClass());
        }

        // Apply new theme class
        root.getStyleClass().add(theme.getStyleClass());

        // Notify listeners
        for (Consumer<Theme> listener : listeners) {
            listener.accept(theme);
        }
    }

    public static void resetTheme(Parent root) {
        applyTheme(root, Theme.CAMPUS);
    }

    public static void addThemeListener(Consumer<Theme> listener) {
        listeners.add(listener);
    }
}
