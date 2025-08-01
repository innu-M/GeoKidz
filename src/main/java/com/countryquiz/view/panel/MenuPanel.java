package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.controller.AudioController;
import com.countryquiz.model.User;
import com.countryquiz.view.components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onLevelSelect;
    private final Runnable onLogout;
    private final AudioController audioController;

    // UI Components
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private TextOverlayButton levelSelectButton;
    private TextOverlayButton scoresButton;
    private MusicToggleButton musicToggleButton;
    private TextOverlayButton aboutButton;
    private TextOverlayButton logoutButton;
    private BackButton backButton;

    public MenuPanel(GameController gameController, Runnable onLevelSelect,
                     Runnable onLogout, AudioController audioController) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onLevelSelect = onLevelSelect;
        this.onLogout = onLogout;
        this.audioController = audioController;

        setLayout(new GridBagLayout());
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        // Title
        titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Welcome message
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.black);

        // Level Select Button
        levelSelectButton = new TextOverlayButton("","/images/level.png");
        levelSelectButton.addActionListener(e -> onLevelSelect.run());

        // High Scores Button
        scoresButton = new TextOverlayButton("","/images/score.png");
        scoresButton.addActionListener(e -> showHighScores());

        // Music Toggle Button
        musicToggleButton = new MusicToggleButton(() -> {
            audioController.toggleMusic();
            updateMusicButtonText();
        });
        updateMusicButtonText();

        // About Button
        aboutButton = new TextOverlayButton("","/images/about.png");
        aboutButton.addActionListener(e -> showAboutDialog());

        // Logout Button
        logoutButton = new TextOverlayButton("","/images/logout.png");
        logoutButton.addActionListener(e -> {
            gameController.logout();
            onLogout.run();
        });

        // Back Button (top-left corner)
        backButton = new BackButton(() -> {
            gameController.logout();
            onLogout.run();
        });
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(backButton, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Welcome message
        gbc.gridy = 2;
        add(welcomeLabel, gbc);

        // Buttons
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        add(levelSelectButton, gbc);

        gbc.gridy = 4;
        add(scoresButton, gbc);

        gbc.gridy = 5;
        add(musicToggleButton, gbc);

        gbc.gridy = 6;
        add(aboutButton, gbc);

        gbc.gridy = 7;
        add(logoutButton, gbc);
    }

    public void refreshUI() {
        if (gameController.isLoggedIn()) {
            try {
                User user = gameController.getCurrentUser();
                welcomeLabel.setText("Welcome, " + user.getUsername());
                welcomeLabel.setVisible(true);
            } catch (IllegalStateException e) {
                showLoginError();
            }
        } else {
            showLoginError();
        }
        revalidate();
        repaint();
    }

    private void showLoginError() {
        welcomeLabel.setText("Please login first");
        welcomeLabel.setForeground(Color.RED);
    }

    private void updateMusicButtonText() {
        musicToggleButton.setText(audioController.isMusicEnabled() ? "🔊 Music ON" : "🔇 Music OFF");
    }

    private void showHighScores() {
        try {
            User user = gameController.getCurrentUser();
            StringBuilder scores = new StringBuilder("<html><h2>Your Scores</h2><table>");

            for (int i = 1; i <= 5; i++) {
                scores.append("<tr><td>Level ").append(i).append(":</td>")
                        .append("<td>").append(user.getLevelScore(i)).append("/10</td></tr>");
            }
            scores.append("</table>");

            if (user.getHighestLevelUnlocked() < 5) {
                scores.append("<p>Unlock next level by scoring 7+ in Level ")
                        .append(user.getHighestLevelUnlocked()).append("</p>");
            }

            scores.append("</html>");

            JOptionPane.showMessageDialog(this, scores.toString(),
                    "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                    "Please login to view scores",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading scores: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAboutDialog() {
        String aboutText = "<html><center>" +
                "<h1>Protasha's Country Quiz</h1>" +
                "<p>Learn about countries through 5 exciting levels:</p>" +
                "<ul>" +
                "<li>🇺🇳 Flags</li>" +
                "<li>🏙️ Capitals</li>" +
                "<li>💰 Currencies</li>" +
                "<li>🗣️ Languages</li>" +
                "<li>🎯 Mastery Quiz</li>" +
                "</ul>" +
                "<p>Score 7+ in each level to unlock the next!</p>" +
                "</center></html>";

        JOptionPane.showMessageDialog(this, aboutText,
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /*protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw decorative elements
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(50, 50, getWidth() - 100, getHeight() - 100, 30, 30);
    }

     */

}