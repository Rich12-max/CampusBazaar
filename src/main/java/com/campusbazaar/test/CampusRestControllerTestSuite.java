package com.campusbazaar.test;

import com.campusbazaar.controller.*;
import com.campusbazaar.util.JsonUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Automated Verification Suite for CampusBazaar Java REST Controllers & DTOs.
 * Validates request routing, business logic execution, and JSON payload contracts.
 */
public class CampusRestControllerTestSuite {

    private static int passedCount = 0;
    private static int failedCount = 0;

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("  🎓 CAMPUSBAZAAR: REST CONTROLLERS & JAVA BACKEND VERIFICATION        ");
        System.out.println("=========================================================================\n");

        testListingController();
        testPricingController();
        testRentalController();
        testNegotiationController();
        testHandoverController();
        testReceiptController();
        testAuthController();

        System.out.println("\n-------------------------------------------------------------------------");
        System.out.printf("  REST CONTROLLER TEST SUMMARY: %d PASSED | %d FAILED\n", passedCount, failedCount);
        if (failedCount == 0) {
            System.out.println("  ✨ ALL REST ARCHITECTURE TESTS COMPLETED WITH 100% SUCCESS!");
        } else {
            System.err.println("  ❌ SOME TESTS FAILED.");
            System.exit(1);
        }
        System.out.println("=========================================================================\n");
    }

    private static void testListingController() {
        System.out.println("🔹 SUITE 1: Listing Controller & Catalogue Operations");
        ListingController controller = new ListingController();

        // 1. Get all listings
        String allJson = controller.handleGetListings(Collections.emptyMap());
        assertCondition(allJson.contains("\"success\":true"), "TC-R1.1: ListingController returns HTTP 200 OK");
        assertCondition(allJson.contains("Higher Engineering Mathematics"), "TC-R1.2: ListingController includes seeded product catalog");

        // 2. Query filter
        Map<String, String> params = new HashMap<>();
        params.put("query", "Casio");
        String queryJson = controller.handleGetListings(params);
        assertCondition(queryJson.contains("Casio FX-991EX"), "TC-R1.3: Search filtering accurately isolates matching items");

        // 3. Create listing
        String newListingJson = "{\"title\":\"Digital Signal Processing Notes\",\"price\":250.0,\"categoryId\":1,\"listingType\":\"SALE\",\"condition\":\"LIKE_NEW\"}";
        String createResp = controller.handleCreateListing(newListingJson);
        assertCondition(createResp.contains("\"success\":true"), "TC-R1.4: Creates new marketplace listing via Java backend");
        assertCondition(createResp.contains("Digital Signal Processing Notes"), "TC-R1.5: Persists listing title and returns serialized DTO");
    }

    private static void testPricingController() {
        System.out.println("\n🔹 SUITE 2: Smart Pricing Engine Controller");
        PricingController controller = new PricingController();

        String payload = "{\"originalPrice\":1200.0,\"categoryId\":1,\"condition\":\"LIKE_NEW\"}";
        String response = controller.handleEstimate(payload);

        assertCondition(response.contains("\"success\":true"), "TC-R2.1: PricingController handles valuation request successfully");
        assertCondition(response.contains("recommendedFair"), "TC-R2.2: Generates recommended fair price");
        assertCondition(response.contains("suggestedMin") && response.contains("suggestedMax"), "TC-R2.3: Returns boundary pricing range");
    }

    private static void testRentalController() {
        System.out.println("\n🔹 SUITE 3: Rental & Escrow Deposit Controller");
        RentalController controller = new RentalController();

        // 1. Rental calculation
        String calcPayload = "{\"days\":7,\"dailyRate\":40.0,\"deposit\":500.0,\"lateFeePerDay\":25.0}";
        String calcResp = controller.handleCalculate(calcPayload);
        assertCondition(calcResp.contains("\"success\":true"), "TC-R3.1: Rental calculation succeeds");
        assertCondition(calcResp.contains("\"baseRent\":280.0"), "TC-R3.2: Base rent matches 7 * 40.0 = 280.0");
        assertCondition(calcResp.contains("\"grandTotal\":780.0"), "TC-R3.3: Grand total includes refundable security deposit (280 + 500 = 780.0)");

        // 2. Get active rentals
        String rentalsResp = controller.handleGetRentals();
        assertCondition(rentalsResp.contains("\"success\":true"), "TC-R3.4: Retrieves active campus micro-rentals");
    }

    private static void testNegotiationController() {
        System.out.println("\n🔹 SUITE 4: Multi-Round Price Negotiation Controller");
        NegotiationController controller = new NegotiationController();

        // 1. Get negotiations
        String listResp = controller.handleGetNegotiations();
        assertCondition(listResp.contains("\"success\":true"), "TC-R4.1: Retrieves active negotiation threads");

        int negId = com.campusbazaar.database.DataStore.getInstance().getPriceNegotiations().get(0).getNegotiationId();

        // 2. Counter-offer
        String counterPayload = "{\"negotiationId\":" + negId + ",\"counterPrice\":3100.0}";
        String counterResp = controller.handleCounterOffer(counterPayload);
        assertCondition(counterResp.contains("\"success\":true"), "TC-R4.2: Enforces counter-offer round transition in Java");

        // 3. Accept offer
        String acceptPayload = "{\"negotiationId\":" + negId + "}";
        String acceptResp = controller.handleAcceptOffer(acceptPayload);
        assertCondition(acceptResp.contains("\"success\":true"), "TC-R4.3: Accepts negotiated offer successfully");
    }

    private static void testHandoverController() {
        System.out.println("\n🔹 SUITE 5: Safe Campus Handover & OTP Controller");
        HandoverController controller = new HandoverController();

        // 1. Get handovers
        String handoversResp = controller.handleGetHandovers();
        assertCondition(handoversResp.contains("\"success\":true"), "TC-R5.1: Retrieves scheduled campus exchange appointments");

        // 2. Verify invalid OTP
        String invalidOtpPayload = "{\"scheduleId\":1,\"otp\":\"000000\"}";
        String invalidResp = controller.handleVerifyOtp(invalidOtpPayload);
        assertCondition(invalidResp.contains("\"success\":false"), "TC-R5.2: Rejects incorrect verification PIN");
    }

    private static void testReceiptController() {
        System.out.println("\n🔹 SUITE 6: Digital Clearance Receipt Controller");
        ReceiptController controller = new ReceiptController();

        String receiptsResp = controller.handleGetReceipts();
        assertCondition(receiptsResp.contains("\"success\":true"), "TC-R6.1: Retrieves official clearance receipts");
        assertCondition(receiptsResp.contains("verificationHash"), "TC-R6.2: Clearance vouchers contain cryptographic digital signature");
    }

    private static void testAuthController() {
        System.out.println("\n🔹 SUITE 7: Student Authentication & Persona Controller");
        AuthController controller = new AuthController();

        // 1. Get me
        String meResp = controller.handleGetCurrentUser();
        assertCondition(meResp.contains("\"success\":true"), "TC-R7.1: Returns active authenticated session");
        assertCondition(meResp.contains("Aarav Patel"), "TC-R7.2: Default persona is Student Aarav Patel");

        // 2. Switch persona to Admin
        String switchPayload = "{\"role\":\"ADMIN\"}";
        String switchResp = controller.handleSwitchPersona(switchPayload);
        assertCondition(switchResp.contains("\"success\":true"), "TC-R7.3: Switches active role to Campus Administrator");
        assertCondition(switchResp.contains("Mehta"), "TC-R7.4: Role switch reflects new identity");
    }

    private static void assertCondition(boolean condition, String testName) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passedCount++;
        } else {
            System.err.println("  [FAIL] " + testName);
            failedCount++;
        }
    }
}
