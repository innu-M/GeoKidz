package com.countryquiz.controller;

import com.countryquiz.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameController {
    private final UserDatabase userDatabase;
    private User currentUser;
    private volatile boolean isLoggedIn = false; // Added volatile
    private List<Country> countries;

    public GameController() {
        this.userDatabase = new UserDatabase();
        this.countries = loadCountries();

        // For testing purposes, create a default user
        userDatabase.register(new User("test", "test"));
    }


    private List<Country> loadCountries() {
        try (InputStream is = getClass().getResourceAsStream("/data/countries.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Type listType = new TypeToken<ArrayList<Country>>(){}.getType();
            List<Country> countries = new Gson().fromJson(reader, listType);
            return countries != null ? countries : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error loading countries: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    public boolean register(String username, String password) {
        try {
            User newUser = new User(username, password);
            return userDatabase.register(newUser);
        } catch (IllegalArgumentException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean login(String username, String password) {
        Optional<User> user = userDatabase.login(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            isLoggedIn = true;
            System.out.println("Login successful - User set: " + currentUser.getUsername());
            return true;
        }
        return false;
    }



    public synchronized void logout() {
        currentUser = null;
        isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void updateUserProgress(int level, int score) {
        if (currentUser == null) return;
        try {
            currentUser.setLevelScore(level, score);
            if (level == currentUser.getHighestLevelUnlocked() && level < 5) {
                currentUser.setHighestLevelUnlocked(level + 1);
            }
            userDatabase.updateUser(currentUser);
        } catch (IllegalArgumentException e) {
            System.err.println("Progress update error: " + e.getMessage());
        }
    }

    public synchronized User getCurrentUser() {
        if (!isLoggedIn) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return currentUser;
    }
    public List<Country> getCountries() {
        return new ArrayList<>(countries); // Return defensive copy
    }

    public void unlockLevel(int i) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        if (i < 1 || i > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5");
        }
        if (i > currentUser.getHighestLevelUnlocked()) {
            currentUser.setHighestLevelUnlocked(i);
            userDatabase.updateUser(currentUser);
        }
    }
}