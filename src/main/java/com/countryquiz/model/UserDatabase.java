package com.countryquiz.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserDatabase {
    private static final String DB_FILE = "users.txt"; // File outside src

    private static List<User> users = new ArrayList<>();
    private Gson gson = new Gson();

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    public UserDatabase() {
        this.users = loadUsers();
    }

    private List<User> loadUsers() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> loadedUsers = new Gson().fromJson(reader, userListType);
            return loadedUsers != null ? loadedUsers : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private synchronized void saveUsers() {
        try (Writer writer = new FileWriter(DB_FILE)) {
            new Gson().toJson(users, writer);
            System.out.println("Saved users to file"); // Debug
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public boolean register(User newUser) {
        if (newUser == null || userExists(newUser.getUsername())) {
            return false;
        }
        users.add(newUser);
        saveUsers();
        System.out.println("Registered user: " + newUser.getUsername()); // Debug
        return true;
    }


    public Optional<User> login(String username, String password) {
        System.out.println("Attempting login for: " + username); // Debug
        return users.stream()
                .filter(user -> user.getUsername().equals(username) &&
                        user.getPassword().equals(password))
                .peek(user -> System.out.println("Found matching user: " + user.getUsername())) // Debug
                .findFirst();
    }

    public boolean userExists(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public synchronized boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                saveUsers();
                return true;
            }
        }
        return false;
    }
    public static List<User> getLeaderboardData() {
        List<User> sortedUsers = new ArrayList<>(users);
        // Sort by total score by default
        sortedUsers.sort(Comparator.comparingInt(User::getTotalScore).reversed());
        return sortedUsers;
    }

}