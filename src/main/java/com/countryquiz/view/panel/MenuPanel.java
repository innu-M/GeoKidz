package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.controller.AudioController;
import com.countryquiz.model.User;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onLevelSelect;
    private final Runnable onLogout;
    private final AudioController audioController;

    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private TextOverlayButton levelSelectButton;
    private TextOverlayButton scoresButton;
    private MusicToggleButton musicToggleButton;
    private TextOverlayButton aboutButton;
    private TextOverlayButton logoutButton;
    private BackButton backButton;
    private ImageButton quizImageButton;

    public MenuPanel(GameController gameController, Runnable onLevelSelect,
                     Runnable onLogout, AudioController audioController) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onLevelSelect = onLevelSelect;
        this.onLogout = onLogout;
        this.audioController = audioController;

        setLayout(null); // Absolute positioning
        initComponents();
    }

    private void initComponents() {
//        titleLabel = new JLabel("Select Option");
//        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
//        titleLabel.setForeground(Color.BLACK);
//        titleLabel.setBounds(260, 90, 400, 50);
//        add(titleLabel);

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.black);
        welcomeLabel.setBounds(280, 160, 350, 30);
        add(welcomeLabel);

        levelSelectButton = new TextOverlayButton("", "/images/level.png");
        levelSelectButton.setBounds(250, 160, 300, 100);
        levelSelectButton.addActionListener(e -> onLevelSelect.run());
        add(levelSelectButton);

        scoresButton = new TextOverlayButton("", "/images/score.png");
        scoresButton.setBounds(250, 250, 300, 100);
        scoresButton.addActionListener(e -> showHighScores());
        add(scoresButton);

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
        logoutButton.setBounds(520, 470, 200, 120);
        logoutButton.addActionListener(e -> {
            gameController.logout();
            onLogout.run();
        });
        add(logoutButton);

//        quizImageButton = new ImageButton(
//                "/images/quiz.png",
//                "/images/quiz_hover.png",
//                180, 100,
//                e -> onLevelSelect.run()
//        );
//        quizImageButton.setBounds(580, 450, 180, 100);
//        add(quizImageButton);

        backButton = new BackButton(() -> {
            gameController.logout();
            onLogout.run();
        });
        backButton.setBounds(28, 460, 200, 120);
        add(backButton);
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
        welcomeLabel.setForeground(Color.RED);
    }

    private void updateMusicButtonText() {
        musicToggleButton.setText(audioController.isMusicEnabled() ? "\uD83D\uDD0A Music ON" : "\uD83D\uDD07 Music OFF");
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
                "<li>\uD83C\uDDFA\uD83C\uDDF3 Flags</li>" +
                "<li>\uD83C\uDFD9\uFE0F Capitals</li>" +
                "<li>\uD83D\uDCB0 Currencies</li>" +
                "<li>\uD83D\uDDE3\uFE0F Languages</li>" +
                "<li>\uD83C\uDFC6 Mastery Quiz</li>" +
                "</ul>" +
                "<p>Score 7+ in each level to unlock the next!</p>" +
                "</center></html>";

        JOptionPane.showMessageDialog(this, aboutText,
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
