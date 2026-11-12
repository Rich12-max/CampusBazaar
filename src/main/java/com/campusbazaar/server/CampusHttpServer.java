package com.campusbazaar.server;

import com.campusbazaar.controller.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * Built-In Zero-Dependency Embedded HTTP Server using standard JDK HttpServer.
 * Serves the modern aesthetic campus marketplace frontend on http://localhost:8080
 */
public class CampusHttpServer {
    private static final int DEFAULT_PORT = 8080;
    private HttpServer server;
    private int port;

    public CampusHttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        int targetPort = this.port;
        boolean started = false;

        // Try targetPort, then fallback to next if port is already bound
        for (int p = targetPort; p < targetPort + 10; p++) {
            try {
                server = HttpServer.create(new InetSocketAddress(p), 0);
                this.port = p;
                started = true;
                break;
            } catch (IOException e) {
                // Try next port
            }
        }

        if (!started) {
            throw new IOException("Could not bind HttpServer on ports " + targetPort + " through " + (targetPort + 10));
        }

        // Register controllers and API dispatcher
        ListingController listingController = new ListingController();
        PricingController pricingController = new PricingController();
        RentalController rentalController = new RentalController();
        NegotiationController negotiationController = new NegotiationController();
        HandoverController handoverController = new HandoverController();
        ReceiptController receiptController = new ReceiptController();
        AuthController authController = new AuthController();

        ApiDispatcherHandler apiDispatcher = new ApiDispatcherHandler(
                listingController,
                pricingController,
                rentalController,
                negotiationController,
                handoverController,
                receiptController,
                authController
        );

        // Register handlers
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api", apiDispatcher);

        server.setExecutor(null); // default executor
        server.start();

        printStartupBanner(this.port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("CampusBazaar Server stopped.");
        }
    }

    private void printStartupBanner(int port) {
        System.out.println("\n" +
                "=========================================================================\n" +
                "  🎓 CAMPUS PEER-TO-PEER MARKETPLACE (CampusBazaar)                     \n" +
                "  End-Semester Project - Object-Oriented Programming Using Java          \n" +
                "=========================================================================\n" +
                "  🟢 Server Status: ACTIVE & RUNNING ON LOCALHOST                       \n" +
                "  🌐 Localhost URL: http://localhost:" + port + "                       \n" +
                "  🌸 Aesthetic UI: Modern Pastel Theme, Cute Micro-Interactions,        \n" +
                "                   Rule-Based Smart Pricing & Rental Calculator Engine  \n" +
                "  📂 Mode: Core Java (JDK Standard Library - Zero Framework Overkill)    \n" +
                "=========================================================================\n" +
                "  👉 Open your web browser and navigate to: http://localhost:" + port + " \n" +
                "=========================================================================\n");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            // Look for web resource
            File file = new File("src/main/resources/web" + path);
            if (!file.exists()) {
                file = new File("web" + path);
            }

            if (file.exists() && !file.isDirectory()) {
                String contentType = "text/html; charset=UTF-8";
                if (path.endsWith(".css")) contentType = "text/css";
                else if (path.endsWith(".js")) contentType = "application/javascript";
                else if (path.endsWith(".png")) contentType = "image/png";
                else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) contentType = "image/jpeg";

                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());

                try (OutputStream os = exchange.getResponseBody();
                     FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                }
            } else {
                String notFound = "<h1>404 Resource Not Found</h1><p>CampusBazaar could not find: " + path + "</p>";
                byte[] bytes = notFound.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(404, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        }
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"status\":\"ONLINE\",\"app\":\"CampusBazaar\",\"version\":\"1.0.0\"}";
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        try {
            CampusHttpServer server = new CampusHttpServer(port);
            server.start();
        } catch (Exception e) {
            System.err.println("Failed to start CampusBazaar server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

