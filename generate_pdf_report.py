# -*- coding: utf-8 -*-
"""
generate_pdf_report.py
Generates Campus_Peer_to_Peer_Marketplace_Project_Report.pdf using ReportLab.
"""

import os
from reportlab.lib.pagesizes import A4
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, Preformatted
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib import colors
from reportlab.lib.units import inch

def build_pdf():
    pdf_filename = "Campus_Peer_to_Peer_Marketplace_Project_Report.pdf"
    doc = SimpleDocTemplate(
        pdf_filename,
        pagesize=A4,
        leftMargin=0.75 * inch,
        rightMargin=0.75 * inch,
        topMargin=0.75 * inch,
        bottomMargin=0.75 * inch
    )

    styles = getSampleStyleSheet()

    # Custom styles
    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=24,
        leading=28,
        textColor=colors.HexColor('#1E3A8A'),
        alignment=1, # Center
        spaceAfter=12
    )

    subtitle_style = ParagraphStyle(
        'DocSubTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Oblique',
        fontSize=12,
        leading=16,
        textColor=colors.HexColor('#475569'),
        alignment=1, # Center
        spaceAfter=24
    )

    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=15,
        leading=18,
        textColor=colors.HexColor('#1E3A8A'),
        spaceBefore=16,
        spaceAfter=8,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=colors.HexColor('#2563EB'),
        spaceBefore=12,
        spaceAfter=6,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=colors.HexColor('#1E293B'),
        spaceAfter=6
    )

    bullet_style = ParagraphStyle(
        'Bullet_Custom',
        parent=body_style,
        leftIndent=15,
        firstLineIndent=-10,
        spaceAfter=4
    )

    code_style = ParagraphStyle(
        'Code_Custom',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=7.5,
        leading=9.5,
        textColor=colors.HexColor('#0F172A')
    )

    story = []

    # COVER PAGE
    story.append(Spacer(1, 40))
    story.append(Paragraph("CAMPUS PEER-TO-PEER MARKETPLACE<br/>(\"CAMPUSBAZAAR\")", title_style))
    story.append(Paragraph("A Specialized Institutional Resource Sharing & Micro-Rental Platform for College Students", subtitle_style))
    story.append(Spacer(1, 20))

    story.append(Paragraph("<i>A Capstone Project Report Submitted in Partial Fulfillment of the Requirements for the Award of the Degree of</i>", body_style))
    story.append(Paragraph("<b>BACHELOR OF TECHNOLOGY / BACHELOR OF ENGINEERING IN COMPUTER SCIENCE & ENGINEERING</b>", body_style))
    story.append(Paragraph("<b>Course:</b> Object-Oriented Programming Using Java (Core Java, MVC Architecture & MySQL)", body_style))
    story.append(Spacer(1, 25))

    story.append(Paragraph("<b>SUBMITTED BY:</b><br/>"
                           "1. Aarav Patel (Roll No: 2023-CS-042) [Student Lead & Architect]<br/>"
                           "2. Rohan Verma (Roll No: 2024-ME-088) [Domain & Database Lead]<br/>"
                           "3. Priya Sharma (Roll No: 2023-IT-019) [UI/UX & Testing Lead]", body_style))
    story.append(Spacer(1, 25))

    story.append(Paragraph("<b>UNDER THE SUPERVISION OF:</b><br/>"
                           "Dr. Rajesh Kumar, Ph.D., Senior Professor & Academic Guide<br/>"
                           "Department of Computer Science & Engineering<br/>"
                           "Academic Year: 2025 – 2026", body_style))
    story.append(PageBreak())

    # CERTIFICATE
    story.append(Paragraph("CERTIFICATE OF PROJECT COMPLETION", h1_style))
    story.append(Paragraph(
        "This is to certify that the project entitled <b>\"CAMPUS PEER-TO-PEER MARKETPLACE (CampusBazaar)\"</b> "
        "is a bonafide work carried out by the student team in partial fulfillment of the requirements for "
        "the academic evaluation of Object-Oriented Programming Using Java in the Department of Computer Science "
        "and Engineering during the academic session 2025–2026.", body_style
    ))
    story.append(Paragraph(
        "The project demonstrates sound software engineering principles, rigorous application of Core Java Object-Oriented "
        "Programming paradigms (Encapsulation, Inheritance, Polymorphism, Abstraction, and Interface Segregation), robust relational "
        "database modeling in MySQL using JDBC PreparedStatement, an MVC architectural separation, and an automated verification "
        "suite covering unit, integration, and algorithmic correctness.", body_style
    ))
    story.append(Spacer(1, 40))
    story.append(Paragraph(
        "__________________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; __________________________<br/>"
        "Internal Project Guide &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Head of Department (CSE/IT)<br/>"
        "Dr. Rajesh Kumar, Ph.D. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Prof. S. N. Deshmukh, Ph.D.<br/><br/><br/>"
        "__________________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; __________________________<br/>"
        "External Examiner 1 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; External Examiner 2<br/>"
        "Date: November 12, 2026 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Institutional Seal", body_style
    ))
    story.append(PageBreak())

    # ABSTRACT
    story.append(Paragraph("EXECUTIVE ABSTRACT", h1_style))
    story.append(Paragraph(
        "Within university campuses worldwide, tertiary students face severe cyclical financial outlays at the beginning of each "
        "academic semester. Expensive textbooks, scientific calculators, laboratory drawing tools (mini-drafters), hostel appliances, "
        "and bicycles represent high initial investments. Once a semester concludes, these valuable assets sit idle in dormitory rooms, "
        "while incoming junior cohorts purchase identical items from off-campus retail vendors at full price. Existing informal "
        "channels (e.g., WhatsApp and Telegram groups) lack trust, discoverability, rental tracking, and payment accountability, while "
        "generic classifieds (e.g., OLX, Quikr) fail due to lack of student verification, stranger danger, and logistical distance.", body_style
    ))
    story.append(Paragraph(
        "To solve this problem, we present CampusBazaar — an institutional, closed-loop peer-to-peer e-commerce and micro-rental platform "
        "engineered exclusively for university students and campus personnel. Developed in Core Java (JDK 17+) strictly adhering to an "
        "academic Model-View-Controller (MVC) architecture, the system incorporates: (1) Zero-Trust Student Identity Verification; "
        "(2) Polymorphic Resource Listings supporting outright peer sales, daily micro-rentals, and direct barter swaps; "
        "(3) An Algorithmic Rental Engine that calculates exact day durations and late-return penalty deductions from security deposits; "
        "(4) A Rule-Based Smart Pricing Engine that evaluates category depreciation and condition retention to prevent price gouging; "
        "(5) Safe Campus Landmark Handovers verified via cryptographic 6-digit OTP PINs; (6) Multi-Round Counter-Offer Price Bargaining; "
        "(7) Official Digital Clearance Receipts carrying SHA-256 integrity verification hashes for administrative hostel clearance; and "
        "(8) A Dual Presentation Layer comprising an interactive Java Swing Desktop GUI and an embedded zero-dependency Web Portal "
        "(http://localhost:8080) featuring real-time aesthetic theme customization across 8 harmonious color palettes.", body_style
    ))
    story.append(Paragraph(
        "The system's persistence layer features a production-ready 15-table relational schema in MySQL 8.0 integrated via JDBC "
        "PreparedStatement (ensuring SQL injection immunity), alongside a thread-safe in-memory Collections DataStore enabling instant "
        "zero-setup evaluation. The implementation is verified by an automated test suite comprising 37 comprehensive test cases "
        "achieving a 100% pass rate (37 PASSED | 0 FAILED). The resulting platform delivers a secure, sustainable circular economy for "
        "campuses, significantly lowering educational costs while demonstrating exemplary Java OOP engineering rigor.", body_style
    ))
    story.append(PageBreak())

    # CHAPTER 1
    story.append(Paragraph("CHAPTER 1: INTRODUCTION", h1_style))
    story.append(Paragraph("<b>1.1 Project Overview</b><br/>"
                           "CampusBazaar is an institutional peer-to-peer commerce and resource-sharing platform engineered in Core Java. "
                           "It establishes a trusted digital circular economy exclusively for university students and campus residents.", body_style))
    story.append(Paragraph("<b>1.2 The Campus Dilemma</b><br/>"
                           "Students face recurring, sharp semester expenses for academic textbooks, calculators, mini-drafters, and cycles. "
                           "These items become dormant post-exams while junior cohorts repurchase them at retail prices.", body_style))
    story.append(Paragraph("<b>1.3 Academic Objectives</b><br/>"
                           "• Master Java Object-Oriented Programming (Inheritance, Polymorphism, Abstraction, Encapsulation, Interface Segregation).<br/>"
                           "• Implement an MVC + DAO architectural pattern with zero external framework overkill.<br/>"
                           "• Build mathematical engines for micro-rental penalty settlement and rule-based smart pricing.<br/>"
                           "• Achieve 100% automated test coverage verified across 37 test cases.", body_style))
    story.append(PageBreak())

    # CHAPTER 2
    story.append(Paragraph("CHAPTER 2: EXISTING SYSTEM VS PROPOSED SYSTEM", h1_style))
    story.append(Paragraph("<b>Comparative Analysis Matrix</b>", h2_style))
    
    comp_table_data = [
        [Paragraph("<b>Parameter</b>", body_style), Paragraph("<b>Informal (WhatsApp)</b>", body_style), Paragraph("<b>Generic (OLX)</b>", body_style), Paragraph("<b>CampusBazaar</b>", body_style)],
        [Paragraph("Verification", body_style), Paragraph("None", body_style), Paragraph("Open Public", body_style), Paragraph("Institutional Roll Verified", body_style)],
        [Paragraph("Discoverability", body_style), Paragraph("Unstructured Chat", body_style), Paragraph("Metro-Level Search", body_style), Paragraph("Structured Campus Catalog", body_style)],
        [Paragraph("Rental Engine", body_style), Paragraph("None / Friction", body_style), Paragraph("Not Supported", body_style), Paragraph("Algorithmic Late Penalty Math", body_style)],
        [Paragraph("Handovers", body_style), Paragraph("Unregulated", body_style), Paragraph("Risky Courier/Meet", body_style), Paragraph("Designated Spots + 6-Digit OTP", body_style)],
        [Paragraph("Receipts", body_style), Paragraph("None", body_style), Paragraph("Basic Invoice", body_style), Paragraph("SHA-256 Clearance Vouchers", body_style)],
        [Paragraph("Architecture", body_style), Paragraph("Black-box", body_style), Paragraph("Closed Web", body_style), Paragraph("Core Java OOP (MVC + DAO)", body_style)]
    ]
    t = Table(comp_table_data, colWidths=[1.1*inch, 1.4*inch, 1.4*inch, 2.1*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#1E3A8A')),
        ('TEXTCOLOR', (0,0), (-1,0), colors.whitesmoke),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CBD5E1')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor('#F8FAFC')]),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)
    story.append(PageBreak())

    # CHAPTER 6 & 9 (OOP & TESTING HIGHLIGHTS)
    story.append(Paragraph("CHAPTER 6: OBJECT-ORIENTED PROGRAMMING IMPLEMENTATION", h1_style))
    story.append(Paragraph(
        "CampusBazaar demonstrates concrete Core Java OOP paradigms:<br/>"
        "• <b>Encapsulation:</b> Private fields with validated getters/setters preventing negative prices and invalid rental bounds.<br/>"
        "• <b>Inheritance:</b> Abstract class <code>Listing</code> specialized by <code>SaleListing</code>, <code>RentalListing</code>, and <code>BarterListing</code>.<br/>"
        "• <b>Polymorphism:</b> Dynamic method dispatch via <code>getEffectivePrice()</code> resolved at runtime across heterogeneous collections.<br/>"
        "• <b>Interface Segregation:</b> Decoupled behavioral contracts: <code>Searchable</code>, <code>Rentable</code>, <code>Reviewable</code>, <code>Reportable</code>.<br/>"
        "• <b>Custom Exceptions:</b> <code>CampusMarketplaceException</code>, <code>ValidationException</code>, <code>InvalidRentalDurationException</code>.<br/>"
        "• <b>Dual Persistence:</b> Production MySQL 8.0 with automated fallback to in-memory concurrent <code>DataStore</code>.", body_style
    ))
    story.append(Spacer(1, 15))

    story.append(Paragraph("CHAPTER 9: AUTOMATED TEST VERIFICATION (37 TESTS)", h1_style))
    story.append(Paragraph("<b>Verification Result:</b> ✅ 37 PASSED | 0 FAILED (100% Success Rate)", body_style))

    test_table_data = [
        [Paragraph("<b>Test ID</b>", body_style), Paragraph("<b>Module / Scenario</b>", body_style), Paragraph("<b>Expected Output</b>", body_style), Paragraph("<b>Status</b>", body_style)],
        [Paragraph("TC-01.1", body_style), Paragraph("SaleListing.getEffectivePrice()", body_style), Paragraph("Resolves selling price 450.0", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-01.2", body_style), Paragraph("RentalListing.getEffectivePrice()", body_style), Paragraph("Resolves daily rate 35.0", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-01.3", body_style), Paragraph("Dynamic Polymorphic Dispatch", body_style), Paragraph("Resolved via abstract Listing ref", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-02.1", body_style), Paragraph("Rental standard charge (5 days @ 40)", body_style), Paragraph("Total charge = ₹200.0", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-02.2", body_style), Paragraph("Rental late penalty (3 days overdue @ 25)", body_style), Paragraph("Late fee ₹75.0 deducted from deposit", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-03.1", body_style), Paragraph("Smart Pricing target estimation", body_style), Paragraph("Target price ~ ₹510.0", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-04.1", body_style), Paragraph("SHA-256 Hash Length check", body_style), Paragraph("Exactly 64 hexadecimal characters", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-05.1", body_style), Paragraph("Invalid email syntax check", body_style), Paragraph("Throws ValidationException", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-05.3", body_style), Paragraph("Inverted rental date bounds check", body_style), Paragraph("Throws InvalidRentalDurationException", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-08.2", body_style), Paragraph("Safe Handover OTP PIN creation", body_style), Paragraph("Generates 6-digit numeric PIN", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-08.4", body_style), Paragraph("Correct Handover PIN verification", body_style), Paragraph("Handover status = VERIFIED_COMPLETED", body_style), Paragraph("<b>PASS</b>", body_style)],
        [Paragraph("TC-10.2", body_style), Paragraph("Clearance slip digital signature", body_style), Paragraph("SHA-256 hash generated (>= 16 chars)", body_style), Paragraph("<b>PASS</b>", body_style)]
    ]

    t_tests = Table(test_table_data, colWidths=[0.9*inch, 2.5*inch, 2.1*inch, 0.8*inch])
    t_tests.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#1E3A8A')),
        ('TEXTCOLOR', (0,0), (-1,0), colors.whitesmoke),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CBD5E1')),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor('#F8FAFC')]),
        ('TOPPADDING', (0,0), (-1,-1), 3),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3),
    ]))
    story.append(t_tests)
    story.append(Spacer(1, 15))

    story.append(Paragraph("<b>Full 37/37 automated test suite logs and details available in docx report and docs/MILESTONE_10_TEST_REPORT.md.</b>", body_style))

    doc.build(story)
    print(f"✅ Generated Companion PDF: {pdf_filename}")

if __name__ == "__main__":
    build_pdf()

