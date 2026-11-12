package com.campusbazaar.server;

import com.campusbazaar.controller.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * High-performance Java REST API Dispatcher.
 * Routes HTTP endpoints to dedicated Java domain controllers.
 * Enforces CORS headers and handles GET/POST/OPTIONS requests.
 */
public class ApiDispatcherHandler implements HttpHandler {

    private final ListingController listingController;
    private final PricingController pricingController;
    private final RentalController rentalController;
    private final NegotiationController negotiationController;
    private final HandoverController handoverController;
    private final ReceiptController receiptController;
    private final AuthController authController;

    public ApiDispatcherHandler(
            ListingController listingController,
            PricingController pricingController,
            RentalController rentalController,
            NegotiationController negotiationController,
            HandoverController handoverController,
            ReceiptController receiptController,
            AuthController authController) {
        this.listingController = listingController;
        this.pricingController = pricingController;
        this.rentalController = rentalController;
        this.negotiationController = negotiationController;
        this.handoverController = handoverController;
        this.receiptController = receiptController;
        this.authController = authController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set standard CORS headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

        String method = exchange.getRequestMethod().toUpperCase();
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String rawPath = exchange.getRequestURI().getPath();
        String path = (rawPath != null) ? rawPath.replaceAll("/+$", "") : "";
        String rawQuery = exchange.getRequestURI().getRawQuery();
        Map<String, String> queryParams = parseQueryParams(rawQuery);
        String requestBody = readBody(exchange.getRequestBody());

        String jsonResponse;
        int statusCode = 200;

        try {
            if ("/api/status".equalsIgnoreCase(path)) {
                jsonResponse = "{\"status\":\"ONLINE\",\"app\":\"CampusBazaar\",\"engine\":\"Core Java REST Backend\",\"version\":\"1.0.0\"}";
            } else if ("/api/auth/me".equalsIgnoreCase(path)) {
                jsonResponse = authController.handleGetCurrentUser();
            } else if ("/api/auth/switch-persona".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = authController.handleSwitchPersona(requestBody);
            } else if ("/api/listings".equalsIgnoreCase(path)) {
                if ("POST".equals(method)) {
                    jsonResponse = listingController.handleCreateListing(requestBody);
                } else {
                    jsonResponse = listingController.handleGetListings(queryParams);
                }
            } else if (path.startsWith("/api/listings/")) {
                String idPart = path.substring("/api/listings/".length());
                try {
                    int id = Integer.parseInt(idPart);
                    jsonResponse = listingController.handleGetListingById(id);
                } catch (NumberFormatException e) {
                    jsonResponse = "{\"success\":false,\"message\":\"Invalid listing ID\"}";
                    statusCode = 400;
                }
            } else if ("/api/pricing/estimate".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = pricingController.handleEstimate(requestBody);
            } else if ("/api/rentals/calculate".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = rentalController.handleCalculate(requestBody);
            } else if ("/api/rentals".equalsIgnoreCase(path)) {
                jsonResponse = rentalController.handleGetRentals();
            } else if ("/api/negotiations".equalsIgnoreCase(path)) {
                jsonResponse = negotiationController.handleGetNegotiations();
            } else if ("/api/negotiations/counter".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = negotiationController.handleCounterOffer(requestBody);
            } else if ("/api/negotiations/accept".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = negotiationController.handleAcceptOffer(requestBody);
            } else if ("/api/handovers".equalsIgnoreCase(path)) {
                jsonResponse = handoverController.handleGetHandovers();
            } else if ("/api/handovers/verify-otp".equalsIgnoreCase(path) && "POST".equals(method)) {
                jsonResponse = handoverController.handleVerifyOtp(requestBody);
            } else if ("/api/receipts".equalsIgnoreCase(path)) {
                jsonResponse = receiptController.handleGetReceipts();
            } else {
                jsonResponse = "{\"success\":false,\"message\":\"API endpoint not found: " + path + "\"}";
                statusCode = 404;
            }
        } catch (Exception e) {
            jsonResponse = "{\"success\":false,\"message\":\"Internal Java Server Error: " + e.getMessage() + "\"}";
            statusCode = 500;
        }

        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Map<String, String> parseQueryParams(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new HashMap<>();
        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String val = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    map.put(key, val);
                } else if (!pair.isEmpty()) {
                    String key = URLDecoder.decode(pair, "UTF-8");
                    map.put(key, "");
                }
            } catch (Exception ignored) {}
        }
        return map;
    }

    private String readBody(InputStream is) throws IOException {
        if (is == null) return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toString(StandardCharsets.UTF_8.name());
    }
}
