package com.countryquiz.view.panel;

import com.countryquiz.controller.AudioController;
import com.countryquiz.controller.GameController;
import com.countryquiz.view.components.*;
import com.countryquiz.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable[] levelActions;
    private final Runnable onBack;

    public LevelPanel(GameController gameController, Runnable[] levelActions, Runnable onBack) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.levelActions = levelActions;
        this.onBack = onBack;
        setLayout(null); // Absolute positioning
        initUI(); // Initialize immediately
    }

    private void initUI() {
        removeAll();

        // User not logged in
        if (!gameController.isLoggedIn() || gameController.getCurrentUser() == null) {
            showLoginPrompt();
            return;
        }

        // Create level buttons
        createLevelButtons();


        revalidate();
        repaint();
    }

    private void showLoginPrompt() {
        JLabel errorLabel = new JLabel("Please login first");
        errorLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        errorLabel.setForeground(Color.WHITE);
        errorLabel.setBounds(270, 200, 300, 40);
        add(errorLabel);

        ImageButton loginButton = new ImageButton(
                "/images/login.png",
                "/images/login.png",
                200, 120,
                e -> onBack.run()
        );
        loginButton.setBounds(290, 270, 200, 120);
        add(loginButton);
    }
 private void createLevelButtons() {
        removeAll();

        User user = gameController.getCurrentUser();
        int highestUnlocked = user.getHighestLevelUnlocked();
        int[][] positions = {{100,100}, {320,100}, {540,100}, {180,300}, {400,300}};
        int size = 170;

        for (int i = 0; i < levelActions.length && i < 5; i++) {
            final int level = i + 1;
            final int actionIndex = i; // Separate index for action array

            ImageButton btn = new ImageButton(
                    "/images/level" + level + ".png",
                    "/images/level" + level + ".png",
                    size, size,
                    e -> {
                        System.out.println("Level " + level + " button clicked");
                        if (level <= highestUnlocked) {
                            System.out.println("Attempting to execute action for Level " + level);
                            levelActions[actionIndex].run();
                        }
                    }
            );

            btn.setBounds(positions[i][0], positions[i][1], size, size);
            btn.setEnabled(level <= highestUnlocked);
            add(btn);
        }

        // Back button
        BackButton backBtn = new BackButton(() -> {
            System.out.println("Back button clicked - executing action");
            onBack.run();
        });
        backBtn.setBounds(280, 460, 200, 120);
        add(backBtn);

        revalidate();
        repaint();
    }


    public void refreshLevels() {
        initUI();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}