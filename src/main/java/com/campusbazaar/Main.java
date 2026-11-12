package com.campusbazaar;

import com.campusbazaar.database.DBConnectionManager;
import com.campusbazaar.database.DataStore;
import com.campusbazaar.server.CampusHttpServer;
import com.campusbazaar.ui.MainSwingFrame;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

/**
 * Main Unified Entry Point for Campus Peer-to-Peer Marketplace.
 * Launches the Localhost Web Server (http://localhost:8080)
 * and optionally opens the modern Java Swing Desktop GUI window.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing CampusBazaar System (Gold Edition)...");

        // Initialize DataStore
        DataStore.getInstance();

        // Check MySQL connectivity status
        boolean dbConnected = DBConnectionManager.getInstance().testConnection();
        if (dbConnected) {
            System.out.println("✅ Connected to MySQL Database (campus_bazaar_db)");
        } else {
            System.out.println("ℹ️ MySQL service not detected on localhost:3306. Utilizing built-in high-performance DataStore (Collections Framework) for immediate evaluation.");
        }

        // Start Localhost Web Server
        int port = 8080;
        try {
            CampusHttpServer server = new CampusHttpServer(port);
            server.start();
        } catch (Exception e) {
            System.err.println("❌ Error starting web server: " + e.getMessage());
        }

        // Launch Java Swing Desktop GUI if desktop graphical display is supported
        boolean launchGui = args.length > 0 && args[0].equalsIgnoreCase("--gui");
        if (launchGui && !GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    MainSwingFrame frame = new MainSwingFrame();
                    frame.setVisible(true);
                } catch (Exception ex) {
                    System.out.println("Swing GUI failed to initialize: " + ex.getMessage());
                }
            });
        }
    }
}

