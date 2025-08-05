package com.countryquiz.model;

import java.util.Arrays;
import java.util.Objects;

public class User {
    private final String username;
    private final String password;
    private final int[] levelScores=new int[4];
    private int highestLevelUnlocked=1;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        boolean[] levelUnlocked = new boolean[4];
        levelUnlocked[0] = true; // Level 1 always unlocked
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setLevelScore(int level, int score) {
        if (level < 1 || level > 4) return;

        levelScores[level-1] = score;

        // Unlock next level if score >=7
        if (score >= 7 && level == highestLevelUnlocked && level < 4) {
            highestLevelUnlocked = level + 1;
        }

        // Unlock mastery if all levels completed with >=7
        if (level == 4 && score >= 7 && checkAllLevelsCompleted()) {
            highestLevelUnlocked = 5;
        }
    }
    public int getLevelScore(int level) {
        // Change from level-1 to proper bounds checking
        if (level < 1 || level > 5) return 0;
        if (level == 5) return 0; // Mastery level has no score

        // For levels 1-4
        return levelScores[level-1];
    }
    private boolean checkAllLevelsCompleted() {
        for (int i = 0; i < 4; i++) {
            if (levelScores[i] < 7) {
                return false;
            }
        }
        return true;
    }





    public int getHighestLevelUnlocked() {
        return highestLevelUnlocked;
    }
    public void setHighestLevelUnlocked(int level) {
        System.out.println("Setting highest unlocked level to: " + level);
        if (level > highestLevelUnlocked && level <= 5) {
            this.highestLevelUnlocked = level;
            System.out.println("New highest level: " + highestLevelUnlocked);
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


    public int getTotalScore() {
        return Arrays.stream(levelScores).sum();
    }
}