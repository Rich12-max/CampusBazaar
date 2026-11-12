# 🎓 Campus Peer-to-Peer Marketplace (`CampusBazaar`)
### End-Semester Academic Software Project – Object-Oriented Programming Using Java

[![Java](https://img.shields.io/badge/Language-100%25%20Pure%20Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/UI-JavaFX%20%7C%20JavaFX%20CSS-FF6F00?style=for-the-badge)](src/main/resources/styles/marketplace.css)
[![Architecture](https://img.shields.io/badge/Architecture-MVC%20%2B%20DAO-2563EB?style=for-the-badge)](docs/)
[![Database](https://img.shields.io/badge/Database-MySQL%208.0%20%7C%20In--Memory-00758F?style=for-the-badge&logo=mysql&logoColor=white)](database/schema.sql)
[![Tests](https://img.shields.io/badge/Tests-37%20Passed%20%7C%200%20Failed-10B981?style=for-the-badge)](docs/MILESTONE_10_TEST_REPORT.md)
[![Status](https://img.shields.io/badge/Status-Complete%20%26%20Verified-8B5CF6?style=for-the-badge)](Campus_Peer_to_Peer_Marketplace_Project_Report.pdf)

---

## 📌 Project Overview

**CampusBazaar** is a specialized institutional peer-to-peer e-commerce and micro-rental marketplace engineered exclusively for college students and faculty. Within university campuses, students experience high recurring expenses each semester purchasing engineering/medical textbooks, scientific calculators, laboratory drawing tools (mini-drafters), hostel appliances, and commuter bicycles. 

Once exams conclude, these valuable assets sit idle in dormitory rooms while incoming junior cohorts repurchase identical items from retail vendors at full price. **CampusBazaar** solves this cyclical dilemma by establishing a closed-loop, trusted campus platform built using **Core Java (JDK 17+)** adhering strictly to an **academic Model-View-Controller (MVC)** architecture.

---

## ✨ Standout Features

- 🛡️ **Zero-Trust Student Identity Verification:** Closed-loop authentication restricted to institutional credentials (`@college.edu` domain and roll number mapping).
- 🏷️ **Polymorphic Listing Specialization:** Direct support for outright peer sales (`SaleListing`), daily micro-rentals (`RentalListing`), and peer item exchanges (`BarterListing`).
- ⏳ **Automated Micro-Rental & Late Penalty Engine:** Manages rental duration (1 to 30 days), upfront security deposit escrow, and calculates overdue penalties directly deducted from deposits.
- 💡 **Rule-Based Smart Price Suggestion Engine:** Calculates objective fair campus valuations based on category baseline retention (40%–65%) and cosmetic condition grade multipliers (50%–100%).
- 📍 **Safe Campus Landmark Handovers:** Coordinates physical meetings at campus landmarks (Library, Canteen, Hostel Gate) verified via cryptographic 6-digit OTP PINs.
- 🤝 **Multi-Round Counter-Offer Price Bargaining:** Structured 3-round counter-offer negotiation threads between buyers and sellers.
- 📄 **Digital Clearance Receipts:** Issues printable clearance vouchers with unique serial numbers (`CB-REC-YYYY-XXXX`) and SHA-256 cryptographic verification signatures for hostel clearance.
- 🎨 **Dual Presentation Layer:**
  - **Web Portal (`http://localhost:8080`):** Responsive glassmorphism interface featuring real-time theme customization across 8 harmonious aesthetic palettes (Campus Classic, Slate Dark, Imperial Gold, Matcha Mint, Sakura Blossom, Royal Lavender, Honey Amber, Neon Midnight).
  - **Desktop GUI (Java Swing):** Modern interactive desktop application (`MainSwingFrame`) with custom rounded cards, badge pills, and dialog flows.

---

## 🏗️ System Architecture & Object-Oriented Design

```
+---------------------------------------------------------------------------------------+
|                                    PRESENTATION LAYER                                 |
|  1. Web Portal (localhost:8080 - HTML5 / CSS3 Glassmorphism / JavaScript / Theme Eng) |
|  2. Desktop GUI (Java Swing - MainSwingFrame, MarketplaceView, RentalsView, AdminView)|
+---------------------------------------------------------------------------------------+
                                           | UI Events / HTTP Requests
                                           v
+---------------------------------------------------------------------------------------+
|                                     CONTROLLER LAYER                                  |
|  AuthController, MarketplaceController, RentalController, AdminController             |
+---------------------------------------------------------------------------------------+
                                           | Business Delegation
                                           v
+---------------------------------------------------------------------------------------+
|                                     SERVICE LAYER                                     |
|  AuthService, ListingService, RentalService, PriceSuggestionEngine, PurchaseService,  |
|  MessagingService, ReviewService, ReportService, AdminService, BarterService,        |
|  HandoverService, NegotiationService, ReceiptService                                  |
+---------------------------------------------------------------------------------------+
                   | Domain Objects                            | CRUD Operations
                   v                                           v
+---------------------------------------+    +------------------------------------------+
|              MODEL LAYER              |    |          DATA ACCESS OBJECTS (DAO)       |
|  User (Abstract), Student, Admin      |    |  UserDAO, ListingDAO, RentalDAO,         |
|  Listing (Abstract), SaleListing,     |<-->|  TransactionDAO, MessageDAO, ReviewDAO   |
|  RentalListing, BarterListing, Product|    +------------------------------------------+
|  HandoverSchedule, PriceNegotiation,  |                      | Parameterized SQL
|  ClearanceReceipt, BarterProposal     |                      v
+---------------------------------------+    +------------------------------------------+
                                             |             DATABASE PERSISTENCE         |
                                             |  1. MySQL Database (schema.sql)          |
                                             |  2. In-Memory Thread-Safe DataStore      |
                                             +------------------------------------------+
```

### Core OOP Principles Implemented:
- **Encapsulation:** Private attributes with validated getters and mutators preventing negative prices and invalid rental bounds.
- **Inheritance:** Abstract specialization hierarchies for Users (`User` $\rightarrow$ `Student`, `Admin`) and Listings (`Listing` $\rightarrow$ `SaleListing`, `RentalListing`, `BarterListing`).
- **Runtime Polymorphism:** Dynamic method dispatch via `getEffectivePrice()` and `calculateRentalCharges()` resolved at runtime across heterogeneous collections.
- **Interface Segregation:** Decoupled contracts (`Searchable`, `Rentable`, `Reviewable`, `Reportable`).
- **Custom Exceptions:** Strongly-typed exception hierarchy rooted in `CampusMarketplaceException`.
- **Dual Persistence:** Production MySQL 8.0 with automated fallback to in-memory concurrent `DataStore` (`ConcurrentHashMap`).

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK) 17** or higher.
- *(Optional)* **MySQL Community Server 8.0+** (The project runs seamlessly in in-memory mode without MySQL).

### Compilation
Using PowerShell:
```powershell
powershell -ExecutionPolicy Bypass -File compile.ps1
```
Or using Command Prompt / Batch:
```cmd
build.bat
```

### Execution
Start the CampusBazaar server:
```powershell
java -cp bin com.campusbazaar.Main
```
- 🌐 Open your web browser and navigate to: **`http://localhost:8080/`**
- 🖥️ To launch the Java Swing Desktop GUI window as well:
```powershell
java -cp bin com.campusbazaar.Main --gui
```

### Running the Automated Test Suite
Execute all 37 comprehensive test cases:
```powershell
java -cp bin com.campusbazaar.test.CampusMarketplaceTestSuite
```
**Verification Outcome:** `37 PASSED | 0 FAILED (100% Success Rate)`.

---

## 📂 Project Structure

```
├── Campus_Peer_to_Peer_Marketplace_Project_Report.docx  # Complete Academic Project Report (Word)
├── Campus_Peer_to_Peer_Marketplace_Project_Report.pdf   # Companion Academic Project Report (PDF)
├── README.md                                           # Project Documentation & Guide
├── build.bat                                           # Windows Batch build script
├── compile.ps1                                         # PowerShell compilation script
├── run.bat                                             # Application startup script
├── pom.xml                                             # Maven configuration
├── database/
│   └── schema.sql                                      # Complete MySQL 15-table relational DDL & seeds
├── docs/
│   ├── CAMPUS_PEER_TO_PEER_MARKETPLACE_COMPLETE_PROJECT_REPORT.md # Full Academic Report (Markdown)
│   ├── FINAL_PROJECT_REPORT_AND_VIVA_MANUAL.md         # Faculty Viva Guide & Q&A
│   ├── MILESTONE_10_TEST_REPORT.md                     # Comprehensive 37-Test Execution Matrix
│   ├── MILESTONE_1_AND_2_EXTREME_REQUIREMENT_ANALYSIS_SRS.md # Formal SRS Specification
│   └── MILESTONE_1_SYSTEM_ANALYSIS_AND_DESIGN.md       # Architectural & UML System Design
└── src/main/
    ├── java/com/campusbazaar/
    │   ├── Main.java                                   # Application Entrypoint
    │   ├── dao/                                        # Data Access Objects (UserDAO, ListingDAO)
    │   ├── database/                                   # DBConnectionManager & In-Memory DataStore
    │   ├── exception/                                  # Custom Exception Hierarchy
    │   ├── model/                                      # Domain Entities, Hierarchies & Enums
    │   ├── server/                                     # Zero-Dependency Embedded HTTP Server
    │   ├── service/                                    # Business Logic & Mathematical Engines
    │   ├── test/                                       # CampusMarketplaceTestSuite (37 Tests)
    │   ├── ui/                                         # Java Swing Desktop GUI Components & Views
    │   └── util/                                       # PasswordHasher (SHA-256) & Validation
    └── resources/web/
        └── index.html                                  # Glassmorphic Responsive Web Portal
```

---

## 📄 Academic Documentation & Project Reports

The repository includes complete, university-ready documentation:
1. **[Academic Project Report (DOCX)](Campus_Peer_to_Peer_Marketplace_Project_Report.docx):** Complete editable report formatted with academic cover page, certificate, abstract, 10 chapters, tables, and appendices.
2. **[Academic Project Report (PDF)](Campus_Peer_to_Peer_Marketplace_Project_Report.pdf):** Clean printable PDF format.
3. **[Viva Voce Preparation Manual](docs/FINAL_PROJECT_REPORT_AND_VIVA_MANUAL.md):** High-priority questions and detailed technical answers for oral evaluation.

---

## 🚀 How to Run the 100% Pure Java Application

The project is built entirely in **Java 17+ and JavaFX**, requiring no web servers or external Node/Python runtimes.

### Option 1: Double-Click Batch Launch (Windows)
Double-click `run.bat` (or execute from terminal):
```cmd
build.bat
run.bat
```

### Option 2: Run via Maven (IntelliJ IDEA / Eclipse / CLI)
```bash
mvn compile javafx:run
```

### Option 3: Run the Automated Academic Verification Test Suite
```bash
java -cp "bin;lib/*" com.campusbazaar.test.CampusMarketplaceTestSuite
```
*(Executes all 37 automated tests validating polymorphism, rental penalties, OTP PINs, and SHA-256 signatures).*

---

## 👥 Contributors & Academic Credits

- **Aarav Patel** (Roll No: 2023-CS-042) – *Student Lead & System Architect*
- **Rohan Verma** (Roll No: 2024-ME-088) – *Domain Specialist & Database Lead*
- **Priya Sharma** (Roll No: 2023-IT-019) – *UI/UX Designer & Testing Lead*

*Department of Computer Science & Engineering | Academic Year 2025–2026*
