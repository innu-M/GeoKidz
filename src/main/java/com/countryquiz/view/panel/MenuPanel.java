package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.controller.AudioController;
import com.countryquiz.model.User;
import com.countryquiz.view.components.*;
import com.countryquiz.view.panel.LeaderboardPanel;
import com.countryquiz.view.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onLevelSelect;
    private final Runnable showLearningPanel; // Changed to Runnable
    private final Runnable onLogout;
    private final AudioController audioController;

    private JLabel welcomeLabel;
    private TextOverlayButton levelSelectButton;
    private TextOverlayButton scoresButton;
    private MusicToggleButton musicToggleButton;
    private TextOverlayButton aboutButton;
    private TextOverlayButton logoutButton;
    private BackButton backButton;
    private TextOverlayButton leaderboardButton;
    private TextOverlayButton learningButton;

    public MenuPanel(GameController gameController,
                     Runnable onLevelSelect,
                     Runnable showLearningPanel, // Correct type
                     Runnable onLogout,
                     AudioController audioController) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onLevelSelect = onLevelSelect;
        this.showLearningPanel = showLearningPanel; // Correct assignment
        this.onLogout = onLogout;
        this.audioController = audioController;

        setLayout(null);
        initComponents();
    }

    private void initComponents() {
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.black);
        welcomeLabel.setBounds(280, 140, 350, 30);
        add(welcomeLabel);

        levelSelectButton = new TextOverlayButton("", "/images/level.png");
        levelSelectButton.setBounds(250, 160, 300, 100);
        levelSelectButton.addActionListener(e -> onLevelSelect.run());
        add(levelSelectButton);

        learningButton = new TextOverlayButton("", "/images/leaderboard.png");
        learningButton.setBounds(250, 250, 300, 100);
        learningButton.addActionListener(e -> showLearningPanel.run()); // Fixed lambda
        add(learningButton);

        scoresButton = new TextOverlayButton("", "/images/score.png");
        scoresButton.setBounds(250, 340, 300, 100);
        scoresButton.addActionListener(e -> {
            audioController.playSoundEffect("click");
            showHighScores();
        });
        add(scoresButton);

        leaderboardButton = new TextOverlayButton("", "/images/leaderboard.png");
        leaderboardButton.setBounds(250, 430, 300, 100);
        leaderboardButton.addActionListener(e -> showLeaderboard());
        add(leaderboardButton);

        musicToggleButton = new MusicToggleButton(() -> {
            audioController.toggleMusic();
            updateMusicButtonText();
        });
        updateMusicButtonText();
        musicToggleButton.setBounds(645, 28, 100, 40);
        add(musicToggleButton);

        aboutButton = new TextOverlayButton("", "/images/about.png");
        aboutButton.setBounds(10, 5, 160, 220);
        aboutButton.addActionListener(e -> showAboutDialog());
        add(aboutButton);

        logoutButton = new TextOverlayButton("", "/images/logout.png");
        logoutButton.setBounds(520, 460, 200, 120);
        logoutButton.addActionListener(e -> {
            gameController.logout();
            onLogout.run();
            refreshUI();
        });
        add(logoutButton);

        backButton = new BackButton(() -> {
            gameController.logout();
            onLogout.run();
        });
        backButton.setBounds(28, 460, 200, 120);
        add(backButton);
    }


    private void showLeaderboard() {
        // Create a dialog to display the leaderboard
        JDialog leaderboardDialog = new JDialog();
        leaderboardDialog.setTitle("Leaderboard");
        leaderboardDialog.setSize(700, 500);
        leaderboardDialog.setLocationRelativeTo(this);
        leaderboardDialog.setModal(true);
        leaderboardDialog.setLayout(new BorderLayout());

        // Create table with user data
        JTable table = new JTable();
        String[] columns = {"Rank", "Username", "Total Score", "Flags", "Capitals", "Currencies", "Languages"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Get and sort users by total score
        List<User> users = gameController.getUserDatabase().getAllUsers();
        users.sort((u1, u2) -> {
            int total1 = u1.getLevelScore(1) + u1.getLevelScore(2) + u1.getLevelScore(3) + u1.getLevelScore(4);
            int total2 = u2.getLevelScore(1) + u2.getLevelScore(2) + u2.getLevelScore(3) + u2.getLevelScore(4);
            return Integer.compare(total2, total1); // Descending order
        });

        // Populate table
        int rank = 1;
        for (User user : users) {
            model.addRow(new Object[]{
                    rank++,
                    user.getUsername(),
                    user.getLevelScore(1) + user.getLevelScore(2) + user.getLevelScore(3) + user.getLevelScore(4),
                    user.getLevelScore(1) + "/10",
                    user.getLevelScore(2) + "/10",
                    user.getLevelScore(3) + "/10",
                    user.getLevelScore(4) + "/10"
            });
        }

        // Configure table appearance
        table.setModel(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> leaderboardDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Add components to dialog
        leaderboardDialog.add(new JScrollPane(table), BorderLayout.CENTER);
        leaderboardDialog.add(buttonPanel, BorderLayout.SOUTH);
        leaderboardDialog.setVisible(true);
    }
    public void refreshUI() {
        if (gameController.isLoggedIn()) {
            try {
                User user = gameController.getCurrentUser();
                welcomeLabel.setText("WELCOME, " + user.getUsername());
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
        welcomeLabel.setForeground(Color.BLACK);
    }

    private void updateMusicButtonText() {
        musicToggleButton.setText(audioController.isMusicEnabled() ? "\uD83D\uDD0A Music ON" : "\uD83D\uDD07 Music OFF");
    }

    private void showHighScores() {
        try {
            User user = gameController.getCurrentUser();
            StringBuilder scores = new StringBuilder("<html><h2>Your Scores</h2><table>");

            // Only show levels 1-4
            for (int i = 1; i <= 4; i++) {
                scores.append("<tr><td>Level ").append(i).append(":</td>")
                        .append("<td>").append(user.getLevelScore(i)).append("/10</td></tr>");
            }

            // Add mastery status
            if (user.getHighestLevelUnlocked() >= 5) {
                scores.append("<tr><td>Mastery:</td><td>Unlocked</td></tr>");
            }

            scores.append("</table></html>");

            JOptionPane.showMessageDialog(this, scores.toString(),
                    "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
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
                "<li>Flags</li>" +
                "<li> Capitals</li>" +
                "<li> Currencies</li>" +
                "<li>Languages</li>" +
                "<li> Mastery Quiz</li>" +
                "</ul>" +
                "<p>Score 7+ in each level to unlock the next!</p>" +
                "</center></html>";

        JOptionPane.showMessageDialog(this, aboutText,
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}