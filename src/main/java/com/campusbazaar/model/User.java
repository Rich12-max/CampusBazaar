package com.campusbazaar.model;

import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Demonstrates Abstraction & Encapsulation.
 * Abstract base class representing all platform participants.
 */
public abstract class User {
    protected int userId;
    protected String fullName;
    protected String email;
    protected String passwordHash;
    protected String phone;
    protected UserRole role;
    protected AccountStatus status;
    protected LocalDateTime createdAt;

    public User(int userId, String fullName, String email, String passwordHash, String phone, UserRole role, AccountStatus status) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public abstract String getRoleSpecificDetails();

    @Override
    public String toString() {
        return "User #" + userId + " [" + fullName + " (" + email + "), Role: " + role + "]";
    }
}

