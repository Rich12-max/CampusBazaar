# 🎓 CAMPUS PEER-TO-PEER MARKETPLACE ("CAMPUSBAZAAR")
## Final Academic Project Report, User Manual & Faculty Viva Guide
**Course:** Object-Oriented Programming Using Java (End-Semester Evaluation)  
**Academic Degree:** B.Tech / B.E. Computer Science & Engineering / Information Technology  
**Architecture:** Model-View-Controller (MVC) with Data Access Object (DAO) Pattern  
**Stack:** Core Java (JDK 17+), Java Swing / Web Interface, MySQL, JDBC  

---

## 1. Project Abstract
CampusBazaar is an institutional, closed-loop peer-to-peer e-commerce and micro-rental marketplace engineered exclusively for university students and campus personnel. Designed to address the high cyclical costs of tertiary education, the system allows verified students to trade, sell, or rent semester textbooks, scientific calculators, mini-drafters, hostel equipment, electronics, and bicycles directly on campus. 

The software demonstrates core Object-Oriented Programming (OOP) paradigms in Java:
- **Encapsulation:** Protected state boundaries across domain entities with strict validation.
- **Inheritance & Hierarchy:** Polymorphic abstractions for users (`Student`, `Admin`), listings (`SaleListing`, `RentalListing`), and transactions (`PurchaseTransaction`, `RentalTransaction`).
- **Polymorphism:** Runtime dynamic dispatch for pricing, late-penalty calculations, and receipt formatting.
- **Interface Segregation:** Standalone contracts for `Searchable`, `Rentable`, `Reviewable`, and `Reportable`.
- **Custom Exceptions:** Robust error handling preventing negative prices, inverted rental dates, and duplicate registrations.
- **Dual Presentation Layer:** Both an aesthetic Web Portal on `http://localhost:8080` (with real-time Gold Aesthetic theme customization) and an interactive Java Swing Desktop GUI window.

---

## 2. System Architecture & MVC Workflow

```
+---------------------------------------------------------------------------------------+
|                                    PRESENTATION LAYER                                 |
|  1. Web Portal (localhost:8080 - HTML5 / CSS3 Glassmorphism / JavaScript / Gold Theme)|
|  2. Desktop GUI (Java Swing - MainSwingFrame, MarketplaceView, RentalsView, AdminView)|
+---------------------------------------------------------------------------------------+
                                           | UI Events
                                           v
+---------------------------------------------------------------------------------------+
|                                     CONTROLLER LAYER                                  |
|  AuthController, MarketplaceController, RentalController, AdminController             |
+---------------------------------------------------------------------------------------+
                                           | Delegates Work
                                           v
+---------------------------------------------------------------------------------------+
|                                     SERVICE LAYER                                     |
|  AuthService, ListingService, RentalService, PriceSuggestionEngine, PurchaseService,  |
|  MessagingService, ReviewService, ReportService, AdminService, BarterService,        |
|  HandoverService, NegotiationService, ReceiptService                                  |
+---------------------------------------------------------------------------------------+
                   |                                           |
                   v                                           v
+---------------------------------------+    +------------------------------------------+
|              MODEL LAYER              |    |          DATA ACCESS OBJECTS (DAO)       |
|  User (Abstract), Student, Admin      |    |  UserDAO, ListingDAO, RentalDAO,         |
|  Listing (Abstract), SaleListing,     |<-->|  TransactionDAO, MessageDAO, ReviewDAO   |
|  RentalListing, BarterListing, Product|    +------------------------------------------+
|  HandoverSchedule, PriceNegotiation,  |
|  ClearanceReceipt, BarterProposal     |
+---------------------------------------+                      | JDBC Parameterized SQL
                                                               v
                                             +------------------------------------------+
                                             |             DATABASE PERSISTENCE         |
                                             |  1. MySQL Database (schema.sql)          |
                                             |  2. In-Memory Thread-Safe DataStore      |
                                             +------------------------------------------+
```

---

## 3. High-Priority Faculty Viva Questions & Answers

### Q1: Why did you choose MVC architecture over a two-tier architecture?
**Answer:** In a standard two-tier architecture, GUI event handlers directly execute SQL queries and business math. If the database schema changes, the UI breaks. MVC strictly separates:
- **Model:** Independent Java classes representing data entities (`User`, `Listing`, `Transaction`). Zero UI or SQL dependencies.
- **View:** Pure presentation (Swing components or Web templates).
- **Controller & Service:** Business rules (e.g., rental overdue penalties, smart price depreciation calculations).
- **DAO:** Isolated persistence layer.
This ensures high maintainability, unit testability, and adherence to the Single Responsibility Principle.

### Q2: Where is Runtime Polymorphism implemented in your project? Can you demonstrate it?
**Answer:** Runtime polymorphism is implemented in the `Listing` class hierarchy:
```java
public abstract class Listing {
    public abstract double getEffectivePrice();
}
```
In `SaleListing`, `getEffectivePrice()` dynamically returns the outright selling price. In `RentalListing`, it dynamically returns the daily rental rate.
When the marketplace iterates over a heterogeneous collection `List<Listing> listings`, the JVM dynamically dispatches the appropriate method at runtime without `instanceof` casting:
```java
for (Listing item : listings) {
    double price = item.getEffectivePrice(); // Dynamically resolved!
}
```
This is verified by our automated test suite in `CampusMarketplaceTestSuite.java` (Test `TC-01`).

### Q3: How does your rental penalty engine work?
**Answer:** The rental engine computes charges algorithmically based on calendar duration:
$$\text{Actual Days} = \max(1, T_{\text{actual}} - T_{\text{start}})$$
$$\text{Overdue Days} = \max(0, T_{\text{actual}} - T_{\text{expected}})$$
$$\text{Late Penalty} = \text{Overdue Days} \times P_{\text{penalty/day}}$$
$$\text{Refunded Deposit} = \max(0, D_{\text{security}} - \text{Late Penalty})$$
If an item is returned on time, overdue days equal zero and zero penalty is deducted. If overdue, the late penalty is automatically subtracted from the security deposit, preventing awkward peer confrontations.

### Q4: Why did you use `PreparedStatement` instead of `Statement`?
**Answer:** `PreparedStatement` provides two crucial benefits:
1. **Security:** Pre-compiles SQL queries on the database engine. User input is treated strictly as literal parameter data, completely preventing SQL Injection attacks.
2. **Performance:** Database engines pre-compile and cache the query execution plan, significantly accelerating repeated executions.

### Q5: How do you protect passwords without storing them in plain text?
**Answer:** We implemented SHA-256 cryptographic hashing with an institutional salt in `PasswordHasher.java`:
$$\text{Hash} = \text{SHA-256}(\text{Raw Password} + \text{Institutional Salt})$$
Plain text passwords are never stored in the database. During login, the entered password is salted and hashed using the identical algorithm, and the resulting 64-character hexadecimal digest is compared against the stored hash.

### Q6: How does the new Peer-to-Peer Barter & Swap module demonstrate OOP principles?
**Answer:** The `BarterListing` class extends the abstract base class `Listing` and implements `Searchable`. It polymorphically overrides `getEffectivePrice()` (to return `cashAdjustment`) and `getPricingDisplay()` (to format the swap item request). This demonstrates **Polymorphism and Open-Closed Principle (OCP)**: we extended the marketplace with barter capabilities without modifying existing `SaleListing` or `RentalListing` classes.

### Q7: Why is the Campus Safe Meetup Scheduler using a 6-digit OTP verification PIN?
**Answer:** In student peer exchanges, the primary safety and trust hazard is disputing whether a physical handover occurred. When a handover is scheduled, the buyer is assigned a secure 6-digit cryptographic PIN (`HandoverSchedule.getVerificationPin()`). Only upon meeting at a designated campus spot (e.g. Library Steps) does the buyer disclose the PIN to the seller. When the seller submits the matching PIN, the system verifies and completes the transaction, updating the listing to `SOLD` and releasing official clearance.

### Q8: How does the Price Bargaining engine prevent infinite negotiation loops?
**Answer:** The `PriceNegotiation` class enforces a strict state machine bounded by `MAX_ROUNDS = 3`. Each party can only counter when `canCounter()` returns true. Once round 3 is reached, the parties must either Accept or Decline, ensuring quick resolution and avoiding lingering stalled threads.

### Q9: What ensures the integrity of the Digital Clearance Slip and Receipt?
**Answer:** Each `ClearanceReceipt` computes a SHA-256 cryptographic digital signature hash over its payload:
$$\text{Signature} = \text{SHA-256}(\text{Receipt\#} + \text{TxnCode} + \text{ItemTitle} + \text{RollNumbers} + \text{Amount})[0..24]$$
Any manual alteration of the printable receipt or PDF slip invalidates the cryptographic signature, providing verifiable proof for university hostel clearance and administrative property transfers.

---

## 4. Comprehensive User Manual

### 4.1 Launching the Application
- **To Launch Web Portal:** Double click `run.bat` or run:
  ```powershell
  java -cp bin com.campusbazaar.Main
  ```
  Open **[http://localhost:8080](http://localhost:8080)** in any web browser.
- **To Launch Desktop Swing GUI:** Run:
  ```powershell
  java -cp bin com.campusbazaar.Main --gui
  ```
- **To Run Verification Test Suite:** Run:
  ```powershell
  powershell -ExecutionPolicy Bypass -File compile.ps1
  java -cp bin com.campusbazaar.test.CampusMarketplaceTestSuite
  ```

### 4.2 Navigating Key Features
1. **Browse Marketplace:** View categorized items, filter by type (`Sale` / `Rental` / `Barter`), or search by title/brand.
2. **Book Barter & Peer Swaps:** Click "Swaps" to browse swap requests (e.g. trading Engineering Mechanics for Database Systems), submit a proposal offering your own book, or accept incoming barter offers.
3. **Campus Safe Meetup & OTP Verification:** Click "Handovers" to inspect scheduled meeting spots (Library, SAC Desk, Security Post), view the buyer's 6-digit OTP PIN, or enter the PIN as the seller to verify and complete hand-off on-the-spot.
4. **Price Bargaining & Counter-Offers:** Click "Bids" to review active student negotiation threads. Submit a counter-offer, accept an agreed price, or decline a bid within the 3-round limit.
5. **Printable Clearance Receipts:** Click "Receipts" to view official digital clearance vouchers with SHA-256 integrity signatures, and click "Print / Download Clearance Voucher" for university hostel clearance.
6. **Switch Aesthetic Themes:** Click any circular color dot in the top-right navbar to immediately toggle between **Champagne Gold 👑**, **Obsidian Gold ⚜️**, **Rose Gold ✨**, **Sakura Blossom 🌸**, and **Matcha Milk 🍵**.
7. **Switch Demo Personas:** Click the user pill in the top-right navbar to toggle between **Aarav Patel (Buyer)**, **Priya Sharma (Seller)**, and **Prof. S. K. Mehta (Admin)**.
8. **Calculate Smart Prices:** Click "Smart Pricing" to test the rule-based pricing calculator.
9. **Manage Rentals:** Click "Rentals" to view active campus rentals, see overdue flags, and settle deposits.
10. **Admin Moderation:** Click "Moderation" to inspect reports, dismiss claims, or delist prohibited items.

