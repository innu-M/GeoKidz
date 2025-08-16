package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.model.Country;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;

public class LearningPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onBackAction;
    private final Runnable onQuizStart;
    private JLabel countryLabel;
    private JLabel capitalLabel;
    private JLabel languageLabel;
    private JLabel currencyLabel;
    private JLabel flagLabel;
    private JLabel progressLabel;
    private int currentIndex = 0;
    private List<Country> countries;

    public LearningPanel(GameController gameController, Runnable onBackAction, Runnable onQuizStart) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onBackAction = onBackAction;
        this.onQuizStart = onQuizStart;
        this.countries = gameController.getCountries();// Load from file instead of controller

        initUI();
        showCountry(currentIndex);
    }


    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back Button
        BackButton backBtn = new BackButton(onBackAction);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(backBtn, gbc);

        // Title
        JLabel title = new JLabel("Learn About Countries");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Progress Label
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        progressLabel.setForeground(Color.BLACK);
        gbc.gridy = 2;
        add(progressLabel, gbc);

        // Flag Display
        flagLabel = new JLabel();
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        add(flagLabel, gbc);

        // Country Info
        countryLabel = createInfoLabel("");
        gbc.gridy = 4;
        add(countryLabel, gbc);

        capitalLabel = createInfoLabel("");
        gbc.gridy = 5;
        add(capitalLabel, gbc);

        languageLabel = createInfoLabel("");
        gbc.gridy = 6;
        add(languageLabel, gbc);

        currencyLabel = createInfoLabel("");
        gbc.gridy = 7;
        add(currencyLabel, gbc);

        // Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        ImageButton prevBtn = new ImageButton("/images/prev_button.png", 50, 50);
        prevBtn.addActionListener(e -> showPreviousCountry());
        buttonPanel.add(prevBtn);

        ImageButton nextBtn = new ImageButton("/images/next_button.png", 50, 50);
        nextBtn.addActionListener(e -> showNextCountry());
        buttonPanel.add(nextBtn);

        gbc.gridy = 8;
        add(buttonPanel, gbc);

        // Quiz Button
        TextOverlayButton quizBtn = new TextOverlayButton("Start Quiz", "/images/button_bg.png");
        quizBtn.addActionListener(e -> onQuizStart.run());
        gbc.gridy = 9;
        add(quizBtn, gbc);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void showCountry(int index) {
        if (countries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No countries available to display",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Country country = countries.get(index);
        countryLabel.setText("Country: " + country.getCountry());
        capitalLabel.setText("Capital: " + country.getCapital());
        languageLabel.setText("Language: " + country.getLanguage());
        currencyLabel.setText("Currency: " + country.getCurrency());
        progressLabel.setText((index + 1) + " of " + countries.size());

        // Load flag image
        try {
            ImageIcon flagIcon = new ImageIcon(getClass().getResource(country.getFlag()));
            if (flagIcon.getImage() != null) {
                Image scaledFlag = flagIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                flagLabel.setIcon(new ImageIcon(scaledFlag));
            }
        } catch (Exception e) {
            flagLabel.setIcon(null);
        }

        // Update button states
        updateButtonStates();
    }

    private void showNextCountry() {
        if (currentIndex < countries.size() - 1) {
            currentIndex++;
            showCountry(currentIndex);
        }
    }

    private void showPreviousCountry() {
        if (currentIndex > 0) {
            currentIndex--;
            showCountry(currentIndex);
        }
    }

    private void updateButtonStates() {
        // You can disable/enable buttons here if needed
        // For example, disable next button on last country
    }
}