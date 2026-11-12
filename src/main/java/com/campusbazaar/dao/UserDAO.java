package com.campusbazaar.dao;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final DataStore dataStore = DataStore.getInstance();

    public User findByEmail(String email) {
        return dataStore.getUserByEmail(email);
    }

    public User findById(int userId) {
        return dataStore.getUserById(userId);
    }

    public List<User> findAll() {
        return new ArrayList<>(dataStore.getAllUsers());
    }

    public void save(User user) {
        dataStore.addUser(user);
    }
}

