-- =========================================================================
-- CAMPUS PEER-TO-PEER MARKETPLACE ("CAMPUSBAZAAR")
-- Complete MySQL Relational Database Schema & Seed Data Script
-- Academic Course: Object-Oriented Programming Using Java
-- =========================================================================

CREATE DATABASE IF NOT EXISTS campus_bazaar_db;
USE campus_bazaar_db;

-- 1. Master Users Table
DROP TABLE IF EXISTS clearance_receipts;
DROP TABLE IF EXISTS price_negotiations;
DROP TABLE IF EXISTS handover_schedules;
DROP TABLE IF EXISTS barter_proposals;
DROP TABLE IF EXISTS barter_details;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS wishlists;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS rental_records;
DROP TABLE IF EXISTS purchase_requests;
DROP TABLE IF EXISTS rental_details;
DROP TABLE IF EXISTS sale_details;
DROP TABLE IF EXISTS listings;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS student_profiles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    account_status ENUM('ACTIVE', 'PENDING_VERIFICATION', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Student Extended Profiles Table (1-to-1 with users)
CREATE TABLE student_profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    student_id_number VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    hostel_address VARCHAR(150) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT TRUE,
    trust_score DECIMAL(3,2) NOT NULL DEFAULT 5.00,
    total_reviews INT NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Categories Table
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    icon_key VARCHAR(50) NOT NULL,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Products Table (Composition in Listings)
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    condition_grade ENUM('BRAND_NEW', 'LIKE_NEW', 'GOOD', 'FAIR') NOT NULL DEFAULT 'GOOD',
    brand VARCHAR(100),
    original_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_product_cat (category_id),
    INDEX idx_product_seller (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Listings Table (Base abstraction in DB)
CREATE TABLE listings (
    listing_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    seller_id INT NOT NULL,
    listing_type ENUM('SALE', 'RENTAL', 'BARTER') NOT NULL,
    status ENUM('AVAILABLE', 'RESERVED', 'RENTED', 'SOLD', 'DELISTED') NOT NULL DEFAULT 'AVAILABLE',
    report_count INT NOT NULL DEFAULT 0,
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_listing_status (status),
    INDEX idx_listing_type (listing_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Sale Specific Listing Details
CREATE TABLE sale_details (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL UNIQUE,
    selling_price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2) NOT NULL,
    is_negotiable BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Rental Specific Listing Details
CREATE TABLE rental_details (
    rental_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL UNIQUE,
    daily_rate DECIMAL(10,2) NOT NULL,
    security_deposit DECIMAL(10,2) NOT NULL,
    min_rental_days INT NOT NULL DEFAULT 1,
    max_rental_days INT NOT NULL DEFAULT 30,
    late_fee_per_day DECIMAL(10,2) NOT NULL DEFAULT 20.00,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7b. Barter Specific Listing Details
CREATE TABLE barter_details (
    barter_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL UNIQUE,
    preferred_exchange_item VARCHAR(255) NOT NULL,
    cash_adjustment DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_negotiable BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7c. Barter Proposals Table
CREATE TABLE barter_proposals (
    proposal_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    proposer_id INT NOT NULL,
    offered_item_title VARCHAR(200) NOT NULL,
    offered_item_condition ENUM('BRAND_NEW', 'LIKE_NEW', 'GOOD', 'FAIR') NOT NULL,
    offered_description TEXT,
    cash_adjustment DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'WITHDRAWN') NOT NULL DEFAULT 'PENDING',
    seller_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (proposer_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Purchase Requests Table
CREATE TABLE purchase_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    buyer_id INT NOT NULL,
    offered_price DECIMAL(10,2) NOT NULL,
    proposed_spot VARCHAR(150) NOT NULL,
    buyer_notes TEXT,
    status ENUM('PENDING', 'ACCEPTED', 'DECLINED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8b. Campus Safe Handover Schedules Table
CREATE TABLE handover_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    campus_spot VARCHAR(150) NOT NULL,
    scheduled_time VARCHAR(100) NOT NULL,
    verification_pin VARCHAR(10) NOT NULL,
    status ENUM('SCHEDULED', 'VERIFIED_COMPLETED', 'CANCELLED', 'MISSED') NOT NULL DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8c. Price Negotiations (Counter-Offer) Table
CREATE TABLE price_negotiations (
    negotiation_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    current_offer DECIMAL(10,2) NOT NULL,
    last_offered_by INT NOT NULL,
    round_number INT NOT NULL DEFAULT 1,
    status ENUM('PENDING_SELLER', 'COUNTERED_BY_SELLER', 'ACCEPTED', 'DECLINED', 'EXPIRED') NOT NULL DEFAULT 'PENDING_SELLER',
    buyer_note TEXT,
    seller_note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (last_offered_by) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Rental Records (Rental Agreements) Table
CREATE TABLE rental_records (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    borrower_id INT NOT NULL,
    lender_id INT NOT NULL,
    start_date DATE NOT NULL,
    expected_end_date DATE NOT NULL,
    actual_return_date DATE NULL,
    daily_rate DECIMAL(10,2) NOT NULL,
    security_deposit DECIMAL(10,2) NOT NULL,
    late_fee_calculated DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    final_amount_paid DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    deposit_refunded DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status ENUM('REQUESTED', 'ACTIVE', 'RETURNED', 'OVERDUE', 'DISPUTED') NOT NULL DEFAULT 'REQUESTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (borrower_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (lender_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Transactions Audit Table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_code VARCHAR(50) NOT NULL UNIQUE,
    listing_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    transaction_type ENUM('PURCHASE', 'RENTAL') NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('CASH_ON_HANDOVER', 'UPI_SIMULATED') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'COMPLETED',
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10b. Official Clearance Receipts Table
CREATE TABLE clearance_receipts (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    transaction_code VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    item_title VARCHAR(200) NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    seller_name VARCHAR(100) NOT NULL,
    seller_roll_no VARCHAR(30) NOT NULL,
    seller_department VARCHAR(100) NOT NULL,
    buyer_name VARCHAR(100) NOT NULL,
    buyer_roll_no VARCHAR(30) NOT NULL,
    buyer_department VARCHAR(100) NOT NULL,
    base_amount DECIMAL(10,2) NOT NULL,
    deposit_refunded DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    late_fee_deducted DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    final_net_paid DECIMAL(10,2) NOT NULL,
    handover_spot VARCHAR(150) NOT NULL,
    verification_hash VARCHAR(64) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Wishlists Table
CREATE TABLE wishlists (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    listing_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_listing (student_id, listing_id),
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Reviews Table
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    reviewer_id INT NOT NULL,
    reviewee_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewee_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 13. Reports Table
CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    reporter_id INT NOT NULL,
    reported_user_id INT NULL,
    reported_listing_id INT NULL,
    reason VARCHAR(100) NOT NULL,
    description TEXT,
    status ENUM('OPEN', 'UNDER_INVESTIGATION', 'RESOLVED', 'DISMISSED') NOT NULL DEFAULT 'OPEN',
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reported_user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (reported_listing_id) REFERENCES listings(listing_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 14. Messages Table
CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (listing_id) REFERENCES listings(listing_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================================
-- SEED SAMPLE DATA (Realistic Campus Personas & Items)
-- =========================================================================

-- Insert Categories
INSERT INTO categories (category_id, category_name, icon_key, description) VALUES
(1, 'Academic Books', 'book', 'Engineering, Medical, Law and Core Textbooks & Question Banks'),
(2, 'Calculators & Tech', 'cpu', 'Scientific calculators, keyboards, monitors, and adaptors'),
(3, 'Lab Equipment', 'flask', 'Drafters, lab coats, dissection sets, and chemistry aprons'),
(4, 'Hostel Essentials', 'home', 'Kettles, mattress, study lamps, table fans, and storage racks'),
(5, 'Cycles & Sports', 'bike', 'Bicycles, badminton rackets, cricket bats, and gym gear'),
(6, 'Stationery & Notes', 'edit', 'Handwritten topper notes, reference guides, and engineering sheets');

-- Sample Passwords: 'password123' hashed with SHA-256 + salt
-- (User password hash matching our PasswordHasher algorithm)
INSERT INTO users (user_id, full_name, email, password_hash, phone, role, account_status) VALUES
(1, 'Aarav Patel', 'aarav.patel@college.edu', 'a9b2c3d4e5f6', '+91 9876543210', 'STUDENT', 'ACTIVE'),
(2, 'Priya Sharma', 'priya.sharma@college.edu', 'a9b2c3d4e5f6', '+91 9876543211', 'STUDENT', 'ACTIVE'),
(3, 'Rohan Verma', 'rohan.verma@college.edu', 'a9b2c3d4e5f6', '+91 9876543212', 'STUDENT', 'ACTIVE'),
(4, 'Prof. S. K. Mehta (Admin)', 'admin@college.edu', 'a9b2c3d4e5f6', '+91 9876543200', 'ADMIN', 'ACTIVE');

INSERT INTO student_profiles (user_id, student_id_number, department, hostel_address, is_verified, trust_score, total_reviews) VALUES
(1, '2023-CS-042', 'Computer Science & Engg', 'Aryabhata Hostel, Room 304', TRUE, 4.90, 8),
(2, '2022-EE-115', 'Electrical Engineering', 'Gargi Hall of Residence, Room 112', TRUE, 5.00, 12),
(3, '2024-ME-088', 'Mechanical Engineering', 'Kalam Hostel, Room 201', TRUE, 4.60, 5);

