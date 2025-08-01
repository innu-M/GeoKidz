package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {
    private static final String[] IMAGE_PATHS = {
            "/images/back_button.png",
            "images/back_button.png",
            "src/main/resources/images/back_button.png"
    };

    public BackButton(Runnable action) {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        ImageIcon icon = null;
        for (String path : IMAGE_PATHS) {
            try {
                icon = new ImageIcon(getClass().getResource(path));
                if (icon.getImage() != null) break;
            } catch (Exception ignored) {}
        }

        if (icon != null && icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        } else {
            System.err.println("Back button image not found, using text fallback");
            setText("< Back");
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        addActionListener(e -> action.run());
    }
}