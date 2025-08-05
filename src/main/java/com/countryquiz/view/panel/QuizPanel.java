package com.countryquiz.view.panel;

import com.countryquiz.Main;
import com.countryquiz.controller.GameController;
import com.countryquiz.controller.AudioController;
import com.countryquiz.model.Country;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
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
    private boolean quizCompleted = false;
    private List<String> currentOptions = new ArrayList<>();


    private JLabel questionLabel;
    private JLabel scoreLabel;
    private JButton[] optionButtons;
    private JLabel resultLabel;
    private JLabel flagLabel;
    private JButton nextButton;
    private JButton prevButton;
    private final Runnable onReturnToLevelSelection;

    public QuizPanel(GameController gameController, Runnable onBack, AudioController audioController, int levelType) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onBack = onBack;
        this.audioController = audioController;
        this.levelType = levelType;
        this.onReturnToLevelSelection = () -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                Container parent = this.getParent();
                while (parent != null && !(parent instanceof Main)) {
                    parent = parent.getParent();
                }
                if (parent instanceof Main) {
                    ((Main) parent).showLevelPanel();
                }
            }
        };
        this.countries=gameController.getValidCountriesForQuiz(levelType);
        System.out.println("Countries count: " + countries.size());
        countries.forEach(c -> System.out.println(c.getCountry()));

        if (countries.size() < 4) {
            showError("Not enough valid countries (" + countries.size() + "). Need at least 4.");
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
        scoreLabel.setForeground(Color.BLACK);
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
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        add(questionLabel, gbc);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setOpaque(false);
        optionButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(new Font("Tahoma", Font.BOLD, 16));
            optionButtons[i].setBackground(new Color(240, 240, 240));
            optionButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
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

        // Navigation Buttons
        JPanel navPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        navPanel.setOpaque(false);

        prevButton = new JButton("Previous");
        prevButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        prevButton.addActionListener(e -> prevQuestion());

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        nextButton.addActionListener(e -> nextQuestion());

        navPanel.add(prevButton);
        navPanel.add(nextButton);

        gbc.gridy = 5;
        add(navPanel, gbc);
    }

    private void nextQuestion() {
        if (quizCompleted) {
            showFinalScore();
            return;
        }

        resultLabel.setText("");

        if (questionCount >= totalQuestions) {
            showFinalScore();
            return;
        }

        // Reset button states
        for (JButton button : optionButtons) {
            button.setEnabled(true);
            button.setBackground(new Color(240, 240, 240));
        }

        // For mastery level (level 5), randomize question type
        int currentQuestionType = levelType;
        if (levelType == 5) {
            currentQuestionType = new Random().nextInt(4) + 1; // Random between 1-4
        }

        // Get a random country
        Collections.shuffle(countries);
        currentCountry = countries.get(0);

        switch (getCurrentQuestionType()) {
            case 1: // Flag
                questionLabel.setText("Which country's flag is this?");
                correctAnswer = currentCountry.getCountry();
                loadFlagImage();
                break;
            case 2: // Capital
                questionLabel.setText(String.format("What is the capital of %s?", currentCountry.getCountry()));
                correctAnswer = currentCountry.getCapital();
                flagLabel.setIcon(null);
                break;
            case 3: // Currency
                questionLabel.setText(String.format("What currency is used in %s?", currentCountry.getCountry()));
                correctAnswer = currentCountry.getCurrency();
                flagLabel.setIcon(null);
                break;
            case 4: // Language
                questionLabel.setText(String.format("What language is spoken in %s?", currentCountry.getCountry()));
                correctAnswer = currentCountry.getLanguage();
                flagLabel.setIcon(null);
                break;
        }

        // Generate options
        currentOptions = generateOptions(correctAnswer);
        for (int i = 0; i < 4 && i < currentOptions.size(); i++) {
            optionButtons[i].setText(currentOptions.get(i));
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackground(new Color(240, 240, 240));
        }

        questionCount++;
        updateNavButtons();
    }


    private void prevQuestion() {
        if (questionCount <= 1) {
            return; // No previous question
        }
        questionCount--;
        resultLabel.setText("");
        for (JButton button : optionButtons) {
            button.setEnabled(true);
            button.setBackground(new Color(240, 240, 240));
        }
        // Reset the flag image
        flagLabel.setIcon(null);
        // Reset the question label
        questionLabel.setText("");
        // Reset the score label
        scoreLabel.setText("Score: " + score + "/" + totalQuestions);
        // Reset the result label
        resultLabel.setText("");
        // Load the previous question
        if (currentCountry != null) {
            switch (levelType) {
                case 1:
                    questionLabel.setText("Which country's flag is this?");
                    correctAnswer = currentCountry.getCountry();
                    loadFlagImage();
                    break;
                case 2:
                    questionLabel.setText(String.format("What is the capital of %s?", currentCountry.getCountry()));
                    correctAnswer = currentCountry.getCapital();
                    flagLabel.setIcon(null);
                    break;
                case 3:
                    questionLabel.setText(String.format("What currency is used in %s?", currentCountry.getCountry()));
                    correctAnswer = currentCountry.getCurrency();
                    flagLabel.setIcon(null);
                    break;
                case 4:
                    questionLabel.setText(String.format("What language is spoken in %s?", currentCountry.getCountry()));
                    correctAnswer = currentCountry.getLanguage();
                    flagLabel.setIcon(null);
                    break;
            }
            currentOptions = generateOptions(correctAnswer);
            for (int i = 0; i < 4 && i < currentOptions.size(); i++) {
                optionButtons[i].setText(currentOptions.get(i));
            }
        } else {
            showError("No previous question available.");
            return; // No previous question to load
        }
        resetQuiz();
    }

    private void loadFlagImage() {
        try {
            URL flagUrl = getClass().getResource(currentCountry.getFlag());
            if (flagUrl != null) {
                ImageIcon flagIcon = new ImageIcon(flagUrl);
                Image scaledFlag = flagIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                flagLabel.setIcon(new ImageIcon(scaledFlag));
            } else {
                flagLabel.setIcon(null);
            }
        } catch (Exception e) {
            flagLabel.setIcon(null);
        }
    }

    private List<String> generateOptions(String correctAnswer) {
        List<String> options = new ArrayList<>();
        List<Country> countryCopy = new ArrayList<>(countries);
        countryCopy.remove(currentCountry);
        Collections.shuffle(countryCopy);

        // Get 3 wrong answers
        for (Country country : countryCopy) {
            if (options.size() >= 3) break;

            String answer = getAnswerForLevelType(country);
            if (answer != null && !answer.equals(correctAnswer) && !options.contains(answer)) {
                options.add(answer);
            }
        }

        // If still not enough options, use some fallbacks
        while (options.size() < 3) {
            options.add("Not " + correctAnswer); // Simple fallback
            if (options.size() < 3) {
                options.add("None of these");
            }
        }

        options.add(correctAnswer);
        Collections.shuffle(options);
        return options;
    }


    private String getAnswerForLevelType(Country country) {
        if (country == null) return null;

        // For mastery level, use the current question type
        int questionType = levelType;
        if (levelType == 5) {
            // Determine what type of question this is based on the current question text
            if (questionLabel.getText().contains("capital")) {
                questionType = 2;
            } else if (questionLabel.getText().contains("currency")) {
                questionType = 3;
            } else if (questionLabel.getText().contains("language")) {
                questionType = 4;
            } else {
                questionType = 1; // Default to flag
            }
        }

        switch (getCurrentQuestionType()) {
            case 1: return country.getCountry();
            case 2: return country.getCapital();
            case 3: return country.getCurrency();
            case 4: return country.getLanguage();
            default: return null;
        }
    }
    private int getCurrentQuestionType() {
        if (levelType != 5) {
            return levelType; // Return normal level type for levels 1-4
        }
        // For mastery level (5), return random type between 1-4
        return new Random().nextInt(4) + 1;
    }



    private void checkAnswer(Object source) {
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
        updateNavButtons();
    }

    private void updateNavButtons() {
        prevButton.setEnabled(questionCount > 1);
        nextButton.setEnabled(true);
    }

    private void showFinalScore() {
        // Update progress - only for levels 1-4
        if (levelType >= 1 && levelType <= 4) {
            gameController.updateLevelProgress(levelType, score);
        }

        // Show results in a dialog
        String message = "Your score: " + score + "/10\n";
        if (levelType == 5) {
            message += "Mastery Level completed!";
        } else if (score >= 7) {
            if (levelType < 4) {
                message += "Level " + (levelType + 1) + " unlocked!";
            } else if (levelType == 4) {
                message += "Mastery Level unlocked!";
            }
        } else {
            message += "Score 7+ needed to unlock next level";
        }

        JOptionPane.showMessageDialog(this, message, "Quiz Complete", JOptionPane.INFORMATION_MESSAGE);
        returnToLevelSelection();
    }

    private void resetQuiz() {
        quizCompleted = false;
        score = 0;
        questionCount = 0;
        scoreLabel.setText("Score: 0/" + totalQuestions);
        resultLabel.setText("");

        for (JButton button : optionButtons) {
            button.setVisible(true);
            button.setBackground(new Color(240, 240, 240));
            button.setEnabled(true);
        }

        prevButton.setVisible(true);
        nextButton.setVisible(true);

        // Remove restart button
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton && ((JButton)comp).getText().equals("Play Again")) {
                remove(comp);
            }
        }

        nextQuestion();
    }
    private void returnToLevelSelection() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            Container parent = this.getParent();
            while (parent != null && !(parent instanceof Main)) {
                parent = parent.getParent();
            }
            if (parent instanceof Main) {
                ((Main) parent).showLevelPanel();
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        onBack.run();
    }
}