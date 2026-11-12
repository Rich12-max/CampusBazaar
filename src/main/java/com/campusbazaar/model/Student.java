package com.campusbazaar.model;

import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.UserRole;
import com.campusbazaar.model.interfaces.Reportable;
import com.campusbazaar.model.interfaces.Reviewable;

/**
 * Demonstrates Inheritance & Multiple Interface Implementation.
 * Represents a registered college student participating as buyer, seller, or borrower.
 */
public class Student extends User implements Reviewable, Reportable {
    private String studentIdNumber;
    private String department;
    private String hostelAddress;
    private boolean isVerified;
    private double trustRating;
    private int totalReviews;
    private int reportCount;

    public Student(int userId, String fullName, String email, String passwordHash, String phone,
                   AccountStatus status, String studentIdNumber, String department, String hostelAddress,
                   boolean isVerified, double trustRating, int totalReviews) {
        super(userId, fullName, email, passwordHash, phone, UserRole.STUDENT, status);
        this.studentIdNumber = studentIdNumber;
        this.department = department;
        this.hostelAddress = hostelAddress;
        this.isVerified = isVerified;
        this.trustRating = trustRating;
        this.totalReviews = totalReviews;
        this.reportCount = 0;
    }

    public String getStudentIdNumber() {
        return studentIdNumber;
    }

    public void setStudentIdNumber(String studentIdNumber) {
        this.studentIdNumber = studentIdNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHostelAddress() {
        return hostelAddress;
    }

    public void setHostelAddress(String hostelAddress) {
        this.hostelAddress = hostelAddress;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public void addRatingScore(int stars) {
        double currentTotal = this.trustRating * this.totalReviews;
        this.totalReviews++;
        this.trustRating = (currentTotal + stars) / this.totalReviews;
    }

    @Override
    public double getTrustRating() {
        return trustRating;
    }

    public void setTrustRating(double trustRating) {
        this.trustRating = trustRating;
    }

    @Override
    public int getTotalReviewsCount() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    @Override
    public void incrementReportCount() {
        this.reportCount++;
    }

    @Override
    public int getReportCount() {
        return reportCount;
    }

    @Override
    public boolean isFlaggedForReview() {
        return reportCount >= 3;
    }

    @Override
    public String getRoleSpecificDetails() {
        return "Roll No: " + studentIdNumber + " | Dept: " + department + " | Hostel: " + hostelAddress +
                " | Verified: " + (isVerified ? "Yes" : "No") + " | Trust: " + String.format("%.1f", trustRating) + " (" + totalReviews + " reviews)";
    }
}

