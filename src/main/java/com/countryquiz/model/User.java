package com.countryquiz.model;

import java.util.Objects;

public class User {
    private final String username;
    private final String password;
    private int[] levelScores;
    private int highestLevelUnlocked;

    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
        this.levelScores = new int[5]; // For 5 levels
        this.highestLevelUnlocked = 1; // Start with level 1 unlocked
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLevelScore(int level) {
        validateLevel(level);
        return levelScores[level-1];
    }

    public void setLevelScore(int level, int score) {
        validateLevel(level);
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }
        levelScores[level-1] = score;
    }

    public int getHighestLevelUnlocked() {
        return highestLevelUnlocked;
    }

    public void setHighestLevelUnlocked(int level) {
        validateLevel(level);
        this.highestLevelUnlocked = level;
    }

    private void validateLevel(int level) {
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}