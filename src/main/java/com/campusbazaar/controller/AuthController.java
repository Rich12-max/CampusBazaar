package com.campusbazaar.controller;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.dto.ApiResponse;
import com.campusbazaar.model.Admin;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.User;
import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.model.enums.UserRole;
import com.campusbazaar.util.JsonUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST Controller for Student Authentication & Persona Role Management.
 */
public class AuthController {

    private final DataStore dataStore = DataStore.getInstance();
    private User activeUser;

    public AuthController() {
        // Default persona: Student Buyer Aarav Patel
        this.activeUser = dataStore.getUserById(1);
        if (this.activeUser == null) {
            this.activeUser = new Student(1, "Aarav Patel", "aarav@campus.edu", "pass", "9876543210",
                    AccountStatus.ACTIVE, "2023-CS-042", "Computer Science & Engg", "Hostel 4, Room 204", true, 4.9, 12);
        }
    }

    public String handleGetCurrentUser() {
        return ApiResponse.ok("Authenticated user profile", userToMap(activeUser)).toJson();
    }

    public String handleSwitchPersona(String jsonBody) {
        String roleStr = JsonUtil.getString(jsonBody, "role", "BUYER").toUpperCase();

        if ("SELLER".equals(roleStr)) {
            activeUser = dataStore.getUserById(2);
            if (activeUser == null) {
                activeUser = new Student(2, "Rohan Verma", "rohan@campus.edu", "pass", "9876543211",
                        AccountStatus.ACTIVE, "2024-ME-088", "Mechanical Engineering", "Hostel 2, Room 108", true, 4.8, 8);
            }
        } else if ("ADMIN".equals(roleStr)) {
            activeUser = dataStore.getUserById(4);
            if (activeUser == null) {
                activeUser = new Admin(4, "Prof. S. K. Mehta", "admin@college.edu", "pass", "9876543212",
                        AccountStatus.ACTIVE, "FAC-ADM-01", "Dean of Student Affairs");
            }
        } else {
            activeUser = dataStore.getUserById(1);
            if (activeUser == null) {
                activeUser = new Student(1, "Aarav Patel", "aarav@campus.edu", "pass", "9876543210",
                        AccountStatus.ACTIVE, "2023-CS-042", "Computer Science & Engg", "Hostel 4, Room 204", true, 4.9, 12);
            }
        }

        return ApiResponse.ok("Switched active persona", userToMap(activeUser)).toJson();
    }

    private Map<String, Object> userToMap(User u) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", u.getUserId());
        map.put("name", u.getFullName());
        map.put("email", u.getEmail());
        map.put("role", u.getRole().name());
        map.put("status", u.getStatus().name());

        if (u instanceof Student) {
            Student s = (Student) u;
            map.put("studentId", s.getStudentIdNumber());
            map.put("department", s.getDepartment());
            map.put("hostel", s.getHostelAddress());
            map.put("trustRating", s.getTrustRating());
            map.put("isVerified", s.isVerified());
        } else if (u instanceof Admin) {
            Admin a = (Admin) u;
            map.put("staffCode", a.getStaffCode());
            map.put("department", a.getDepartmentDesignation());
        }
        return map;
    }
}
