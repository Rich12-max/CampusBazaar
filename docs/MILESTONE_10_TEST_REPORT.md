# 🎓 Campus Peer-to-Peer Marketplace (CampusBazaar)
## Milestone 10: Comprehensive Test Plan & Verification Report
**Course:** Object-Oriented Programming Using Java  
**Test Suite:** [`com.campusbazaar.test.CampusMarketplaceTestSuite`](file:///c:/Users/Richa%20Sharma/Documents/java/src/main/java/com/campusbazaar/test/CampusMarketplaceTestSuite.java)  
**Execution Status:** ✅ 37 PASSED | 0 FAILED (100% Success Rate)  

---

## 1. Test Strategy & Scope
The verification process for CampusBazaar encompasses:
1. **Core OOP Verification:** Polymorphic dynamic method dispatch, inheritance integrity across `SaleListing`, `RentalListing`, and `BarterListing`, interface contract conformance (`Searchable`, `Rentable`, `Reviewable`, `Reportable`), and encapsulation boundary checks.
2. **Algorithmic Validation:** Micro-rental duration and late return penalty formula verification; rule-based depreciation pricing engine accuracy.
3. **Security & Cryptography:** SHA-256 password salting, hash length validation, digital clearance signature computation, and 6-digit OTP verification PIN generation.
4. **Input Validation & Edge Cases:** Malformed email rejections, negative currency prevention, invalid inverted rental windows, and negotiation limit constraints.
5. **State Transition Integrity:** Lifecycle integrity across Purchase Requests, Handover Appointments, Barter Proposals, Price Negotiations, and Listing state machines (`AVAILABLE` $\rightarrow$ `RESERVED` $\rightarrow$ `SOLD`).

---

## 2. Test Execution Results Matrix

| Test ID | Module / Concept | Test Scenario | Input Data | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| **TC-01.1** | OOP Polymorphism | `SaleListing.getEffectivePrice()` | Selling Price = 450.0 | Resolves to 450.0 | 450.0 | **PASS** |
| **TC-01.2** | OOP Polymorphism | `RentalListing.getEffectivePrice()` | Daily Rate = 35.0 | Resolves to 35.0 | 35.0 | **PASS** |
| **TC-01.3** | Runtime Polymorphism | Dynamic dispatch via abstract `Listing` reference | Heterogeneous list | Distinct behaviors | Verified | **PASS** |
| **TC-02.1** | Rental Engine | Standard rental charges | 5 days @ ₹40/day | ₹200.0 | ₹200.0 | **PASS** |
| **TC-02.2** | Late Penalty Engine | Overdue penalty calculation | 3 days overdue @ ₹25/day | ₹75.0 penalty | ₹75.0 | **PASS** |
| **TC-02.3** | Rental Engine | On-time return penalty | 0 days overdue | ₹0.0 penalty | ₹0.0 | **PASS** |
| **TC-03.1** | Smart Pricing | Fair target computation | Book, Orig: 1000, Like New | Target ~ ₹510 | ₹510.0 | **PASS** |
| **TC-03.2** | Smart Pricing | Price lower bound | Fair: ₹510 | Min < Fair | Verified | **PASS** |
| **TC-03.3** | Smart Pricing | Price upper bound | Fair: ₹510 | Max > Fair | Verified | **PASS** |
| **TC-04.1** | Cryptography | SHA-256 Hash Length | "StudentPass@2026" | 64 hex characters | 64 chars | **PASS** |
| **TC-04.2** | Cryptography | Password Verification | Valid password | Match = true | true | **PASS** |
| **TC-04.3** | Cryptography | Tampered Password | "WrongPass" | Match = false | false | **PASS** |
| **TC-05.1** | Input Validation | Invalid email format | "invalid-email-format" | `ValidationException` | Caught | **PASS** |
| **TC-05.2** | Input Validation | Negative product price | Price = -50.0 | `ValidationException` | Caught | **PASS** |
| **TC-05.3** | Validation & Edge Case | Inverted rental dates | Start date > Return date | `InvalidRentalDurationException` | Caught | **PASS** |
| **TC-06.1** | State Machine | Buy Request creation | Request submitted | Status = PENDING | PENDING | **PASS** |
| **TC-06.2** | State Machine | Seller acceptance | Request accepted | Status = ACCEPTED | ACCEPTED | **PASS** |
| **TC-06.3** | State Machine | Listing reservation | Request accepted | Listing = RESERVED | RESERVED | **PASS** |
| **TC-06.4** | State Machine | Handover finalization | Handover confirmed | Request = COMPLETED | COMPLETED | **PASS** |
| **TC-06.5** | State Machine | Listing closure | Transaction settled | Listing = SOLD | SOLD | **PASS** |
| **TC-06.6** | Transaction Audit | Unique code generation | Purchase transaction | Immutable audit record | Code verified | **PASS** |
| **TC-07.1** | Barter & Swap | `BarterListing` instantiation | Type = `BARTER` | Polymorphic listing | Type `BARTER` | **PASS** |
| **TC-07.2** | Barter & Swap | Barter display string | `preferredExchangeItem` | Contains "SWAP" prefix | Verified | **PASS** |
| **TC-07.3** | Barter & Swap | Swap proposal submission | `proposeSwap(...)` | Status = PENDING | PENDING | **PASS** |
| **TC-07.4** | Barter & Swap | Proposal acceptance | `acceptProposal(...)` | Status = ACCEPTED | ACCEPTED | **PASS** |
| **TC-07.5** | Barter & Swap | Listing transition on swap | Proposal accepted | Listing = RESERVED | RESERVED | **PASS** |
| **TC-08.1** | Safe Handover | Appointment creation | Valid campus spot & time | Status = SCHEDULED | SCHEDULED | **PASS** |
| **TC-08.2** | Safe Handover | OTP Generation | Secure PIN creation | 6 numeric digits | 6 digits verified | **PASS** |
| **TC-08.3** | Safe Handover | Incorrect PIN rejection | Wrong PIN = "000000" | Completion rejected | Rejection verified | **PASS** |
| **TC-08.4** | Safe Handover | Correct PIN verification | Matching OTP PIN | Verified & completed | Verified | **PASS** |
| **TC-08.5** | Safe Handover | State transition | PIN matched | Status = VERIFIED_COMPLETED | VERIFIED_COMPLETED | **PASS** |
| **TC-09.1** | Price Bargaining | Negotiation initialization | Buyer initial offer | Round 1, PENDING_SELLER | Verified | **PASS** |
| **TC-09.2** | Price Bargaining | Counter-Offer mechanism | Seller counters price | Round 2, COUNTERED_BY_SELLER | Verified | **PASS** |
| **TC-09.3** | Price Bargaining | Negotiation agreement | Buyer accepts counter | Status = ACCEPTED | ACCEPTED | **PASS** |
| **TC-10.1** | Digital Receipts | Receipt retrieval | Serial "CB-REC-2026-0042" | Valid receipt object | Retrieved | **PASS** |
| **TC-10.2** | Digital Receipts | SHA-256 Digital Signature | Certificate parameters | Cryptographic hash | $\ge$ 16 hex chars | **PASS** |
| **TC-10.3** | Digital Receipts | Authority validation | Printable slip | Issuing authority text | Verified | **PASS** |

---

## 3. How to Run Test Suite
From the terminal, run:
```powershell
powershell -ExecutionPolicy Bypass -File compile.ps1
java -cp bin com.campusbazaar.test.CampusMarketplaceTestSuite
```
Or execute through standard IDE test runners.

