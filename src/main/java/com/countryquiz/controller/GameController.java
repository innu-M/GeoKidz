package com.countryquiz.controller;

import com.countryquiz.model.*;
import com.countryquiz.view.panel.LeaderboardPanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private final UserDatabase userDatabase;
    private User currentUser;
    private volatile boolean isLoggedIn = false; // Added volatile
    private List<Country> countries;

    public GameController() {
        this.userDatabase = new UserDatabase();
        this.countries = loadCountries();
    }

    private List<Country> loadCountries() {
        try (InputStream is = getClass().getResourceAsStream("/data/countries.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Type listType = new TypeToken<ArrayList<Country>>(){}.getType();
            List<Country> loadedCountries = new Gson().fromJson(reader, listType);

            if (loadedCountries == null) {
                System.err.println("Loaded countries list is null");
                return Collections.emptyList();
            }

            System.out.println("Successfully loaded " + loadedCountries.size() + " countries");
            return loadedCountries.stream()
                    .filter(c -> c.getCountry() != null && !c.getCountry().isEmpty())
                    .filter(c -> c.getCapital() != null && !c.getCapital().isEmpty())
                    .filter(c -> c.getCurrency() != null && !c.getCurrency().isEmpty())
                    .filter(c -> c.getLanguage() != null && !c.getLanguage().isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error loading countries: " + e.getMessage());
            e.printStackTrace();
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

    public synchronized void updateLevelProgress(int level, int score) {
        if (currentUser == null) return;

        // Update the score for this level
        currentUser.setLevelScore(level, score);

        // Unlock next level if score >=7 and not already unlocked
        if (score >= 7 && level < 5) {
            int nextLevel = level + 1;
            if (nextLevel > currentUser.getHighestLevelUnlocked()) {
                currentUser.setHighestLevelUnlocked(nextLevel);
                System.out.println("Unlocked level " + nextLevel + " for user " + currentUser.getUsername());
            }
        }

        // Save to database
        userDatabase.updateUser(currentUser);

        // Debug output
        System.out.println("Updated level " + level + " with score " + score +
                ". Highest unlocked: " + currentUser.getHighestLevelUnlocked());
    }
    public synchronized void updateUserProgress(int level, int score) {
        if (currentUser == null) {
            System.err.println("Cannot update progress - no user logged in");
            return;
        }

        System.out.println("Updating level " + level + " with score " + score);
        currentUser.setLevelScore(level, score);
        userDatabase.updateUser(currentUser);

        // Debug output
        System.out.println("Updated level " + level + " with score " + score +
                ". Highest unlocked: " + currentUser.getHighestLevelUnlocked());
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
    public boolean isLevelUnlocked(int level) {
        System.out.println("Checking if level " + level + " is unlocked...");
        if (currentUser == null) {
            System.out.println("No user - only level 1 available");
            return level == 1;
        }
        boolean unlocked = level <= currentUser.getHighestLevelUnlocked();
        System.out.println("Level " + level + " unlocked: " + unlocked);
        return unlocked;
    }

    public List<Country> getCountriesForQuiz(int levelType) {
        List<Country> filtered = new ArrayList<>(countries);

        // Additional filtering based on quiz type
        if (levelType == 2) { // Capital quiz
            filtered.removeIf(c -> c.getCapital() == null || c.getCapital().isEmpty());
        }
        else if (levelType == 3) { // Currency quiz
            filtered.removeIf(c -> c.getCurrency() == null || c.getCurrency().isEmpty());
        }
        else if (levelType == 4) { // Language quiz
            filtered.removeIf(c -> c.getLanguage() == null || c.getLanguage().isEmpty());
        }

        return filtered.size() >= 4 ? filtered : Collections.emptyList();
    }
    public List<Country> getValidCountriesForQuiz(int levelType) {
        List<Country> validCountries = new ArrayList<>(countries);

        // Additional level-specific validation
        validCountries.removeIf(c ->
                c.getCountry() == null || c.getCountry().isEmpty() ||
                        (levelType == 2 && (c.getCapital() == null || c.getCapital().isEmpty())) ||
                        (levelType == 3 && (c.getCurrency() == null || c.getCurrency().isEmpty())) ||
                        (levelType == 4 && (c.getLanguage() == null || c.getLanguage().isEmpty()))
        );

        System.out.println("Returning " + validCountries.size() + " valid countries for level " + levelType);
        return validCountries;
    }

    public LeaderboardPanel getLeaderboardPanel() {
        return new LeaderboardPanel(this); // Pass the controller instead of just userDatabase
    }
    public UserDatabase getUserDatabase() {
        return this.userDatabase;
    }

}