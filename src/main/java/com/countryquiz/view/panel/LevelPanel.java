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
        setLayout(null); // Absolute positioning
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

        // User not logged in
        if (gameController.getCurrentUser() == null) {
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
            return;
        }

        // Title
        JLabel title = new JLabel("Select Level");
        title.setFont(new Font("Tahoma", Font.BOLD, 32));
        title.setForeground(Color.BLACK);
        title.setBounds(285, 8, 280, 280);
        add(title);

        // Level buttons (resizable)
        int highestUnlocked = gameController.getCurrentUser().getHighestLevelUnlocked();
        String[] levelNames = {"Flags", "Capitals", "Currencies", "Languages", "Mastery Quiz"};
        int[][] buttonPositions = {
                {100, 160}, {320, 160}, {540, 160}, {180, 340}, {400, 340}
        };

        int buttonWidth = 170;
        int buttonHeight = 170;

        for (int i = 0; i < levelActions.length && i < 5; i++) {
            int x = buttonPositions[i][0];
            int y = buttonPositions[i][1];
            int level = i + 1;

            String imagePath = "/images/level" + level + ".png";

            int finalI = i;
            ImageButton levelBtn = new ImageButton(
                    imagePath,
                    imagePath,
                    buttonWidth,
                    buttonHeight,
                    e -> levelActions[finalI].run()
            );

            levelBtn.setBounds(x, y, buttonWidth, buttonHeight);

            if (level > highestUnlocked) {
                levelBtn.setEnabled(false);
            }

            add(levelBtn);
        }

        // Back button
        BackButton backBtn = new BackButton(onBack);
        backBtn.setBounds(350, 500, 50, 50);
        add(backBtn);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // fixed panel size
    }
}
