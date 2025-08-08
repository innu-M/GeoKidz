package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.view.components.*;

import javax.swing.*;
import java.awt.*;

public class LevelOnePanel extends BackgroundPanel {
    public LevelOnePanel(GameController gameController, Runnable onBack, Runnable onLearn, Runnable onQuiz) {
        super("/images/allbg.png");
        setLayout(null); // Absolute positioning

        // Back Button
        ImageButton backBtn = new ImageButton("/images/back_button.png", 150, 80);
        backBtn.setBounds(80, 300, 150, 80);
        backBtn.addActionListener(e -> onBack.run());
        add(backBtn);

        // Learn Button
        ImageButton learnBtn = new ImageButton("/images/learn_button.png", 150, 80);
        learnBtn.setBounds(320, 300, 150, 80);
        learnBtn.addActionListener(e -> onLearn.run());
        add(learnBtn);

        // Quiz Button
        ImageButton quizBtn = new ImageButton("/images/quiz_button.png", 150, 80);
        quizBtn.setBounds(560, 300, 150, 80);
        quizBtn.addActionListener(e -> onQuiz.run());
        add(quizBtn);
    }
}
