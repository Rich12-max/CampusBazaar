package com.campusbazaar.ui.components;

import com.campusbazaar.model.Listing;
import com.campusbazaar.model.RentalListing;
import com.campusbazaar.model.SaleListing;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Aesthetic Product Card Component for Swing UI.
 */
public class ProductCardPanel extends RoundedPanel {
    private final Listing listing;
    private final Runnable onActionCallback;

    public ProductCardPanel(Listing listing, Runnable onActionCallback) {
        super(18, Color.WHITE, Theme.BORDER);
        this.listing = listing;
        this.onActionCallback = onActionCallback;

        setLayout(new BorderLayout(0, 8));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setPreferredSize(new Dimension(260, 240));

        initUI();
    }

    private void initUI() {
        // Top Header: Type badge & Status badge
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        boolean isSale = listing.getListingType() == ListingType.SALE;
        BadgePill typePill = isSale 
                ? new BadgePill("🌸 SALE", Theme.MINT_LIGHT, Theme.MINT)
                : new BadgePill("✨ RENT", Theme.PINK_LIGHT, Theme.PINK);

        BadgePill statusPill = new BadgePill("🟢 " + listing.getStatus(), Theme.PRIMARY_LIGHT, Theme.PRIMARY);

        topRow.add(typePill, BorderLayout.WEST);
        topRow.add(statusPill, BorderLayout.EAST);
        add(topRow, BorderLayout.NORTH);

        // Center: Item info
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("<html><b>" + listing.getProduct().getTitle() + "</b></html>");
        titleLabel.setFont(Theme.FONT_BODY_BOLD);
        titleLabel.setForeground(Theme.TEXT_MAIN);

        JLabel sellerLabel = new JLabel("🎓 " + listing.getSeller().getFullName() + " (⭐ " + String.format("%.1f", listing.getSeller().getTrustRating()) + ")");
        sellerLabel.setFont(Theme.FONT_SMALL);
        sellerLabel.setForeground(Theme.TEXT_MUTED);

        JLabel descLabel = new JLabel("<html><p style='width:180px;'>" + listing.getProduct().getDescription() + "</p></html>");
        descLabel.setFont(Theme.FONT_SMALL);
        descLabel.setForeground(Theme.TEXT_MUTED);

        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(sellerLabel);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(descLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Price & Button
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        JLabel priceLabel = new JLabel(listing.getPricingDisplay());
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(Theme.PRIMARY);

        RoundedButton actionBtn = new RoundedButton(isSale ? "Buy" : "Rent", Theme.PRIMARY, Theme.PRIMARY_DARK);
        actionBtn.setPreferredSize(new Dimension(80, 28));
        actionBtn.addActionListener(e -> {
            if (onActionCallback != null) onActionCallback.run();
        });

        bottomRow.add(priceLabel, BorderLayout.WEST);
        bottomRow.add(actionBtn, BorderLayout.EAST);

        add(bottomRow, BorderLayout.SOUTH);
    }
}

