package com.campusbazaar.ui.views;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Report;
import com.campusbazaar.service.AdminService;
import com.campusbazaar.service.ReportService;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.components.RoundedPanel;
import com.campusbazaar.ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminView extends JPanel {
    private final AdminService adminService = new AdminService();
    private final ReportService reportService = new ReportService();
    private final DataStore dataStore = DataStore.getInstance();
    private JPanel reportsPanel;
    private JLabel totalListingsLabel;
    private JLabel totalStudentsLabel;
    private JLabel pendingReportsLabel;

    public AdminView() {
        setLayout(new BorderLayout(0, 18));
        setBackground(Theme.BACKGROUND);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initHeader();
        initReports();
        refresh();
    }

    private void initHeader() {
        JPanel top = new JPanel(new BorderLayout(0, 14));
        top.setOpaque(false);

        JLabel title = new JLabel("Campus Moderator & Safety Oversight Center 🛡️");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_MAIN);
        top.add(title, BorderLayout.NORTH);

        // Stats Cards
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 14, 0));
        statsRow.setOpaque(false);

        totalListingsLabel = new JLabel("6");
        totalStudentsLabel = new JLabel("4");
        pendingReportsLabel = new JLabel("1");

        statsRow.add(createStatBox("TOTAL ACTIVE LISTINGS", totalListingsLabel, Theme.PRIMARY));
        statsRow.add(createStatBox("REGISTERED STUDENTS", totalStudentsLabel, Theme.MINT));
        statsRow.add(createStatBox("PENDING SAFETY REPORTS", pendingReportsLabel, Color.RED));

        top.add(statsRow, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
    }

    private JPanel createStatBox(String title, JLabel valLabel, Color col) {
        RoundedPanel p = new RoundedPanel(16, Color.WHITE, Theme.BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel(title);
        t.setFont(Theme.FONT_BADGE);
        t.setForeground(Theme.TEXT_MUTED);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLabel.setForeground(col);

        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(valLabel);
        return p;
    }

    private void initReports() {
        reportsPanel = new JPanel();
        reportsPanel.setLayout(new BoxLayout(reportsPanel, BoxLayout.Y_AXIS));
        reportsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(reportsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        totalListingsLabel.setText(String.valueOf(dataStore.getListings().size()));
        totalStudentsLabel.setText(String.valueOf(dataStore.getAllUsers().size()));
        totalReportsCount();

        reportsPanel.removeAll();
        List<Report> reps = reportService.getAllReports();

        if (reps.isEmpty()) {
            JLabel empty = new JLabel("All clear! No pending fraud reports or complaints. ✨");
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_MUTED);
            reportsPanel.add(empty);
        } else {
            for (Report r : reps) {
                RoundedPanel p = new RoundedPanel(16, Color.WHITE, Theme.BORDER);
                p.setLayout(new BorderLayout(14, 10));
                p.setBorder(new EmptyBorder(14, 18, 14, 18));
                p.setMaximumSize(new Dimension(800, 95));

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                info.setOpaque(false);

                JLabel reason = new JLabel("🚨 " + r.getReason());
                reason.setFont(Theme.FONT_BODY_BOLD);
                reason.setForeground(Color.RED);

                JLabel desc = new JLabel("Reported Item: " + r.getReportedListingTitle() + " | By: " + r.getReporterName() + " — \"" + r.getDescription() + "\"");
                desc.setFont(Theme.FONT_SMALL);
                desc.setForeground(Theme.TEXT_MUTED);

                info.add(reason);
                info.add(Box.createVerticalStrut(4));
                info.add(desc);

                p.add(info, BorderLayout.CENTER);

                JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                actions.setOpaque(false);

                JButton dismissBtn = new JButton("Dismiss");
                dismissBtn.addActionListener(e -> {
                    reportService.resolveReport(r.getReportId(), "Dismissed by admin");
                    refresh();
                });

                RoundedButton delistBtn = new RoundedButton("Delist Item", Color.RED, Color.RED.darker());
                delistBtn.addActionListener(e -> {
                    adminService.delistListing(r.getReportedListingId());
                    reportService.resolveReport(r.getReportId(), "Item delisted due to complaint");
                    JOptionPane.showMessageDialog(this, "Item successfully delisted from campus marketplace.", "Admin Action", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                });

                actions.add(dismissBtn);
                actions.add(delistBtn);
                p.add(actions, BorderLayout.EAST);

                reportsPanel.add(p);
                reportsPanel.add(Box.createVerticalStrut(10));
            }
        }

        reportsPanel.revalidate();
        reportsPanel.repaint();
    }

    private void totalReportsCount() {
        long count = dataStore.getReports().stream().filter(r -> r.getStatus() == com.campusbazaar.model.enums.ReportStatus.OPEN).count();
        pendingReportsLabel.setText(String.valueOf(count));
    }
}

