# CAMPUS PEER-TO-PEER MARKETPLACE ("CAMPUSBAZAAR")
## Milestones 1 & 2: Extreme-Detailing Requirement Analysis & Formal SRS
**Course:** Object-Oriented Programming Using Java (End-Semester Academic Evaluation)  
**Level:** Undergraduate Computer Science & Engineering / IT  
**Standard:** IEEE 830-1998 Software Requirements Specifications Standard Adapted for Academic Rigor  
**Author:** Senior Java Software Architect & Academic Mentorship Team  

---

## 1. Executive Summary & Problem Space

### 1.1 The Campus Resource Dilemma
Within tertiary academic institutions (universities and technical colleges), students experience acute cyclical financial outlays at the beginning of each semester. Essential educational resources including textbooks (e.g., Higher Engineering Mathematics, Operating System Concepts), specialised hardware (e.g., Casio FX-991EX ClassWiz scientific calculators, Arduino/Raspberry Pi development boards), laboratory implements (e.g., engineering mini-drafters, dissection kits, lab coats), hostel appliances (e.g., electric kettles, study lamps), and campus mobility assets (e.g., bicycles) represent significant capital investments. 

Once a semester or academic year concludes, these assets sit dormant in student dormitories. Concurrently, the incoming junior cohort purchases identical items from off-campus commercial vendors at standard retail prices. 

### 1.2 Systematic Failures of Generic Classifieds (OLX, Quikr, Facebook Marketplace)
1. **Absence of Student Identity Vetting:** Anyone can create an account, introducing stranger danger, harassment, and off-campus predatory actors.
2. **Logistical Incompatibility:** Generic platforms expect courier shipping or distant off-campus handovers. Campus life requires immediate handovers at known campus landmarks (e.g., Central Library, Hostel Gates, Canteen).
3. **Lack of Short-Term Rental Infrastructure:** Students frequently require an item for a short period (e.g., a scientific calculator for an exam week, or a mini-drafter for a 4-week graphics module). Generic portals only support outright sales.
4. **No Automated Overdue Penalty Settlement:** Informal peer borrowing results in unreturned goods, damaged equipment, or lost deposits due to lack of an algorithmic tracking and penalty settlement system.
5. **Information Asymmetry & Price Gouging:** Students lack objective data on fair second-hand campus valuations, resulting in arbitrary pricing.

### 1.3 The CampusBazaar Solution
CampusBazaar is an institutional, closed-loop peer-to-peer commerce and micro-rental platform engineered exclusively for college campuses. It operates on three pillars:
- **Zero-Trust Institutional Verification:** Strict simulated institutional credentials (university email domain and unique roll number mapping).
- **Dual-Model Resource Sharing:** Outright sale and micro-rentals governed by automated duration bounds and late penalty engines.
- **Strict Academic MVC Object-Oriented Architecture:** Engineered in Core Java with strong OOP principles (polymorphic pricing, abstract lifecycles, and interface segregation).

---

## 2. Stakeholders & User Persona Analysis

| Stakeholder Role | Academic Context | Key Goals | Pain Points & Constraints |
|---|---|---|---|
| **Student Buyer** | 1st to 4th year undergraduate student | Procure required semester books and lab equipment at 40-70% discount; ensure safe on-campus inspection. | Limited budget; vulnerability to scams; inability to inspect items before payment. |
| **Student Seller** | Senior student (2nd to 4th year) | Liquidate surplus course materials, cycles, and hostel gear to recover cash before graduating or shifting dorms. | Time-consuming negotiations; unverified buyers failing to show up at meeting points. |
| **Student Borrower** | Examination candidate / Lab student | Temporarily access expensive calculators or equipment for days or weeks without buying them outright. | Inability to borrow without collateral; risk of losing deposit if disputes arise. |
| **Student Lender** | Equipment owner with idle gear | Monetize dormant gear safely with a guaranteed security deposit and automated late fees. | Fear of damaged or unreturned equipment; awkward peer confrontations regarding overdue fees. |
| **Campus Administrator** | Dean of Student Affairs / Student Council Moderator | Ensure safety, remove prohibited goods (e.g., weapons, contraband, exam cheats), resolve disputes, and maintain platform integrity. | Need for complete transaction audit trails; quick account suspension controls. |

---

## 3. High-Granularity Functional Requirements (FR Specifications)

### [FR-001] Student Registration & Institutional Credential Verification
- **Requirement ID:** FR-001
- **Module:** Authentication & Verification
- **Priority:** Critical (P0)
- **Actor:** Prospective Student
- **Business Justification:** Prevents non-institutional actors from accessing the platform, eliminating commercial spammers and ensuring physical safety during handovers.
- **Preconditions:** User is not authenticated; possesses an active university-issued email address (`@college.edu`).
- **Trigger:** Student clicks "Register / Sign Up" button on UI.
- **Main Workflow:**
  1. User inputs: Full Name, Institutional Email, Raw Password, Mobile Number, Student Roll Number, Branch/Department, and Hostel/Day-Scholar Address.
  2. System validates input formats (email syntax, password strength, mandatory fields).
  3. System hashes the raw password using SHA-256 with an institutional cryptographic salt string.
  4. System executes verification simulator: Generates a 6-digit numeric token and displays an institutional OTP verification prompt.
  5. User inputs the 6-digit token.
  6. System creates a `Student` entity with `isVerified = true` and `AccountStatus = ACTIVE`.
  7. Record is persisted to database within a single atomic transaction.
- **Alternative Workflow:** If user closes OTP simulator, account is saved with `AccountStatus = PENDING_VERIFICATION`; listing and purchasing capabilities remain disabled.
- **Exception Scenarios:**
  - *E-01.1:* Duplicate institutional email. Action: Throw `AuthenticationException("Email already registered")`.
  - *E-01.2:* Duplicate Student Roll Number. Action: Reject registration and inform user to contact campus moderator.
  - *E-01.3:* Weak password (< 6 chars). Action: Throw `ValidationException("Password must be at least 6 characters")`.
- **Postconditions:** Student account created, hashed in persistence store, and authenticated session initialized.
- **Acceptance Criteria:** Account created with SHA-256 hash (never plain text); verified badge visible in profile.

---

### [FR-002] Multi-Factor Authentication & Role-Based Session Initialization
- **Requirement ID:** FR-002
- **Module:** Authentication & Verification
- **Priority:** Critical (P0)
- **Actor:** Registered Student, Campus Administrator
- **Business Justification:** Enforces role-based access control (RBAC), preventing students from accessing moderation tools and preventing unauthorized account takeover.
- **Preconditions:** Account exists in persistence store.
- **Trigger:** User submits login form with email and password.
- **Main Workflow:**
  1. System looks up user by normalized lowercase email.
  2. System verifies SHA-256(rawPassword + SALT) matches `password_hash`.
  3. System verifies account is not `SUSPENDED`.
  4. System initializes session state (`AuthService.setCurrentUser(user)`).
  5. If role is `STUDENT`, render Student Dashboard; if `ADMIN`, render Administrator Moderation Console.
- **Exception Scenarios:**
  - *E-02.1:* Account suspended by moderator. Action: Deny access with error dialog: *"Your account has been suspended by campus moderators due to policy violations."*
  - *E-02.2:* Password mismatch. Action: Deny access with generic error: *"Invalid credentials."*
- **Postconditions:** Active session created, session token stored in memory, appropriate UI dashboard presented.

---

### [FR-003] Product Categorization & Sale Listing Creation
- **Requirement ID:** FR-003
- **Module:** Listing & Catalog Management
- **Priority:** High (P1)
- **Actor:** Verified Student Seller
- **Business Justification:** Standardizes item cataloging, enables effective search, and captures physical depreciation characteristics.
- **Preconditions:** User is authenticated as `Student` and `isVerified == true`.
- **Trigger:** Seller clicks "Post Listing" and selects "Outright Sale".
- **Main Workflow:**
  1. Seller inputs: Category ID, Item Title, Detailed Description, Condition Grade (`BRAND_NEW`, `LIKE_NEW`, `GOOD`, `FAIR`), Original Retail Price, Selling Price, Negotiable flag (`true`/`false`), and Image URL/path.
  2. System validates: Title non-empty, Selling Price > 0, Original Price $\ge$ Selling Price.
  3. System instantiates `Product` object (Composition inside `SaleListing`).
  4. System instantiates `SaleListing` with status `AVAILABLE`.
  5. Record persisted; marketplace catalog refreshed.
- **Validation Rules:**
  - $\text{Selling Price} \le \text{Original Price}$ (Enforces second-hand nature; prevents scalping).
  - Condition must be selected from strict enum `ItemCondition`.
- **Postconditions:** Item appears immediately in marketplace feed under designated category.

---

### [FR-004] Micro-Rental Listing Creation with Safety Collateral
- **Requirement ID:** FR-004
- **Module:** Listing & Catalog Management
- **Priority:** High (P1)
- **Actor:** Verified Student Seller / Lender
- **Business Justification:** Unlocks short-term access to expensive semester equipment while securing the lender via refundable security deposits and late-return deterrents.
- **Preconditions:** User is authenticated as verified `Student`.
- **Trigger:** Seller clicks "Post Listing" and selects "Campus Rental (Daily)".
- **Main Workflow:**
  1. Seller inputs: Category, Title, Description, Condition, Daily Rental Rate ($R$), Security Deposit ($D$), Minimum Duration ($d_{\min}$), Maximum Duration ($d_{\max}$), and Daily Late Penalty Fee ($P$).
  2. System validates: $R > 0$, $D > 0$, $1 \le d_{\min} \le d_{\max} \le 60$, and $P \ge R$.
  3. System creates `RentalListing` extending `Listing` and implementing `Rentable`.
  4. Listing registered with status `AVAILABLE`.
- **Validation Rules:**
  - Late fee per day $P$ cannot be zero (must act as economic deterrent against hoarding).
  - Security Deposit $D$ must exceed estimated rental charges to ensure collateral adequacy.
- **Postconditions:** Rentable product indexed and flagged with `"✨ RENT"` badge.

---

### [FR-005] Rule-Based Smart Campus Pricing Engine
- **Requirement ID:** FR-005
- **Module:** Smart Pricing Engine
- **Priority:** Medium (P2)
- **Actor:** Student Seller, Student Buyer
- **Business Justification:** Eliminates campus price gouging, ensures transparency, and assists junior students in making fair purchase decisions without relying on commercial external APIs.
- **Preconditions:** Valid category and original retail price entered.
- **Algorithm & Formulation:**
  $$\text{Base Valuation} = \text{Original Price} \times C_{\text{factor}} \times M_{\text{cond}}$$
  Where Category Retention Factors $C_{\text{factor}}$:
  - Academic Books: $0.60$
  - Calculators & Electronics: $0.50$
  - Lab Equipment: $0.65$
  - Hostel Essentials: $0.45$
  - Cycles & Sports: $0.55$
  - Stationery & Notes: $0.40$
  
  Condition Multipliers $M_{\text{cond}}$:
  - Brand New: $1.00$
  - Like New: $0.85$
  - Good: $0.70$
  - Fair: $0.50$

  Market Adjustment:
  $$\text{Final Fair Target} = (\text{Base Valuation} \times 0.70) + (\text{Market Average of Category} \times 0.30)$$
  $$\text{Suggested Range} = [\max(50, 0.85 \times \text{Fair Target}), 1.15 \times \text{Fair Target}]$$
- **Postconditions:** Returns `PriceSuggestion` containing Min, Max, Target Fair Price, and technical justification string.

---

### [FR-006] Marketplace Search, Multi-Criteria Filtering & Sorting
- **Requirement ID:** FR-006
- **Module:** Catalog & Browsing
- **Priority:** High (P1)
- **Actor:** Student Buyer / Borrower
- **Preconditions:** Catalog loaded.
- **Main Workflow:**
  1. Buyer enters search string (matches title, brand, or description case-insensitively).
  2. Buyer selects category filter pill (0 for All, 1-6 for specific categories).
  3. Buyer toggles listing type (`ALL`, `SALE`, `RENTAL`).
  4. Buyer selects sort criterion (`NEWEST`, `PRICE_ASC`, `PRICE_DESC`).
  5. System applies `Searchable` interface matching polymorphically.
- **Acceptance Criteria:** Real-time filtering executes in $< 50\text{ms}$ over in-memory collections and optimized indexed SQL queries.

---

### [FR-007] Purchase Request Lifecycle & Campus Handover Negotiation
- **Requirement ID:** FR-007
- **Module:** Purchase Request Management
- **Priority:** Critical (P0)
- **Actor:** Student Buyer, Student Seller
- **Preconditions:** Listing is `AVAILABLE`. Buyer ID $\neq$ Seller ID.
- **Trigger:** Buyer clicks "Buy Now" on a `SaleListing`.
- **Main Workflow:**
  1. System checks: Buyer cannot purchase own listing. If attempted, throw `ValidationException`.
  2. Buyer selects campus meeting location from verified presets:
     - 🏛️ *Central Library Entrance Steps*
     - ☕ *Campus Main Canteen & Cafeteria*
     - 🏢 *Academic Block A Fountain*
     - 🚪 *Hostel Gate #1 Security Post*
     - 🏸 *Sports Complex Badminton Court*
  3. Buyer inputs offered price and optional coordination note.
  4. System registers `PurchaseRequest` with status `PENDING`.
  5. Seller views request in incoming dashboard and clicks "Accept".
  6. Listing status transitions from `AVAILABLE` to `RESERVED`.
  7. Upon meeting on campus, both parties confirm inspection and handover.
  8. System transitions request to `COMPLETED`, listing to `SOLD`, and creates immutable `PurchaseTransaction` record.
- **Exception Scenarios:**
  - Listing already reserved by another buyer: Request blocked; notification displayed.
  - Seller declines: Request status set to `DECLINED`; listing remains `AVAILABLE`.

---

### [FR-008] Rental Booking, Overdue Detection & Automated Penalty Settlement
- **Requirement ID:** FR-008
- **Module:** Micro-Rental Management
- **Priority:** Critical (P0)
- **Actor:** Student Borrower, Student Lender
- **Preconditions:** Listing is `RentalListing` with status `AVAILABLE`.
- **Workflow & Algorithmic Rules:**
  1. Borrower selects start date $T_{\text{start}}$ and expected return date $T_{\text{exp}}$.
  2. Validation: $T_{\text{start}} \le T_{\text{exp}}$ and $d_{\min} \le (T_{\text{exp}} - T_{\text{start}}) \le d_{\max}$.
  3. System computes initial deposit required:
     $$\text{Estimated Rent} = \text{Days} \times R_{\text{daily}}$$
     $$\text{Total Collateral Payable} = \text{Estimated Rent} + D_{\text{security}}$$
  4. Lender approves handover; status becomes `ACTIVE_RENTAL`; listing status becomes `RENTED`.
  5. When item is returned on actual date $T_{\text{act}}$:
     $$\text{Actual Days} = \max(1, T_{\text{act}} - T_{\text{start}})$$
     $$\text{Overdue Days} = \max(0, T_{\text{act}} - T_{\text{exp}})$$
     $$\text{Late Penalty} = \text{Overdue Days} \times P_{\text{late/day}}$$
     $$\text{Total Owed} = (\text{Actual Days} \times R_{\text{daily}}) + \text{Late Penalty}$$
     $$\text{Refunded Deposit} = \max(0, D_{\text{security}} - \text{Late Penalty})$$
  6. Lender confirms return in good order; listing status reverts to `AVAILABLE`.
  7. Immutable `RentalTransaction` audit record generated.

---

### [FR-009] Bookmark & Wishlist Management
- **Requirement ID:** FR-009
- **Module:** Wishlist
- **Priority:** Medium (P2)
- **Actor:** Student
- **Workflow:** Toggle heart button on any product card. Idempotently adds or removes `(studentId, listingId)` mapping in `wishlists` table; updates UI count badge.

---

### [FR-010] Contextual Peer-to-Peer Messaging
- **Requirement ID:** FR-010
- **Module:** Messaging
- **Priority:** High (P1)
- **Actor:** Buyer, Seller
- **Workflow:** Threaded chat tied specifically to `(listingId, buyerId, sellerId)`. Allows instant scheduling of physical inspection at campus landmarks.

---

### [FR-011] Two-Way Reputation & Rating System
- **Requirement ID:** FR-011
- **Module:** Reviews & Trust
- **Priority:** High (P1)
- **Actor:** Transaction Participants
- **Preconditions:** Transaction status is `COMPLETED`.
- **Workflow:** Participant submits star rating (1 to 5) and feedback comment. System updates target student's `trustRating` running weighted average:
  $$\text{New Rating} = \frac{(\text{Current Rating} \times N) + \text{Stars}}{N + 1}$$

---

### [FR-012] Fraud, Damage & Policy Abuse Reporting
- **Requirement ID:** FR-012
- **Module:** Safety & Moderation
- **Priority:** High (P1)
- **Actor:** Student
- **Workflow:** Student flags an item or peer selecting reasons (Condition Mismatch, Non-Responsive, Damaged Rental Return, Prohibited Goods). Creates a `Report` with status `OPEN`.

---

### [FR-013] Administrator Moderation & System Governance
- **Requirement ID:** FR-013
- **Module:** Administration
- **Priority:** Critical (P0)
- **Actor:** Administrator (`role == ADMIN`)
- **Workflow:** Admin investigates reports, can click "Dismiss" (sets status to `RESOLVED`) or "Delist Item" (sets listing status to `DELISTED`), or "Suspend User" (sets `AccountStatus` to `SUSPENDED`).

---

## 4. Non-Functional Requirements (NFR Specifications)

| ID | Category | Metric / Specification | Architectural Implementation |
|---|---|---|---|
| **NFR-001** | **Security** | Zero plain-text passwords; resistant to SQL Injection. | SHA-256 with institutional salt; 100% `PreparedStatement` parameterization. |
| **NFR-002** | **Transaction Atomicity** | Zero phantom bookings or orphaned transaction states. | Multi-table state transitions wrapped in `conn.setAutoCommit(false)`, `commit()`, and `rollback()`. |
| **NFR-003** | **Performance** | Catalog retrieval $< 100\text{ms}$ under 10,000 records. | Clustered primary keys; composite unique index on `(student_id, listing_id)`; indexes on `category_id`, `status`. |
| **NFR-004** | **Usability & Aesthetic** | High aesthetic appeal; responsive and intuitive; zero non-functional controls. | Gold / Sakura / Lavender custom themes; custom rounded anti-aliased Java Swing painters; visual toast notifications. |
| **NFR-005** | **Maintainability** | Decoupled presentation from business rules. | Strict MVC architecture; UI layer has zero SQL or business calculations; delegated to `Service` and `DAO` layers. |
| **NFR-006** | **Portability** | Runs on any standard JDK 17+ environment without commercial cloud dependencies. | Zero third-party proprietary frameworks; standard JDK libraries (`java.base`, `jdk.httpserver`, `java.desktop`). |

---

## 5. State Transition Matrices

### 5.1 Listing State Transition Matrix
| Current State | Event: Buy Requested | Event: Request Accepted | Event: Handover Complete | Event: Return Complete | Event: Admin Delist |
|---|---|---|---|---|---|
| **AVAILABLE** | AVAILABLE | RESERVED (Sale) / RENTED (Rent) | — | — | DELISTED |
| **RESERVED** | Ignored | — | SOLD | — | DELISTED |
| **RENTED** | Ignored | — | — | AVAILABLE | DELISTED |
| **SOLD** | Terminated | Terminated | Terminated | Terminated | Terminated |
| **DELISTED** | Terminated | Terminated | Terminated | Terminated | Terminated |

---

## 6. Milestone Acceptance Criteria

1. Every functional requirement (FR-001 through FR-013) has an explicit model class, database table, service method, and UI presentation component.
2. Every validation rule (e.g., non-negative pricing, valid rental windows, non-self purchasing) throws checked custom exceptions.
3. System compiles and runs out-of-the-box on localhost without external paid cloud dependencies.

