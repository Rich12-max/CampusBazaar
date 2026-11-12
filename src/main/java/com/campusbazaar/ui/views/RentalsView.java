package com.campusbazaar.ui.views;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.RentalRecord;
import com.campusbazaar.model.enums.RentalStatus;
import com.campusbazaar.service.RentalService;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.components.RoundedPanel;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalsView extends JPanel {
    private final RentalService rentalService = new RentalService();
    private final DataStore dataStore = DataStore.getInstance();
    private JPanel listPanel;

    public RentalsView() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BACKGROUND);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Active & Completed Campus Rentals ⏳");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_MAIN);
        add(title, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        refreshList();
    }

    public void refreshList() {
        listPanel.removeAll();
        List<RentalRecord> records = dataStore.getRentalRecords();

        if (records.isEmpty()) {
            JLabel emptyLabel = new JLabel("No active campus rentals at the moment. 🌸");
            emptyLabel.setFont(Theme.FONT_BODY);
            emptyLabel.setForeground(Theme.TEXT_MUTED);
            listPanel.add(emptyLabel);
        } else {
            for (RentalRecord r : records) {
                RoundedPanel card = new RoundedPanel(16, Color.WHITE, Theme.BORDER);
                card.setLayout(new BorderLayout(14, 10));
                card.setBorder(new EmptyBorder(16, 18, 16, 18));
                card.setMaximumSize(new Dimension(800, 110));

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setOpaque(false);

                JLabel name = new JLabel(r.getItemTitle());
                name.setFont(Theme.FONT_HEADER);
                name.setForeground(Theme.TEXT_MAIN);

                LocalDate today = LocalDate.now();
                boolean isOverdue = today.isAfter(r.getExpectedEndDate()) && r.getStatus() == RentalStatus.ACTIVE;
                long overdueDays = isOverdue ? ChronoUnit.DAYS.between(r.getExpectedEndDate(), today) : 0;
                double penalty = overdueDays * 25.0;

                JLabel meta = new JLabel("Borrower: " + r.getBorrowerName() + " | Lender: " + r.getLenderName() + " | Rate: ₹" + (int)r.getDailyRate() + "/day");
                meta.setFont(Theme.FONT_SMALL);
                meta.setForeground(Theme.TEXT_MUTED);

                JLabel statusLabel = new JLabel();
                statusLabel.setFont(Theme.FONT_SMALL);
                if (r.getStatus() == RentalStatus.ACTIVE) {
                    if (isOverdue) {
                        statusLabel.setText("⚠️ OVERDUE by " + overdueDays + " day(s)! Late Penalty: ₹" + (int)penalty + " (deducted from deposit)");
                        statusLabel.setForeground(Color.RED);
                    } else {
                        statusLabel.setText("🟢 Active (Return by " + r.getExpectedEndDate() + ") | Deposit: ₹" + (int)r.getSecurityDeposit());
                        statusLabel.setForeground(Theme.MINT);
                    }
                } else {
                    statusLabel.setText("✅ Settled & Returned");
                    statusLabel.setForeground(Theme.TEXT_MUTED);
                }

                info.add(name);
                info.add(Box.createVerticalStrut(4));
                info.add(meta);
                info.add(Box.createVerticalStrut(4));
                info.add(statusLabel);

                card.add(info, BorderLayout.CENTER);

                if (r.getStatus() == RentalStatus.ACTIVE) {
                    RoundedButton returnBtn = new RoundedButton("Confirm Return", Theme.PRIMARY, Theme.PRIMARY_DARK);
                    returnBtn.setPreferredSize(new Dimension(140, 32));
                    returnBtn.addActionListener(e -> {
                        rentalService.processReturn(r.getRentalId(), LocalDate.now());
                        JOptionPane.showMessageDialog(this,
                                "Rental returned successfully!\nDeposit settled and listing restored to Available status. ✨",
                                "Rental Settled", JOptionPane.INFORMATION_MESSAGE);
                        refreshList();
                    });
                    card.add(returnBtn, BorderLayout.EAST);
                }

                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(12));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}

