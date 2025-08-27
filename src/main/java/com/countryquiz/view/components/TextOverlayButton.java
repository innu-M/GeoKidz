package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.*;

public class TextOverlayButton extends JButton {
    public TextOverlayButton(String text, String imagePath) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.BLACK);
        setFont(new Font("Arial", Font.BOLD, 14));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            if (icon.getImage() == null) {
                throw new RuntimeException("Image not found: " + imagePath);
            }
            setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 95, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            // Fallback to default button if image fails to load
            System.err.println("Error loading button image: " + e.getMessage());
            setBackground(new Color(150, 75, 0)); // Fallback color
        }
    }

//    public TextOverlayButton(String text, int width, int height) {
//        this(text, "/images/button_bg.png"); // Use default image
//        // Resize if different dimensions are requested
//        if (width != 150 || height != 25) {
//            ImageIcon icon = (ImageIcon) getIcon();
//            if (icon != null) {
//                setIcon(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
//            }
//        }
//    }
}