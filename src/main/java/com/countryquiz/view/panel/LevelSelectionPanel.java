package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import javax.swing.*;
import java.awt.*;


public class LevelSelectionPanel extends JPanel {
    private final GameController gameController;

    public LevelSelectionPanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new GridLayout(5, 1, 10, 10));
        refreshLevelButtons();
    }

    public void refreshLevelButtons() {
        removeAll();

        // Create a grid layout with some spacing
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Regular levels
        for (int i = 1; i <= 4; i++) {
            JButton levelButton = new JButton();
            levelButton.setFont(new Font("Tahoma", Font.BOLD, 16));

            if (gameController.isLevelUnlocked(i)) {
                levelButton.setText("Level " + i + getLevelType(i));
                levelButton.setEnabled(true);
                levelButton.setBackground(new Color(100, 200, 100)); // Green for unlocked
            } else {
                levelButton.setText("LOCKED (Score 7+ in Level " + (i-1) + ")");
                levelButton.setEnabled(false);
                levelButton.setBackground(new Color(200, 100, 100)); // Red for locked
            }
            add(levelButton);
        }

        // Mastery level
        JButton masteryButton = new JButton();
        masteryButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        if (gameController.isLevelUnlocked(5)) {
            masteryButton.setText("MASTERY LEVEL");
            masteryButton.setEnabled(true);
            masteryButton.setBackground(new Color(100, 100, 200)); // Blue for mastery
        } else {
            masteryButton.setText("Complete all 4 levels to unlock");
            masteryButton.setEnabled(false);
            masteryButton.setBackground(new Color(200, 200, 200)); // Gray for locked
        }
        add(masteryButton);

        revalidate();
        repaint();
    }

    private String getLevelType(int level) {
        switch(level) {
            case 1: return " - Flags";
            case 2: return " - Capitals";
            case 3: return " - Currencies";
            case 4: return " - Languages";
            default: return "";
        }
    }
}