package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.model.Country;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LearningPanel extends BackgroundPanel {
    private final GameController gameController;
    private final Runnable onBackAction;
    private JLabel countryLabel;
    private JLabel capitalLabel;
    private JLabel languageLabel;
    private JLabel currencyLabel;
    private JLabel flagLabel;
    private JLabel progressLabel;
    private int currentIndex = 0;
    private List<Country> countries;

    public LearningPanel(GameController gameController, Runnable onBackAction) {
        super("/images/allbg.png");
        this.gameController = gameController;
        this.onBackAction = onBackAction;
        this.countries = gameController.getCountries();

        initUI();
        showCountry(currentIndex);
    }

    private void initUI() {
        setLayout(null); // absolute positioning

        // Back Button
        BackButton backBtn = new BackButton(onBackAction);
        backBtn.setBounds(20, 20, 190, 100);
        add(backBtn);

        // Title
        JLabel title = new JLabel("INFO'S About Countries");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        title.setBounds(270, 90, 400, 40);
        add(title);

        // Progress Label
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        progressLabel.setForeground(Color.BLACK);
        progressLabel.setBounds(370, 128, 200, 30);
        add(progressLabel);

        // Flag Display
        flagLabel = new JLabel();
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        flagLabel.setBounds(300, 160, 200, 120);
        add(flagLabel);

        // Country Info
        countryLabel = createInfoLabel("");
        countryLabel.setBounds(290, 300, 200, 20);
        add(countryLabel);

        capitalLabel = createInfoLabel("");
        capitalLabel.setBounds(290, 325, 200, 20);
        add(capitalLabel);

        languageLabel = createInfoLabel("");
        languageLabel.setBounds(290, 350, 200, 20);
        add(languageLabel);

        currencyLabel = createInfoLabel("");
        currencyLabel.setBounds(290, 375, 200, 20);
        add(currencyLabel);

        // Navigation Buttons
        ImageButton prevBtn = new ImageButton("/images/prev_button.png", 100, 70);
        prevBtn.setBounds(180, 410, 190, 100);
        prevBtn.addActionListener(e -> showPreviousCountry());
        add(prevBtn);

        ImageButton nextBtn = new ImageButton("/images/next2.png", 100, 70);
        nextBtn.setBounds(420, 410, 190, 100);
        nextBtn.addActionListener(e -> showNextCountry());
        add(nextBtn);

    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
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
}
