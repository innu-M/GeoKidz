package com.countryquiz.view.panel;

import com.countryquiz.view.components.*;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends BackgroundPanel {
    public WelcomePanel(Runnable onStartAction, Runnable toggleMusicAction) {
        super("/images/background.jpg");

        // Disable layout manager for absolute positioning
        setLayout(null);

        // Create Start Button with explicit size
        ImageButton startBtn = new ImageButton(
                "/images/getstarted.png",
                "/images/getstarted.png",
                220, 120,
                e -> onStartAction.run()
        );
        // Set exact position and size: x=275, y=350, width=250, height=100
        startBtn.setBounds(280, 240, 250, 200);
        add(startBtn);

        // Create Music Toggle Button and position it explicitly
        MusicToggleButton musicBtn = new MusicToggleButton(toggleMusicAction);
        musicBtn.setBounds(10, 10, 150, 40);  // Adjust size and position as needed
        add(musicBtn);
    }
}
