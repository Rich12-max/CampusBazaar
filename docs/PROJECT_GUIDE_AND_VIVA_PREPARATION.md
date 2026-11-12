# 🎓 Campus Peer-to-Peer Marketplace (CampusBazaar)
## Comprehensive Academic Guide & Viva Preparation

**Course:** Object-Oriented Programming Using Java (End-Semester Project)  
**Application URL:** [http://localhost:8080](http://localhost:8080)  
**Architecture:** Model-View-Controller (MVC) + Data Access Object (DAO)  
**Technology Stack:** Core Java (JDK 17+), Java Swing / Web Interface, MySQL, JDBC  

---

## 1. Project Overview & Identity
**CampusBazaar** is an exclusive, closed-loop peer-to-peer resource exchange platform built for college students. It allows verified students to buy, sell, and rent academic books, scientific calculators, mini drafters, hostel appliances, cycles, and lab gear directly on campus with safety and transparency.

---

## 2. Object-Oriented Programming (OOP) Implementation Map

During your faculty evaluation and project viva, you can confidently explain and point to the following Java implementations:

### 2.1 Encapsulation
- **Where:** All model classes in `com.campusbazaar.model` (`User`, `Student`, `Product`, `Listing`, `SaleListing`, `RentalListing`, `Transaction`, `RentalRecord`, `PurchaseRequest`).
- **How:** All attributes are declared `private` or `protected`. Public getters and setters provide access with data integrity checks and validation (see [`ValidationUtil.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/util/ValidationUtil.java)).
- **Why:** Protects internal object state from unauthorized modification and prevents invalid states (e.g., negative rental prices or inverted return dates).

### 2.2 Inheritance
- **User Hierarchy:**
  - `User` (Abstract base class with common fields like `userId`, `fullName`, `email`, `passwordHash`, `role`, `status`)
    - $\rightarrow$ [`Student`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/Student.java) (Extends `User` with `studentIdNumber`, `department`, `hostelAddress`, `isVerified`, `trustRating`)
    - $\rightarrow$ [`Admin`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/Admin.java) (Extends `User` with `staffCode`, `departmentDesignation`)
- **Listing Hierarchy:**
  - `Listing` (Abstract base class with `product`, `seller`, `listingType`, `status`, `postedAt`)
    - $\rightarrow$ [`SaleListing`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/SaleListing.java) (Extends `Listing` with `sellingPrice`, `originalPrice`, `isNegotiable`)
    - $\rightarrow$ [`RentalListing`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/RentalListing.java) (Extends `Listing` with `dailyRentalPrice`, `securityDeposit`, `minRentalDays`, `maxRentalDays`, `lateFeePerDay`)
- **Transaction Hierarchy:**
  - `Transaction` (Abstract base class with `transactionCode`, `totalAmount`, `paymentMethod`, `status`)
    - $\rightarrow$ [`PurchaseTransaction`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/PurchaseTransaction.java) (Extends `Transaction` with `campusMeetingSpot`)
    - $\rightarrow$ [`RentalTransaction`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/RentalTransaction.java) (Extends `Transaction` with `daysRented`, `daysOverdue`, `latePenaltyAmount`, `depositRefunded`)

### 2.3 Polymorphism (Method Overriding & Runtime Polymorphism)
- **Dynamic Pricing (`getEffectivePrice()`):**
  - In `SaleListing`: Returns outright selling price.
  - In `RentalListing`: Returns daily rental rate.
- **Dynamic Receipt Generation (`generateReceiptSummary()`):**
  - In `PurchaseTransaction`: Generates purchase handover receipt with campus spot.
  - In `RentalTransaction`: Generates rental settlement receipt showing duration, overdue days, late penalties deducted, and refunded security deposit.

### 2.4 Abstraction & Interfaces
- **`Searchable`** ([`Searchable.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/interfaces/Searchable.java)): Implemented by both `SaleListing` and `RentalListing` to standardize keyword search, category filtering, and price range evaluation.
- **`Rentable`** ([`Rentable.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/interfaces/Rentable.java)): Implemented by `RentalListing` defining `calculateRentalCost(int days)` and `calculateLatePenalty(int daysOverdue)`.
- **`Reviewable`** ([`Reviewable.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/interfaces/Reviewable.java)): Implemented by `Student` for peer trust score aggregation.
- **`Reportable`** ([`Reportable.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/model/interfaces/Reportable.java)): Implemented by `Listing` and `Student` for safety oversight and abuse flagging.

### 2.5 Custom Exception Handling
- Defined in `com.campusbazaar.exception`:
  - `CampusMarketplaceException` (Base checked exception)
  - `AuthenticationException` (Invalid credentials or suspended account)
  - `ListingNotFoundException` (Delisted or invalid listing ID)
  - `InvalidRentalDurationException` (Start date after end date, or exceeding allowed rental window)
  - `UnauthorizedOperationException` (Non-owner attempt to modify listing)
  - `ValidationException` (Malformed email, negative price, empty fields)

---

## 3. Key Standout Features

1. **Rule-Based Smart Price Suggestion Engine:**
   - Implemented in [`PriceSuggestionEngine.java`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/service/PriceSuggestionEngine.java).
   - Considers academic category depreciation baseline (Books: 60%, Lab Equipment: 65%, Electronics: 50%, Hostel: 45%), condition multiplier (Brand New: 1.0, Like New: 0.85, Good: 0.70, Fair: 0.50), and existing market listings without requiring external cloud AI APIs.
2. **Product Rental & Late Penalty Lifecycle:**
   - Tracks duration, computes daily rate $\times$ days, and automatically applies:
     $$\text{Late Penalty} = \text{Overdue Days} \times \text{Late Rate per Day}$$
     $$\text{Refunded Deposit} = \max(0, \text{Deposit} - \text{Late Penalty})$$
3. **Campus Safety & In-App Coordination:**
   - On-campus landmark meeting spot presets (Library Steps, Cafeteria, Block A, Hostel Gates) for secure peer exchanges.
   - Live buyer-seller threaded messaging.
   - Fraud reporting and administrative moderation center.

---

## 4. How to Run & Demonstrate

### 4.1 Running on Localhost (Web Browser)
The server is currently running on:
**[http://localhost:8080](http://localhost:8080)**

To run it again anytime from the terminal:
```powershell
.\compile.ps1
java -cp bin com.campusbazaar.Main
```
Or simply double-click `run.bat`!

### 4.2 Demo Personas (1-Click Switcher)
Click the user pill in the top-right navbar to test:
- 🎓 **Aarav Patel (Student Buyer / Seller):** View listings, send buy requests, rent calculators, and view wishlist.
- 🌸 **Priya Sharma (Senior Student Seller):** Accept purchase requests, approve calculator rentals, and settle deposits.
- 🛡️ **Prof. S. K. Mehta (Administrator / Moderator):** View campus statistics, investigate reported fraud items, and delist violations.

### 4.3 Database Script
The complete MySQL relational database schema with tables, foreign keys, cascade constraints, and sample seed data is located at:
[`database/schema.sql`](file:///c:/Users/Richa%20Sharma/Documents/java/database/schema.sql)

