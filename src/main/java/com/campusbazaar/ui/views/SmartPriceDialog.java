package com.campusbazaar.ui.views;

import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.service.PriceSuggestionEngine;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.components.RoundedPanel;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SmartPriceDialog extends JDialog {
    private JComboBox<String> categoryCombo;
    private JTextField origPriceField;
    private JComboBox<ItemCondition> conditionCombo;
    private JLabel rangeResultLabel;
    private JLabel fairResultLabel;
    private JLabel reasonLabel;

    public SmartPriceDialog(Frame parent) {
        super(parent, "Rule-Based Smart Price Engine 💡", true);
        setSize(460, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        initUI();
        calculatePrice();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Smart Campus Price Calculator 💡");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_MAIN);
        content.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 14));
        form.setOpaque(false);

        form.add(createLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{
                "1. Books (60% retention) 📚",
                "2. Tech & Calculators (50%) 💻",
                "3. Lab Gear (65%) 🔬",
                "4. Hostel Living (45%) 🛏️",
                "5. Cycles & Sports (55%) 🚲",
                "6. Stationery (40%) 📝"
        });
        categoryCombo.addActionListener(e -> calculatePrice());
        form.add(categoryCombo);

        form.add(createLabel("Original Price (₹):"));
        origPriceField = new JTextField("1000");
        origPriceField.addActionListener(e -> calculatePrice());
        form.add(origPriceField);

        form.add(createLabel("Condition Grade:"));
        conditionCombo = new JComboBox<>(ItemCondition.values());
        conditionCombo.setSelectedItem(ItemCondition.LIKE_NEW);
        conditionCombo.addActionListener(e -> calculatePrice());
        form.add(conditionCombo);

        content.add(form, BorderLayout.CENTER);

        // Results Card
        RoundedPanel resultBox = new RoundedPanel(18, Theme.GOLD_LIGHT, Theme.BORDER);
        resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.Y_AXIS));
        resultBox.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel header = new JLabel("RECOMMENDED FAIR CAMPUS VALUATION:");
        header.setFont(Theme.FONT_SMALL);
        header.setForeground(Theme.TEXT_MUTED);

        rangeResultLabel = new JLabel("₹435 - ₹585");
        rangeResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        rangeResultLabel.setForeground(Theme.PRIMARY);

        fairResultLabel = new JLabel("Fair Target: ₹510");
        fairResultLabel.setFont(Theme.FONT_BODY_BOLD);
        fairResultLabel.setForeground(Theme.MINT);

        reasonLabel = new JLabel("<html><p style='width:340px;'>Calculated based on category baseline and condition wear.</p></html>");
        reasonLabel.setFont(Theme.FONT_SMALL);
        reasonLabel.setForeground(Theme.TEXT_MUTED);

        resultBox.add(header);
        resultBox.add(Box.createVerticalStrut(4));
        resultBox.add(rangeResultLabel);
        resultBox.add(Box.createVerticalStrut(4));
        resultBox.add(fairResultLabel);
        resultBox.add(Box.createVerticalStrut(8));
        resultBox.add(reasonLabel);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.add(resultBox, BorderLayout.NORTH);

        RoundedButton calcBtn = new RoundedButton("Recalculate Price ✨", Theme.PRIMARY, Theme.PRIMARY_DARK);
        calcBtn.addActionListener(e -> calculatePrice());
        bottom.add(calcBtn, BorderLayout.SOUTH);

        content.add(bottom, BorderLayout.SOUTH);
        add(content);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BODY_BOLD);
        l.setForeground(Theme.TEXT_MAIN);
        return l;
    }

    private void calculatePrice() {
        try {
            int catId = categoryCombo.getSelectedIndex() + 1;
            double orig = Double.parseDouble(origPriceField.getText().trim());
            ItemCondition cond = (ItemCondition) conditionCombo.getSelectedItem();

            PriceSuggestionEngine.PriceSuggestion sug = PriceSuggestionEngine.calculatePrice(catId, orig, cond, null);
            rangeResultLabel.setText("₹" + (int)sug.getSuggestedMin() + " - ₹" + (int)sug.getSuggestedMax());
            fairResultLabel.setText("Fair Target: ₹" + (int)sug.getRecommendedFair());
            reasonLabel.setText("<html><p style='width:340px;'>" + sug.getReasoning() + "</p></html>");
        } catch (Exception ex) {
            rangeResultLabel.setText("Invalid Input");
        }
    }
}

