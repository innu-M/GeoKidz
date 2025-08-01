package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;

public class LevelPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable[] levelActions;
    private final Runnable onBack;
    private boolean initialized = false;

    public LevelPanel(GameController gameController, Runnable[] levelActions, Runnable onBack) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.levelActions = levelActions;
        this.onBack = onBack;
        setLayout(new GridBagLayout());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!initialized) {
            initUI();
            initialized = true;
        }
    }

    private void initUI() {
        removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        // Check if user is logged in
        if (gameController.getCurrentUser() == null) {
            JLabel errorLabel = new JLabel("Please login first");
            errorLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
            errorLabel.setForeground(Color.WHITE);
            add(errorLabel, gbc);

            JButton loginButton = new TextOverlayButton("Go to Login", "/images/login.png");
            loginButton.addActionListener(e -> onBack.run());
            gbc.gridy = 1;
            add(loginButton, gbc);

            revalidate();
            repaint();
            return;
        }

        // User is logged in - show levels
        JLabel title = new JLabel("Select Level");
        title.setFont(new Font("Tahoma", Font.BOLD, 32));
        title.setForeground(Color.BLACK);
        add(title, gbc);

        int highestUnlocked = gameController.getCurrentUser().getHighestLevelUnlocked();
        String[] levelNames = {"Flags", "Capitals", "Currencies", "Languages", "Mastery Quiz"};

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        for (int i = 0; i < levelActions.length && i < 5; i++) {
            gbc.gridx = i % 3;
            if (i > 0 && i % 3 == 0) gbc.gridy++;

            int level = i + 1;
            TextOverlayButton levelBtn = new TextOverlayButton(levelNames[i], "/images/level"+level+".png");

            if (level <= highestUnlocked) {
                int score = gameController.getCurrentUser().getLevelScore(level);
                levelBtn.setText(levelNames[i] + " (" + score + "/10)");
                final int levelIndex = i;
                levelBtn.addActionListener(e -> {
                    if (levelIndex < levelActions.length) {
                        levelActions[levelIndex].run();
                    }
                });
            } else {
                levelBtn.setText("Locked");
                levelBtn.setEnabled(false);
            }

            add(levelBtn, gbc);
        }

        // Back Button
        gbc.gridx = 1;
        gbc.gridy++;
        BackButton backBtn = new BackButton(onBack);
        add(backBtn, gbc);

        revalidate();
        repaint();
    }
}