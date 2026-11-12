package com.campusbazaar.ui;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.User;
import com.campusbazaar.ui.components.RoundedButton;
import com.campusbazaar.ui.components.RoundedPanel;
import com.campusbazaar.ui.theme.Theme;
import com.campusbazaar.ui.views.AddListingDialog;
import com.campusbazaar.ui.views.AdminView;
import com.campusbazaar.ui.views.MarketplaceView;
import com.campusbazaar.ui.views.RentalsView;
import com.campusbazaar.ui.views.SmartPriceDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;

/**
 * Main Primary Java Swing Window for CampusBazaar.
 * Connects the MVC architecture into a modern, aesthetic desktop interface.
 */
public class MainSwingFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);

    private MarketplaceView marketplaceView;
    private RentalsView rentalsView;
    private AdminView adminView;

    private JLabel userStatusLabel;
    private Student currentStudent;

    public MainSwingFrame() {
        super("Campus Peer-to-Peer Marketplace (CampusBazaar) 👑");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        // Default to student persona Aarav Patel (User #1)
        User u = DataStore.getInstance().getUserById(1);
        if (u instanceof Student) {
            this.currentStudent = (Student) u;
        }

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BACKGROUND);

        // 1. Sleek Navigation Sidebar
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // 2. Top Header Bar
        JPanel topBar = createTopBar();
        root.add(topBar, BorderLayout.NORTH);

        // 3. Central Content Area (CardLayout)
        marketplaceView = new MarketplaceView();
        rentalsView = new RentalsView();
        adminView = new AdminView();

        contentCards.add(marketplaceView, "MARKETPLACE");
        contentCards.add(rentalsView, "RENTALS");
        contentCards.add(adminView, "ADMIN");

        root.add(contentCards, BorderLayout.CENTER);
        add(root);
    }

    private JPanel createSidebar() {
        RoundedPanel side = new RoundedPanel(0, Color.WHITE, Theme.BORDER);
        side.setPreferredSize(new Dimension(230, 0));
        side.setLayout(new BorderLayout());
        side.setBorder(new EmptyBorder(22, 16, 22, 16));

        // Navigation Menu
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);

        menu.add(createNavButton("🛍️ Browse Catalog", () -> {
            cardLayout.show(contentCards, "MARKETPLACE");
            marketplaceView.refreshGrid();
        }));
        menu.add(Box.createVerticalStrut(10));

        menu.add(createNavButton("➕ Post Listing", () -> {
            AddListingDialog dlg = new AddListingDialog(this, currentStudent, () -> {
                marketplaceView.refreshGrid();
            });
            dlg.setVisible(true);
        }));
        menu.add(Box.createVerticalStrut(10));

        menu.add(createNavButton("💡 Smart Pricing", () -> {
            SmartPriceDialog dlg = new SmartPriceDialog(this);
            dlg.setVisible(true);
        }));
        menu.add(Box.createVerticalStrut(10));

        menu.add(createNavButton("⏳ Active Rentals", () -> {
            cardLayout.show(contentCards, "RENTALS");
            rentalsView.refreshList();
        }));
        menu.add(Box.createVerticalStrut(10));

        menu.add(createNavButton("🛡️ Moderation Center", () -> {
            cardLayout.show(contentCards, "ADMIN");
            adminView.refresh();
        }));

        side.add(menu, BorderLayout.NORTH);

        // Bottom Web Portal Button
        RoundedButton webBtn = new RoundedButton("🌐 Open Web App", Theme.GOLD, Theme.GOLD_DARK);
        webBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Server running at http://localhost:8080", "Web URL", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        side.add(webBtn, BorderLayout.SOUTH);

        return side;
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));

        // Brand
        JLabel brand = new JLabel("CampusBazaar 👑 [Gold Edition]");
        brand.setFont(Theme.FONT_TITLE);
        brand.setForeground(Theme.GOLD_DARK);

        // User Persona Status
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userPanel.setOpaque(false);

        userStatusLabel = new JLabel("🎓 " + currentStudent.getFullName() + " (Verified Student)");
        userStatusLabel.setFont(Theme.FONT_BODY_BOLD);
        userStatusLabel.setForeground(Theme.TEXT_MAIN);

        RoundedButton switchBtn = new RoundedButton("Switch Persona ⇄", Theme.GOLD_LIGHT, Theme.BORDER, Theme.GOLD_DARK, 14);
        switchBtn.addActionListener(e -> {
            String[] options = {"Aarav Patel (Student Buyer)", "Priya Sharma (Student Seller)", "Prof. Mehta (Administrator)"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Select a simulated campus persona:", "Switch Demo Persona",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                currentStudent = (Student) DataStore.getInstance().getUserById(1);
                userStatusLabel.setText("🎓 Aarav Patel (Student Buyer)");
            } else if (choice == 1) {
                currentStudent = (Student) DataStore.getInstance().getUserById(2);
                userStatusLabel.setText("🌸 Priya Sharma (Student Seller)");
            } else if (choice == 2) {
                userStatusLabel.setText("🛡️ Prof. Mehta (Dean / Administrator)");
            }
        });

        userPanel.add(userStatusLabel);
        userPanel.add(switchBtn);

        bar.add(brand, BorderLayout.WEST);
        bar.add(userPanel, BorderLayout.EAST);
        return bar;
    }

    private JButton createNavButton(String title, Runnable action) {
        JButton btn = new JButton(title);
        btn.setFont(Theme.FONT_BODY_BOLD);
        btn.setForeground(Theme.TEXT_MAIN);
        btn.setBackground(Theme.BACKGROUND);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainSwingFrame frame = new MainSwingFrame();
            frame.setVisible(true);
        });
    }
}

