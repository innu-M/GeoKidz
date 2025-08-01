// src/main/java/com/countryquiz/view/panel/WelcomePanel.java
package com.countryquiz.view.panel;

import com.countryquiz.view.components.*;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends BackgroundPanel {
    public WelcomePanel(Runnable onStartAction, Runnable toggleMusicAction) {
        super("/images/background.jpg");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
       /* JLabel title = new JLabel("");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);
        */


        // Start Button - Using TextOverlayButton with proper path
        TextOverlayButton startBtn = new TextOverlayButton("", "/images/getstarted.png");
        startBtn.addActionListener(e -> onStartAction.run());
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        add(startBtn, gbc);
        // Music Toggle Button - Using MusicToggleButton instead of ImageButton
        MusicToggleButton musicBtn = new MusicToggleButton(toggleMusicAction);
        gbc.gridy = 2;
        add(musicBtn, gbc);
    }
}