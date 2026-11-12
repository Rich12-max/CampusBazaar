package com.campusbazaar.database;

import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.*;
import com.campusbazaar.util.PasswordHasher;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-Safe Centralized In-Memory Data Store.
 * Backed by Java Collections Framework (ConcurrentHashMap, CopyOnWriteArrayList).
 * Ensures instant out-of-the-box local execution on http://localhost:8080
 * while preserving identical entity schemas corresponding to MySQL.
 */
public class DataStore {
    private static DataStore instance;

    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final List<Category> categories = new CopyOnWriteArrayList<>();
    private final List<Listing> listings = new CopyOnWriteArrayList<>();
    private final List<PurchaseRequest> purchaseRequests = new CopyOnWriteArrayList<>();
    private final List<RentalRecord> rentalRecords = new CopyOnWriteArrayList<>();
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();
    private final Set<String> wishlists = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final List<Review> reviews = new CopyOnWriteArrayList<>();
    private final List<Report> reports = new CopyOnWriteArrayList<>();
    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final List<BarterProposal> barterProposals = new CopyOnWriteArrayList<>();
    private final List<HandoverSchedule> handoverSchedules = new CopyOnWriteArrayList<>();
    private final List<PriceNegotiation> priceNegotiations = new CopyOnWriteArrayList<>();
    private final List<ClearanceReceipt> clearanceReceipts = new CopyOnWriteArrayList<>();

    private final AtomicInteger nextUserId = new AtomicInteger(100);
    private final AtomicInteger nextProductId = new AtomicInteger(200);
    private final AtomicInteger nextListingId = new AtomicInteger(300);
    private final AtomicInteger nextRequestId = new AtomicInteger(400);
    private final AtomicInteger nextRentalId = new AtomicInteger(500);
    private final AtomicInteger nextTransactionId = new AtomicInteger(600);
    private final AtomicInteger nextReviewId = new AtomicInteger(700);
    private final AtomicInteger nextReportId = new AtomicInteger(800);
    private final AtomicInteger nextMessageId = new AtomicInteger(900);
    private final AtomicInteger nextProposalId = new AtomicInteger(1000);
    private final AtomicInteger nextScheduleId = new AtomicInteger(1100);
    private final AtomicInteger nextNegotiationId = new AtomicInteger(1200);
    private final AtomicInteger nextReceiptId = new AtomicInteger(1300);

    private DataStore() {
        seedInitialData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedInitialData() {
        // 1. Categories
        categories.add(new Category(1, "Academic Books", "📚", "Engineering, Medical, Commerce and Humanities Textbooks"));
        categories.add(new Category(2, "Calculators & Electronics", "💻", "Scientific calculators, monitors, keyboards, chargers & adapters"));
        categories.add(new Category(3, "Lab Equipment", "🔬", "Mini drafters, engineering drawing sheets, lab coats, dissection sets"));
        categories.add(new Category(4, "Hostel Essentials", "🛏️", "Electric kettles, study lamps, table fans, bedsheets, storage organizers"));
        categories.add(new Category(5, "Cycles & Sports Gear", "🚲", "Bicycles, badminton rackets, cricket kits, yoga mats, gym dumbells"));
        categories.add(new Category(6, "Stationery & Notes", "📝", "Semester handwritten toppers notes, exam question banks, highlighters"));

        // 2. Initial Users
        String defaultHash = PasswordHasher.hash("password123");

        Student s1 = new Student(1, "Aarav Patel", "aarav@college.edu", defaultHash, "+91 9876543210",
                AccountStatus.ACTIVE, "2023-CS-042", "Computer Science", "Aryabhata Hostel, Room 304", true, 4.9, 12);
        Student s2 = new Student(2, "Priya Sharma", "priya@college.edu", defaultHash, "+91 9876543211",
                AccountStatus.ACTIVE, "2022-EE-115", "Electrical Engineering", "Gargi Hall, Room 112", true, 5.0, 18);
        Student s3 = new Student(3, "Rohan Verma", "rohan@college.edu", defaultHash, "+91 9876543212",
                AccountStatus.ACTIVE, "2024-ME-088", "Mechanical Engineering", "Kalam Hostel, Room 201", true, 4.7, 7);
        Admin admin = new Admin(4, "Prof. S. K. Mehta", "admin@college.edu", defaultHash, "+91 9876543200",
                AccountStatus.ACTIVE, "FAC-ADM-01", "Dean of Student Affairs");

        addUser(s1);
        addUser(s2);
        addUser(s3);
        addUser(admin);

        // 3. Products & Listings
        // Item 1: Higher Engineering Mathematics (Sale)
        Product p1 = new Product(101, 1, 1, "Higher Engineering Mathematics (44th Edition)",
                "Standard B.S. Grewal book for Sem 1 & Sem 2 mathematics. Completely intact, pencil markings only on key formulas.",
                ItemCondition.LIKE_NEW, "Khanna Publishers", 950.0,
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80");
        SaleListing l1 = new SaleListing(201, p1, s1, ListingStatus.AVAILABLE, 420.0, 950.0, true);
        listings.add(l1);

        // Item 2: Casio FX-991EX Scientific Calculator (Rental)
        Product p2 = new Product(102, 2, 2, "Casio FX-991EX ClassWiz Scientific Calculator",
                "Advanced scientific calculator with natural textbook display. Perfect for end-sem examination weeks and lab practicals.",
                ItemCondition.BRAND_NEW, "Casio", 1600.0,
                "https://images.unsplash.com/photo-1594980596870-8aa52a78d8cd?auto=format&fit=crop&w=600&q=80");
        RentalListing l2 = new RentalListing(202, p2, s2, ListingStatus.AVAILABLE, 40.0, 500.0, 2, 14, 25.0);
        listings.add(l2);

        // Item 3: Engineering Mini Drafter & Sheet Container (Rental)
        Product p3 = new Product(103, 3, 3, "Omega Professional Engineering Mini Drafter",
                "Stainless steel clamps with dual scale arm and expandable black sheet tube. Essential for first year Engineering Graphics.",
                ItemCondition.GOOD, "Omega", 850.0,
                "https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?auto=format&fit=crop&w=600&q=80");
        RentalListing l3 = new RentalListing(203, p3, s3, ListingStatus.AVAILABLE, 30.0, 300.0, 3, 20, 20.0);
        listings.add(l3);

        // Item 4: Hero Roadeo Mountain Bicycle 21-Speed (Sale)
        Product p4 = new Product(104, 1, 5, "Hero Roadeo 21-Speed Gear Bicycle",
                "Dual disc brakes, front shock absorber, serviced last month with new tires. Best for traveling between hostel and academic blocks.",
                ItemCondition.GOOD, "Hero", 8500.0,
                "https://images.unsplash.com/photo-1485965120184-e220f721d03e?auto=format&fit=crop&w=600&q=80");
        SaleListing l4 = new SaleListing(204, p4, s1, ListingStatus.AVAILABLE, 3400.0, 8500.0, true);
        listings.add(l4);

        // Item 5: Pigeon 1.5L Stainless Steel Electric Kettle (Sale)
        Product p5 = new Product(105, 2, 4, "Pigeon 1.5-Litre Electric Kettle for Hostel",
                "Fast boiling stainless steel body with auto shut-off. Clean and well maintained for late night instant noodles and coffee.",
                ItemCondition.LIKE_NEW, "Pigeon", 1200.0,
                "https://images.unsplash.com/photo-1544816155-12df9643f363?auto=format&fit=crop&w=600&q=80");
        SaleListing l5 = new SaleListing(205, p5, s2, ListingStatus.AVAILABLE, 550.0, 1200.0, false);
        listings.add(l5);

        // Item 6: Yonex Nanoray Badminton Racket Pair (Rental)
        Product p6 = new Product(106, 3, 5, "Yonex Carbon Graphite Badminton Rackets (Pair)",
                "Lightweight carbon graphite frame with BG65 gutting. Includes 3 Mavis 350 nylon shuttlecocks for campus court games.",
                ItemCondition.LIKE_NEW, "Yonex", 2800.0,
                "https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?auto=format&fit=crop&w=600&q=80");
        RentalListing l6 = new RentalListing(206, p6, s3, ListingStatus.AVAILABLE, 60.0, 800.0, 1, 7, 35.0);
        listings.add(l6);

        // 4. Sample Wishlists
        wishlists.add("1:202");
        wishlists.add("1:203");
        wishlists.add("2:201");

        // 5. Sample Purchase Request
        PurchaseRequest pr1 = new PurchaseRequest(nextRequestId.getAndIncrement(), 201, 2, "Priya Sharma", 400.0,
                "Central Library Steps", "Hi Aarav! Can collect today evening around 5 PM.", RequestStatus.PENDING);
        purchaseRequests.add(pr1);

        // 6. Sample Active Rental
        RentalRecord rr1 = new RentalRecord(nextRentalId.getAndIncrement(), 202, "Casio FX-991EX Scientific Calculator",
                1, "Aarav Patel", 2, "Priya Sharma", LocalDate.now().minusDays(5), LocalDate.now().plusDays(2),
                40.0, 500.0, RentalStatus.ACTIVE);
        rentalRecords.add(rr1);

        // 7. Sample Completed Transaction
        PurchaseTransaction pt1 = new PurchaseTransaction(nextTransactionId.getAndIncrement(), "TXN-2026-CAMPUS-091",
                201, "Data Structures in C++ (Tanenbaum)", 3, "Rohan Verma", 1, "Aarav Patel",
                350.0, PaymentMethod.UPI_SIMULATED, TransactionStatus.COMPLETED, "Hostel Canteen");
        transactions.add(pt1);

        // 8. Sample Reviews
        Review rev1 = new Review(nextReviewId.getAndIncrement(), pt1.getTransactionId(), 3, "Rohan Verma",
                1, "Aarav Patel", 5, "Book was in fantastic condition, exactly as described! Quick on-campus handover.");
        reviews.add(rev1);

        // 9. Sample Messages
        Message m1 = new Message(nextMessageId.getAndIncrement(), 201, "Higher Engineering Mathematics",
                2, "Priya Sharma", 1, "Aarav Patel", "Hey Aarav, is this edition compliant with the latest syllabus?", true);
        Message m2 = new Message(nextMessageId.getAndIncrement(), 201, "Higher Engineering Mathematics",
                1, "Aarav Patel", 2, "Priya Sharma", "Yes Priya! It covers all units including Laplace transforms and Fourier series.", true);
        messages.add(m1);
        messages.add(m2);

        // 10. Sample Report
        Report rep1 = new Report(nextReportId.getAndIncrement(), 3, "Rohan Verma", 1, "Aarav Patel",
                204, "Hero Roadeo 21-Speed Gear Bicycle", "Item condition discrepancy",
                "Seller mentioned brand new tires but tread is noticeably worn.", ReportStatus.OPEN);
        reports.add(rep1);

        // 11. Sample Barter / Swap Item
        Product p7 = new Product(107, 1, 1, "Engineering Mechanics (S. S. Bhavikatti)",
                "Prescribed textbook for First Year Engineering Mechanics. Clean pages, no torn sheets. Willing to swap for Sem 4 Database Systems or Microprocessors book.",
                ItemCondition.LIKE_NEW, "New Age International", 750.0,
                "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80");
        BarterListing l7 = new BarterListing(207, p7, s1, ListingStatus.AVAILABLE,
                "Sem 4 Database Systems / Microprocessors Book", 0.0, true);
        listings.add(l7);

        // 12. Sample Barter Proposal
        BarterProposal bp1 = new BarterProposal(nextProposalId.getAndIncrement(), 207, 2, "Priya Sharma", "2022-EE-115",
                "Database System Concepts (Silberschatz 7th Ed)", ItemCondition.LIKE_NEW,
                "Hardcover edition with all chapter summaries highlighted. Direct even exchange.", 0.0, ProposalStatus.PENDING);
        barterProposals.add(bp1);

        // 13. Sample Handover Schedule
        HandoverSchedule hs1 = new HandoverSchedule(nextScheduleId.getAndIncrement(), 201, "Higher Engineering Mathematics (44th Edition)",
                2, "Priya Sharma", 1, "Aarav Patel", "Central Library Ground Floor Steps", "Today, 5:30 PM",
                "Meet near the entrance notice board. Bring college ID card.");
        handoverSchedules.add(hs1);

        // 14. Sample Price Negotiation
        PriceNegotiation pn1 = new PriceNegotiation(nextNegotiationId.getAndIncrement(), 204, "Hero Roadeo 21-Speed Gear Bicycle",
                3400.0, 2, "Priya Sharma", 1, "Aarav Patel", 3000.0, "Can do ₹3000 if picked up from Aryabhata hostel today.");
        pn1.sellerCounter(3200.0, "Bicycle was serviced last week with new brake pads. Can settle at ₹3200 final.");
        priceNegotiations.add(pn1);

        // 15. Sample Clearance Receipt
        ClearanceReceipt cr1 = new ClearanceReceipt("CB-REC-2026-0042", "TXN-2026-CAMPUS-091", "PURCHASE_CLEARANCE",
                "Data Structures in C++ (Tanenbaum)", "Academic Books",
                "Aarav Patel", "2023-CS-042", "Computer Science",
                "Rohan Verma", "2024-ME-088", "Mechanical Engineering",
                350.0, 0.0, 0.0, 350.0, "Hostel Canteen Safe Zone");
        clearanceReceipts.add(cr1);
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;
        return usersByEmail.get(email.toLowerCase().trim());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Listing> getListings() {
        return listings;
    }

    public void addListing(Listing listing) {
        listings.add(0, listing); // newest first
    }

    public Listing getListingById(int id) {
        for (Listing l : listings) {
            if (l.getListingId() == id) return l;
        }
        return null;
    }

    public boolean removeListing(int id) {
        return listings.removeIf(l -> l.getListingId() == id);
    }

    public List<PurchaseRequest> getPurchaseRequests() {
        return purchaseRequests;
    }

    public void addPurchaseRequest(PurchaseRequest pr) {
        purchaseRequests.add(0, pr);
    }

    public PurchaseRequest getPurchaseRequestById(int id) {
        for (PurchaseRequest pr : purchaseRequests) {
            if (pr.getRequestId() == id) return pr;
        }
        return null;
    }

    public List<RentalRecord> getRentalRecords() {
        return rentalRecords;
    }

    public void addRentalRecord(RentalRecord rr) {
        rentalRecords.add(0, rr);
    }

    public RentalRecord getRentalRecordById(int id) {
        for (RentalRecord rr : rentalRecords) {
            if (rr.getRentalId() == id) return rr;
        }
        return null;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(0, t);
    }

    public boolean isWishlisted(int studentId, int listingId) {
        return wishlists.contains(studentId + ":" + listingId);
    }

    public boolean toggleWishlist(int studentId, int listingId) {
        String key = studentId + ":" + listingId;
        if (wishlists.contains(key)) {
            wishlists.remove(key);
            return false;
        } else {
            wishlists.add(key);
            return true;
        }
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review r) {
        reviews.add(0, r);
    }

    public List<Report> getReports() {
        return reports;
    }

    public void addReport(Report rep) {
        reports.add(0, rep);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public List<BarterProposal> getBarterProposals() {
        return barterProposals;
    }

    public void addBarterProposal(BarterProposal bp) {
        barterProposals.add(0, bp);
    }

    public BarterProposal getBarterProposalById(int id) {
        for (BarterProposal bp : barterProposals) {
            if (bp.getProposalId() == id) return bp;
        }
        return null;
    }

    public List<HandoverSchedule> getHandoverSchedules() {
        return handoverSchedules;
    }

    public void addHandoverSchedule(HandoverSchedule hs) {
        handoverSchedules.add(0, hs);
    }

    public HandoverSchedule getHandoverScheduleById(int id) {
        for (HandoverSchedule hs : handoverSchedules) {
            if (hs.getScheduleId() == id) return hs;
        }
        return null;
    }

    public List<PriceNegotiation> getPriceNegotiations() {
        return priceNegotiations;
    }

    public void addPriceNegotiation(PriceNegotiation pn) {
        priceNegotiations.add(0, pn);
    }

    public PriceNegotiation getPriceNegotiationById(int id) {
        for (PriceNegotiation pn : priceNegotiations) {
            if (pn.getNegotiationId() == id) return pn;
        }
        return null;
    }

    public List<ClearanceReceipt> getClearanceReceipts() {
        return clearanceReceipts;
    }

    public void addClearanceReceipt(ClearanceReceipt cr) {
        clearanceReceipts.add(0, cr);
    }

    public ClearanceReceipt getClearanceReceiptByNumber(String num) {
        for (ClearanceReceipt cr : clearanceReceipts) {
            if (cr.getReceiptNumber().equalsIgnoreCase(num) || cr.getTransactionCode().equalsIgnoreCase(num)) return cr;
        }
        return null;
    }

    public int nextUserId() {
        return nextUserId.getAndIncrement();
    }

    public int nextProductId() {
        return nextProductId.getAndIncrement();
    }

    public int nextListingId() {
        return nextListingId.getAndIncrement();
    }

    public int nextRequestId() {
        return nextRequestId.getAndIncrement();
    }

    public int nextRentalId() {
        return nextRentalId.getAndIncrement();
    }

    public int nextTransactionId() {
        return nextTransactionId.getAndIncrement();
    }

    public int nextReviewId() {
        return nextReviewId.getAndIncrement();
    }

    public int nextReportId() {
        return nextReportId.getAndIncrement();
    }

    public int nextMessageId() {
        return nextMessageId.getAndIncrement();
    }

    public int nextProposalId() {
        return nextProposalId.getAndIncrement();
    }

    public int nextScheduleId() {
        return nextScheduleId.getAndIncrement();
    }

    public int nextNegotiationId() {
        return nextNegotiationId.getAndIncrement();
    }

    public int nextReceiptId() {
        return nextReceiptId.getAndIncrement();
    }
}


