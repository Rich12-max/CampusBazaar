package com.campusbazaar.service;

import com.campusbazaar.dao.UserDAO;
import com.campusbazaar.exception.AuthenticationException;
import com.campusbazaar.exception.ValidationException;
import com.campusbazaar.model.Admin;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.User;
import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.UserRole;
import com.campusbazaar.util.PasswordHasher;
import com.campusbazaar.util.ValidationUtil;

import java.util.Random;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private static User currentSessionUser;

    public User authenticate(String email, String rawPassword) throws AuthenticationException, ValidationException {
        ValidationUtil.validateEmail(email);
        ValidationUtil.requireNonEmpty(rawPassword, "Password");

        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new AuthenticationException("No account found with college email: " + email);
        }

        if (user.getStatus() == AccountStatus.SUSPENDED) {
            throw new AuthenticationException("Your account has been suspended by the campus moderator due to policy violations.");
        }

        if (!PasswordHasher.verify(rawPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Incorrect password. Please verify your credentials.");
        }

        currentSessionUser = user;
        return user;
    }

    public Student registerStudent(int userId, String fullName, String email, String rawPassword, String phone,
                                   String rollNumber, String department, String hostel)
            throws ValidationException, AuthenticationException {
        ValidationUtil.requireNonEmpty(fullName, "Full Name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.requireNonEmpty(rawPassword, "Password");
        ValidationUtil.requireNonEmpty(rollNumber, "Student Roll Number");
        ValidationUtil.requireNonEmpty(department, "Department");

        if (userDAO.findByEmail(email) != null) {
            throw new AuthenticationException("An account with email " + email + " is already registered.");
        }

        String hash = PasswordHasher.hash(rawPassword);
        Student student = new Student(userId, fullName, email, hash, phone,
                AccountStatus.ACTIVE, rollNumber, department, hostel, true, 5.0, 1);
        userDAO.save(student);
        currentSessionUser = student;
        return student;
    }

    public static User getCurrentUser() {
        return currentSessionUser;
    }

    public static void setCurrentUser(User user) {
        currentSessionUser = user;
    }

    public static void logout() {
        currentSessionUser = null;
    }

    public String generateVerificationCode(String email) {
        Random rnd = new Random();
        int code = 100000 + rnd.nextInt(900000);
        return String.valueOf(code);
    }
}

