package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.controller.AudioController;
import com.countryquiz.model.Country;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.net.URL;

public class QuizPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onBack;
    private final AudioController audioController;
    private int levelType;
    private List<Country> countries;
    private Country currentCountry;
    private String correctAnswer;
    private int score = 0;
    private int questionCount = 0;
    private final int totalQuestions = 10;

    private JLabel questionLabel;
    private JLabel scoreLabel;
    private JButton[] optionButtons;
    private JLabel resultLabel;
    private JLabel flagLabel;
    private boolean quizCompleted = false;

    public QuizPanel(GameController gameController, Runnable onBack, AudioController audioController, int levelType) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onBack = onBack;
        this.audioController = audioController;
        this.levelType = levelType;
        this.countries = new ArrayList<>(gameController.getCountries());
        if (countries.isEmpty()) {
            showError("No country data loaded");
            return;
        }

        initUI();
        nextQuestion();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back Button
        BackButton backBtn = new BackButton(onBack);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(backBtn, gbc);

        // Score Display
        scoreLabel = new JLabel("Score: 0/" + totalQuestions);
        scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        scoreLabel.setForeground(Color.black);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(scoreLabel, gbc);

        // Flag Display
        flagLabel = new JLabel();
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(flagLabel, gbc);

        // Question
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        questionLabel.setForeground(Color.black);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        add(questionLabel, gbc);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setOpaque(false);
        optionButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new TextOverlayButton("Option " + (i+1), "/images/button_bg.png");
            optionButtons[i].setFont(new Font("Tahoma", Font.BOLD, 16));
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].addActionListener(e -> checkAnswer(e.getSource()));
            optionsPanel.add(optionButtons[i]);
        }

        gbc.gridy = 3;
        add(optionsPanel, gbc);

        // Result Label
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        add(resultLabel, gbc);
    }

    private void nextQuestion() {
        if (quizCompleted) {
            return;
        }

        if (questionCount >= totalQuestions) {
            showFinalScore();
            return;
        }

        resultLabel.setText("");
        if (countries.isEmpty()) {
            showError("No country data available");
            return;
        }

        Collections.shuffle(countries);
        currentCountry = countries.get(0);

        // Set flag image
        try {
            URL flagUrl = getClass().getResource(currentCountry.getFlag());
            if (flagUrl != null) {
                ImageIcon flagIcon = new ImageIcon(flagUrl);
                Image scaledFlag = flagIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                flagLabel.setIcon(new ImageIcon(scaledFlag));
            } else {
                flagLabel.setIcon(null);
                System.err.println("Flag not found: " + currentCountry.getFlag());
            }
        } catch (Exception e) {
            System.err.println("Error loading flag: " + e.getMessage());
            flagLabel.setIcon(null);
        }

        // Set question based on level type
        switch (levelType) {
            case 1: // Flags
                questionLabel.setText("Which country's flag is this?");
                correctAnswer = currentCountry.getName();
                break;
            case 2: // Capitals
                questionLabel.setText("What is the capital of " + currentCountry.getName() + "?");
                correctAnswer = currentCountry.getCapital();
                break;
            case 3: // Currencies
                questionLabel.setText("What currency is used in " + currentCountry.getName() + "?");
                correctAnswer = currentCountry.getCurrency();
                break;
            case 4: // Languages
                questionLabel.setText("What language is spoken in " + currentCountry.getName() + "?");
                correctAnswer = currentCountry.getLanguage();
                break;
            case 5: // Mastery (random)
                int randomType = new Random().nextInt(4) + 1;
                this.levelType = randomType;
                nextQuestion();
                return;
        }

        // Generate options
        List<String> options = generateOptions(correctAnswer);
        for (int i = 0; i < 4 && i < options.size(); i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackground(null);
            optionButtons[i].setVisible(true);
        }

        questionCount++;
    }

    private void showError(String noCountryDataAvailable) {
        JOptionPane.showMessageDialog(this, noCountryDataAvailable, "Error", JOptionPane.ERROR_MESSAGE);
        onBack.run(); // Navigate back if no data is available
        quizCompleted = true; // Mark quiz as completed to prevent further questions
    }

    private List<String> generateOptions(String correctAnswer) {
        List<String> options = new ArrayList<>();
        List<Country> countryCopy = new ArrayList<>(countries);
        Collections.shuffle(countryCopy);

        // Add 3 wrong answers
        for (Country country : countryCopy) {
            String answer = getAnswerForLevelType(country);
            if (answer != null && !answer.equals(correctAnswer) && !options.contains(answer)) {
                options.add(answer);
                if (options.size() >= 3) break;
            }
        }

        // Add correct answer and shuffle
        if (correctAnswer != null) {
            options.add(correctAnswer);
        } else {
            options.add("Unknown");
        }
        Collections.shuffle(options);

        return options;
    }

    private String getAnswerForLevelType(Country country) {
        if (country == null) return null;

        switch (levelType) {
            case 1: return country.getName();
            case 2: return country.getCapital();
            case 3: return country.getCurrency();
            case 4: return country.getLanguage();
            default: return null;
        }
    }

    private void checkAnswer(Object source) {
        if (quizCompleted) return;

        JButton selectedButton = (JButton) source;
        boolean isCorrect = selectedButton.getText().equals(correctAnswer);

        // Visual feedback
        for (JButton button : optionButtons) {
            button.setEnabled(false);
            if (button.getText().equals(correctAnswer)) {
                button.setBackground(Color.GREEN);
            } else if (button == selectedButton && !isCorrect) {
                button.setBackground(Color.RED);
            }
        }

        // Audio feedback
        if (isCorrect) {
            score++;
            audioController.playSoundEffect("correct");
            resultLabel.setText("Correct!");
            resultLabel.setForeground(Color.GREEN);
        } else {
            audioController.playSoundEffect("wrong");
            resultLabel.setText("Incorrect! The answer is: " + correctAnswer);
            resultLabel.setForeground(Color.RED);
        }

        scoreLabel.setText("Score: " + score + "/" + totalQuestions);

        // Next question after delay
        Timer timer = new Timer(1500, e -> {
            nextQuestion();
            for (JButton button : optionButtons) {
                button.setBackground(null);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showFinalScore() {
        quizCompleted = true;

        // Update user progress if score >= 7
        if (score >= 7 && levelType < 5) {
            gameController.updateUserProgress(levelType, score); // Update current level progress
            if (levelType < 4) {
                gameController.unlockLevel(levelType + 1); // Unlock next level
            }
        }

        // Show results
        questionLabel.setText("Quiz Completed!");
        flagLabel.setIcon(null);

        String resultMessage;
        if (score >= 7) {
            resultMessage = "Congratulations! You passed with " + score + "/" + totalQuestions;
            if (levelType < 4) {
                resultMessage += "\nLevel " + (levelType + 1) + " unlocked!";
            }
        } else {
            resultMessage = "You scored " + score + "/" + totalQuestions + "\nTry again to unlock the next level!";
        }

        resultLabel.setText("<html><center>" + resultMessage + "</center></html>");
        resultLabel.setForeground(score >= 7 ? Color.GREEN : Color.RED);

        // Hide options
        for (JButton button : optionButtons) {
            button.setVisible(false);
        }

        // Add restart button
        TextOverlayButton restartBtn = new TextOverlayButton("Play Again", "/images/button_bg.png");
        restartBtn.addActionListener(e -> {
            resetQuiz();
            nextQuestion();
        });

        // Add back to menu button
        TextOverlayButton menuBtn = new TextOverlayButton("Back to Menu", "/images/button_bg.png");
        menuBtn.addActionListener(e -> onBack.run());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(restartBtn);
        buttonPanel.add(menuBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        revalidate();
        repaint();
    }

    private void resetQuiz() {
        quizCompleted = false;
        score = 0;
        questionCount = 0;
        scoreLabel.setText("Score: 0/" + totalQuestions);
        resultLabel.setText("");

        for (JButton button : optionButtons) {
            button.setVisible(true);
            button.setBackground(null);
            button.setEnabled(true);
        }
    }
}