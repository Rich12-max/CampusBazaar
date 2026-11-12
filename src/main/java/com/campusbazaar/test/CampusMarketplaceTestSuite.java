package com.campusbazaar.test;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.InvalidRentalDurationException;
import com.campusbazaar.exception.ValidationException;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.*;
import com.campusbazaar.service.*;
import com.campusbazaar.util.PasswordHasher;
import com.campusbazaar.util.ValidationUtil;

import java.time.LocalDate;

/**
 * Automated Academic Verification Test Suite.
 * Validates core OOP requirements, pricing calculations, rental penalties,
 * validation edge cases, and transaction state machines.
 */
public class CampusMarketplaceTestSuite {
    private static int passedCount = 0;
    private static int failedCount = 0;

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("  🎓 CAMPUSBAZAAR: AUTOMATED TEST SUITE & ACADEMIC OOP VERIFICATION     ");
        System.out.println("=========================================================================\n");

        testPolymorphicPricing();
        testRentalAndPenaltyCalculations();
        testSmartPricingEngine();
        testCryptographicPasswordHashing();
        testValidationAndEdgeCases();
        testPurchaseAndHandoverLifecycle();
        testBarterModule();
        testCampusHandoverWithOTP();
        testPriceNegotiationEngine();
        testDigitalClearanceReceipt();

        System.out.println("\n-------------------------------------------------------------------------");
        System.out.println("  TEST EXECUTION SUMMARY: " + passedCount + " PASSED | " + failedCount + " FAILED");
        if (failedCount == 0) {
            System.out.println("  🏆 ALL VERIFICATION SUITES COMPLETED WITH 100% SUCCESS!");
        } else {
            System.out.println("  ⚠️ SOME TESTS FAILED. PLEASE REVIEW STACK TRACE.");
        }
        System.out.println("=========================================================================");
    }

    private static void assertTrue(String testId, String scenario, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testId + ": " + scenario);
            passedCount++;
        } else {
            System.err.println("  [FAIL] " + testId + ": " + scenario);
            failedCount++;
        }
    }

    // --- TEST 1: POLYMORPHISM ---
    private static void testPolymorphicPricing() {
        System.out.println("▶ SUITE 1: Object-Oriented Polymorphism & Dynamic Dispatch");
        Student seller = (Student) DataStore.getInstance().getUserById(1);
        Product p1 = new Product(1, 1, 1, "Java Programming", "Textbook", ItemCondition.LIKE_NEW, "Pearson", 800, "");

        Listing saleItem = new SaleListing(1001, p1, seller, ListingStatus.AVAILABLE, 450.0, 800.0, true);
        Listing rentItem = new RentalListing(1002, p1, seller, ListingStatus.AVAILABLE, 35.0, 300.0, 1, 14, 20.0);

        // Polymorphic invocation of getEffectivePrice()
        assertTrue("TC-01.1", "SaleListing getEffectivePrice() resolves to outright selling price (450.0)",
                saleItem.getEffectivePrice() == 450.0);
        assertTrue("TC-01.2", "RentalListing getEffectivePrice() resolves to daily rental rate (35.0)",
                rentItem.getEffectivePrice() == 35.0);
        assertTrue("TC-01.3", "Runtime Polymorphism works via abstract base reference",
                saleItem.getEffectivePrice() != rentItem.getEffectivePrice());
    }

    // --- TEST 2: RENTALS & PENALTY ENGINE ---
    private static void testRentalAndPenaltyCalculations() {
        System.out.println("\n▶ SUITE 2: Product Rental & Late Penalty Calculation Engine");
        Student seller = (Student) DataStore.getInstance().getUserById(2);
        Product p = new Product(2, 2, 2, "Casio FX Calculator", "Calc", ItemCondition.BRAND_NEW, "Casio", 1600, "");
        RentalListing rental = new RentalListing(1003, p, seller, ListingStatus.AVAILABLE, 40.0, 500.0, 2, 14, 25.0);

        // Normal duration cost: 5 days * 40/day = 200.0
        double cost = rental.calculateRentalCost(5);
        assertTrue("TC-02.1", "Standard rental calculation (5 days @ 40/day = 200.0)", cost == 200.0);

        // Overdue penalty: 3 days overdue * 25/day = 75.0
        double penalty = rental.calculateLatePenalty(3);
        assertTrue("TC-02.2", "Late penalty calculation (3 days overdue @ 25/day = 75.0)", penalty == 75.0);

        // On-time return (0 overdue days): 0 penalty
        double zeroPenalty = rental.calculateLatePenalty(0);
        assertTrue("TC-02.3", "On-time return produces zero late penalty", zeroPenalty == 0.0);
    }

    // --- TEST 3: SMART PRICING ENGINE ---
    private static void testSmartPricingEngine() {
        System.out.println("\n▶ SUITE 3: Rule-Based Smart Campus Pricing Engine");
        // Category 1: Academic Books (60% retention), Condition: Like New (85%)
        // Base = 1000 * 0.60 * 0.85 = 510.0
        PriceSuggestionEngine.PriceSuggestion sug = PriceSuggestionEngine.calculatePrice(1, 1000.0, ItemCondition.LIKE_NEW, null);

        assertTrue("TC-03.1", "Fair target computed within expected bounds (around 510)",
                Math.abs(sug.getRecommendedFair() - 510.0) < 5.0);
        assertTrue("TC-03.2", "Min suggested price is lower than fair target",
                sug.getSuggestedMin() < sug.getRecommendedFair());
        assertTrue("TC-03.3", "Max suggested price is higher than fair target",
                sug.getSuggestedMax() > sug.getRecommendedFair());
    }

    // --- TEST 4: CRYPTOGRAPHY ---
    private static void testCryptographicPasswordHashing() {
        System.out.println("\n▶ SUITE 4: Security & Cryptographic Password Hashing");
        String raw = "StudentPass@2026";
        String hash = PasswordHasher.hash(raw);

        assertTrue("TC-04.1", "Password hash is 64 hex characters (SHA-256)", hash.length() == 64);
        assertTrue("TC-04.2", "Password verification succeeds for correct secret", PasswordHasher.verify(raw, hash));
        assertTrue("TC-04.3", "Password verification fails for incorrect secret", !PasswordHasher.verify("WrongPass", hash));
    }

    // --- TEST 5: VALIDATIONS & EDGE CASES ---
    private static void testValidationAndEdgeCases() {
        System.out.println("\n▶ SUITE 5: Validation & Edge Case Handling");

        // Email validation test
        boolean emailCaught = false;
        try {
            ValidationUtil.validateEmail("invalid-email-format");
        } catch (ValidationException e) {
            emailCaught = true;
        }
        assertTrue("TC-05.1", "Rejects malformed email string without domain", emailCaught);

        // Negative price test
        boolean priceCaught = false;
        try {
            ValidationUtil.validatePositive(-50.0, "Selling Price");
        } catch (ValidationException e) {
            priceCaught = true;
        }
        assertTrue("TC-05.2", "Rejects negative product price", priceCaught);

        // Inverted rental date window test
        boolean dateCaught = false;
        try {
            RentalService rs = new RentalService();
            Student borrower = (Student) DataStore.getInstance().getUserById(1);
            RentalListing listing = (RentalListing) DataStore.getInstance().getListingById(202);
            rs.requestRental(listing, borrower, LocalDate.now().plusDays(5), LocalDate.now().plusDays(2)); // start > end
        } catch (InvalidRentalDurationException e) {
            dateCaught = true;
        }
        assertTrue("TC-05.3", "Rejects inverted rental date window (start date after expected return date)", dateCaught);
    }

    // --- TEST 6: PURCHASE & HANDOVER STATE MACHINE ---
    private static void testPurchaseAndHandoverLifecycle() {
        System.out.println("\n▶ SUITE 6: End-to-End Purchase & Handover State Machine");
        PurchaseService ps = new PurchaseService();
        Student buyer = (Student) DataStore.getInstance().getUserById(2); // Priya
        Listing listing = DataStore.getInstance().getListingById(201);    // Aarav's Maths book

        // 1. Submit Buy Request
        PurchaseRequest req = ps.submitPurchaseRequest(listing, buyer, 410.0, "Central Library Steps", "Ready with UPI");
        assertTrue("TC-06.1", "Purchase request initialized with PENDING status", req.getStatus() == RequestStatus.PENDING);

        // 2. Seller Accepts
        ps.acceptRequest(req.getRequestId());
        assertTrue("TC-06.2", "Purchase request transitions to ACCEPTED status", req.getStatus() == RequestStatus.ACCEPTED);
        assertTrue("TC-06.3", "Listing status transitions to RESERVED upon seller acceptance", listing.getStatus() == ListingStatus.RESERVED);

        // 3. Handover Completed
        PurchaseTransaction txn = ps.completeHandover(req.getRequestId(), PaymentMethod.UPI_SIMULATED);
        assertTrue("TC-06.4", "Purchase request transitions to COMPLETED status", req.getStatus() == RequestStatus.COMPLETED);
        assertTrue("TC-06.5", "Listing status transitions to SOLD", listing.getStatus() == ListingStatus.SOLD);
        assertTrue("TC-06.6", "Immutable transaction audit record created with unique code", txn != null && txn.getTransactionCode().startsWith("BUY-TXN"));
    }

    // --- TEST 7: BARTER & SWAP MODULE ---
    private static void testBarterModule() {
        System.out.println("\n▶ SUITE 7: Peer-to-Peer Barter & Book Swap Module");
        BarterService bs = new BarterService();
        Student s1 = (Student) DataStore.getInstance().getUserById(1);
        Student s2 = (Student) DataStore.getInstance().getUserById(2);

        // 1. Polymorphic Barter listing
        BarterListing bl = (BarterListing) DataStore.getInstance().getListingById(207);
        assertTrue("TC-07.1", "BarterListing correctly instantiated with type BARTER", bl != null && bl.getListingType() == ListingType.BARTER);
        assertTrue("TC-07.2", "Barter pricing display shows swap item requirement", bl.getPricingDisplay().contains("SWAP"));

        // 2. Submit proposal
        try {
            BarterProposal bp = bs.proposeSwap(bl.getListingId(), s2, "Operating Systems Concepts", ItemCondition.LIKE_NEW, "Galvin textbook", 0.0);
            assertTrue("TC-07.3", "Swap proposal initialized with PENDING status", bp.getStatus() == ProposalStatus.PENDING);

            // 3. Accept proposal
            bs.acceptProposal(bp.getProposalId(), s1.getUserId());
            assertTrue("TC-07.4", "Barter proposal transitions to ACCEPTED status", bp.getStatus() == ProposalStatus.ACCEPTED);
            assertTrue("TC-07.5", "Barter listing transitions to RESERVED upon acceptance", bl.getStatus() == ListingStatus.RESERVED);
        } catch (Exception e) {
            assertTrue("TC-07.X", "Unexpected exception in Barter suite: " + e.getMessage(), false);
        }
    }

    // --- TEST 8: CAMPUS SAFE HANDOVER & OTP ---
    private static void testCampusHandoverWithOTP() {
        System.out.println("\n▶ SUITE 8: Safe Campus Meetup & 6-Digit OTP Verification");
        HandoverService hs = new HandoverService();
        Student buyer = (Student) DataStore.getInstance().getUserById(2);
        Student seller = (Student) DataStore.getInstance().getUserById(1);

        try {
            // 1. Schedule appointment
            HandoverSchedule schedule = hs.scheduleHandover(204, buyer, seller,
                    "Student Activity Center (SAC) Main Desk", "Tomorrow, 4:00 PM", "Near the notice board");
            assertTrue("TC-08.1", "Handover appointment created in SCHEDULED state", schedule.getStatus() == HandoverStatus.SCHEDULED);
            assertTrue("TC-08.2", "Generates 6-digit cryptographic verification PIN", schedule.getVerificationPin() != null && schedule.getVerificationPin().length() == 6);

            // 2. Invalid PIN rejection
            boolean wrongPinRejected = !hs.verifyAndCompleteHandover(schedule.getScheduleId(), seller.getUserId(), "000000");
            assertTrue("TC-08.3", "Rejects incorrect verification PIN without completing handover", wrongPinRejected);

            // 3. Valid PIN verification & completion
            boolean correctPinAccepted = hs.verifyAndCompleteHandover(schedule.getScheduleId(), seller.getUserId(), schedule.getVerificationPin());
            assertTrue("TC-08.4", "Accepts matching 6-digit OTP and verifies completion", correctPinAccepted);
            assertTrue("TC-08.5", "Schedule transitions to VERIFIED_COMPLETED status", schedule.getStatus() == HandoverStatus.VERIFIED_COMPLETED);
        } catch (Exception e) {
            assertTrue("TC-08.X", "Unexpected exception in Handover suite: " + e.getMessage(), false);
        }
    }

    // --- TEST 9: PRICE BARGAINING & COUNTER-OFFER ENGINE ---
    private static void testPriceNegotiationEngine() {
        System.out.println("\n▶ SUITE 9: Price Bargaining & Counter-Offer Engine");
        NegotiationService ns = new NegotiationService();
        Student buyer = (Student) DataStore.getInstance().getUserById(3); // Rohan
        Student seller = (Student) DataStore.getInstance().getUserById(2); // Priya

        try {
            // 1. Buyer offers ₹450 on ₹550 kettle (Listing 205)
            PriceNegotiation pn = ns.startNegotiation(205, buyer, 450.0, "Can buy today if ₹450");
            assertTrue("TC-09.1", "Negotiation starts in PENDING_SELLER status (Round 1)", pn.getStatus() == NegotiationStatus.PENDING_SELLER && pn.getRoundNumber() == 1);

            // 2. Seller counters at ₹500 (Round 2)
            ns.sellerCounter(pn.getNegotiationId(), seller.getUserId(), 500.0, "Lowest I can do is ₹500");
            assertTrue("TC-09.2", "Seller counter-offer increments round to 2 and status to COUNTERED_BY_SELLER",
                    pn.getRoundNumber() == 2 && pn.getStatus() == NegotiationStatus.COUNTERED_BY_SELLER);

            // 3. Buyer accepts seller counter
            ns.buyerAcceptCounter(pn.getNegotiationId(), buyer.getUserId());
            assertTrue("TC-09.3", "Negotiation successfully transitions to ACCEPTED on agreed price",
                    pn.getStatus() == NegotiationStatus.ACCEPTED && pn.getCurrentOfferPrice() == 500.0);
        } catch (Exception e) {
            assertTrue("TC-09.X", "Unexpected exception in Negotiation suite: " + e.getMessage(), false);
        }
    }

    // --- TEST 10: DIGITAL CLEARANCE CERTIFICATE & RECEIPT ---
    private static void testDigitalClearanceReceipt() {
        System.out.println("\n▶ SUITE 10: Official Campus Clearance Receipt & Integrity Signature");
        ReceiptService rs = new ReceiptService();

        ClearanceReceipt cr = rs.getReceiptByNumber("CB-REC-2026-0042");
        assertTrue("TC-10.1", "Clearance receipt successfully retrieved by receipt number", cr != null);
        assertTrue("TC-10.2", "Computes SHA-256 digital integrity verification signature", cr.getVerificationHash() != null && cr.getVerificationHash().length() >= 16);
        assertTrue("TC-10.3", "Formatted printable clearance slip contains official issuing authority", cr.toFormattedTextSlip().contains("CampusBazaar Student Affairs"));
    }
}


