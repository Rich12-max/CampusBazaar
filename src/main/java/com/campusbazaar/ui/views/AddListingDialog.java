package com.campusbazaar.ui.views;

import com.campusbazaar.model.Student;
import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.model.enums.ListingType;
import com.campusbazaar.service.ListingService;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddListingDialog extends JDialog {
    private final ListingService listingService = new ListingService();
    private final Student currentSeller;
    private final Runnable onSuccessCallback;

    private JComboBox<String> typeCombo;
    private JComboBox<String> categoryCombo;
    private JTextField titleField;
    private JTextArea descArea;
    private JComboBox<ItemCondition> conditionCombo;
    private JTextField priceField;
    private JTextField origPriceField;
    private JTextField depositField;
    private JLabel priceLabel;
    private JLabel depositLabel;

    public AddListingDialog(Frame parent, Student currentSeller, Runnable onSuccessCallback) {
        super(parent, "Post a New Campus Listing 🌸", true);
        this.currentSeller = currentSeller;
        this.onSuccessCallback = onSuccessCallback;

        setSize(480, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel titleLabel = new JLabel("Create Campus Listing ✨");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_MAIN);
        content.add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 12));
        form.setOpaque(false);

        form.add(createLabel("Listing Type:"));
        typeCombo = new JComboBox<>(new String[]{"Outright Sale 🏷️", "Campus Rental (Daily) ✨"});
        typeCombo.addActionListener(e -> toggleRentalFields());
        form.add(typeCombo);

        form.add(createLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{
                "1. Academic Books 📚",
                "2. Calculators & Tech 💻",
                "3. Lab Equipment 🔬",
                "4. Hostel Essentials 🛏️",
                "5. Cycles & Sports 🚲",
                "6. Stationery & Notes 📝"
        });
        form.add(categoryCombo);

        form.add(createLabel("Item Title:"));
        titleField = new JTextField();
        form.add(titleField);

        form.add(createLabel("Description:"));
        descArea = new JTextArea(2, 15);
        descArea.setLineWrap(true);
        form.add(new JScrollPane(descArea));

        form.add(createLabel("Item Condition:"));
        conditionCombo = new JComboBox<>(ItemCondition.values());
        conditionCombo.setSelectedItem(ItemCondition.LIKE_NEW);
        form.add(conditionCombo);

        priceLabel = createLabel("Selling Price (₹):");
        form.add(priceLabel);
        priceField = new JTextField("350");
        form.add(priceField);

        depositLabel = createLabel("Original Price (₹):");
        form.add(depositLabel);
        origPriceField = new JTextField("800");
        form.add(origPriceField);

        content.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        RoundedButton submitBtn = new RoundedButton("Publish Listing 🚀", Theme.PRIMARY, Theme.PRIMARY_DARK);
        submitBtn.addActionListener(e -> submitForm());

        btnRow.add(cancelBtn);
        btnRow.add(submitBtn);
        content.add(btnRow, BorderLayout.SOUTH);

        add(content);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BODY_BOLD);
        l.setForeground(Theme.TEXT_MAIN);
        return l;
    }

    private void toggleRentalFields() {
        boolean isSale = typeCombo.getSelectedIndex() == 0;
        if (isSale) {
            priceLabel.setText("Selling Price (₹):");
            depositLabel.setText("Original Price (₹):");
            priceField.setText("350");
            origPriceField.setText("800");
        } else {
            priceLabel.setText("Daily Rent Rate (₹):");
            depositLabel.setText("Security Deposit (₹):");
            priceField.setText("40");
            origPriceField.setText("400");
        }
    }

    private void submitForm() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title for the product.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int catId = categoryCombo.getSelectedIndex() + 1;
            ItemCondition cond = (ItemCondition) conditionCombo.getSelectedItem();
            boolean isSale = typeCombo.getSelectedIndex() == 0;

            if (isSale) {
                double sellPrice = Double.parseDouble(priceField.getText().trim());
                double origPrice = Double.parseDouble(origPriceField.getText().trim());
                listingService.createSaleListing(currentSeller, catId, title, desc, cond, "Campus Brand", origPrice, sellPrice, true, "");
            } else {
                double dailyRate = Double.parseDouble(priceField.getText().trim());
                double deposit = Double.parseDouble(origPriceField.getText().trim());
                listingService.createRentalListing(currentSeller, catId, title, desc, cond, "Campus Brand", deposit * 2, dailyRate, deposit, 1, 14, 25.0, "");
            }

            JOptionPane.showMessageDialog(this, "Listing published successfully! 🌸", "Success", JOptionPane.INFORMATION_MESSAGE);
            if (onSuccessCallback != null) onSuccessCallback.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for prices.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

