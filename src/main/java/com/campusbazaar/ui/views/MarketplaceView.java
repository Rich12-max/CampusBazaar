package com.campusbazaar.ui.views;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Listing;
import com.campusbazaar.model.SaleListing;
import com.campusbazaar.service.ListingService;
import com.campusbazaar.ui.components.ProductCardPanel;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MarketplaceView extends JPanel {
    private final ListingService listingService = new ListingService();
    private JPanel gridPanel;
    private JTextField searchField;
    private int selectedCategory = 0;

    public MarketplaceView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BACKGROUND);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        initHeader();
        initGrid();
        refreshGrid();
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(12, 12));
        headerPanel.setOpaque(false);

        // Search Bar & Filter Row
        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setOpaque(false);

        searchField = new JTextField();
        searchField.setFont(Theme.FONT_BODY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        RoundedButton searchBtn = new RoundedButton("Search", Theme.PRIMARY, Theme.PRIMARY_DARK);
        searchBtn.addActionListener(e -> refreshGrid());

        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchBtn, BorderLayout.EAST);

        // Category Filter Chips
        JPanel chipsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipsRow.setOpaque(false);

        String[] cats = {"All", "Books 📚", "Tech 💻", "Lab 🔬", "Hostel 🛏️", "Sports 🚲"};
        for (int i = 0; i < cats.length; i++) {
            final int catId = i;
            JButton chip = new JButton(cats[i]);
            chip.setFont(Theme.FONT_SMALL);
            chip.setBackground(Color.WHITE);
            chip.setFocusPainted(false);
            chip.addActionListener(e -> {
                selectedCategory = catId;
                refreshGrid();
            });
            chipsRow.add(chip);
        }

        headerPanel.add(searchRow, BorderLayout.NORTH);
        headerPanel.add(chipsRow, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initGrid() {
        gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        gridPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshGrid() {
        gridPanel.removeAll();
        String query = searchField != null ? searchField.getText().trim() : "";
        List<Listing> list = listingService.searchAndFilter(query, selectedCategory, "ALL", null, null, "NEWEST");

        for (Listing l : list) {
            ProductCardPanel card = new ProductCardPanel(l, () -> {
                JOptionPane.showMessageDialog(this,
                        "Request initiated for: " + l.getProduct().getTitle() + "\nCampus Meeting Handover simulated successfully! 🌸",
                        "Campus Action", JOptionPane.INFORMATION_MESSAGE);
            });
            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}

