package com.campusbazaar.model;

import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.UserRole;

/**
 * Demonstrates Inheritance.
 * Represents university administration or student council moderator.
 */
public class Admin extends User {
    private String staffCode;
    private String departmentDesignation;

    public Admin(int userId, String fullName, String email, String passwordHash, String phone,
                 AccountStatus status, String staffCode, String departmentDesignation) {
        super(userId, fullName, email, passwordHash, phone, UserRole.ADMIN, status);
        this.staffCode = staffCode;
        this.departmentDesignation = departmentDesignation;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getDepartmentDesignation() {
        return departmentDesignation;
    }

    public void setDepartmentDesignation(String departmentDesignation) {
        this.departmentDesignation = departmentDesignation;
    }

    @Override
    public String getRoleSpecificDetails() {
        return "Staff Code: " + staffCode + " | Designation: " + departmentDesignation;
    }
}

