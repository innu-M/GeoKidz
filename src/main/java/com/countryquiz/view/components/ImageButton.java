// src/main/java/com/countryquiz/view/components/ImageButton.java
package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ImageButton extends JButton {
    private ImageIcon normalIcon;
    private ImageIcon hoverIcon;

    public ImageButton(String normalImagePath, String hoverImagePath, ActionListener action) {
        loadImages(normalImagePath, hoverImagePath);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        addActionListener(action);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (hoverIcon != null) setIcon(hoverIcon);
            }

            public void mouseExited(MouseEvent e) {
                if (normalIcon != null) setIcon(normalIcon);
            }
        });
    }

    public ImageButton(String normalImagePath, int width, int height) {
        loadImages(normalImagePath, normalImagePath);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        if (normalIcon != null) {
            setIcon(new ImageIcon(normalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        } else {
            setText("Button"); // Fallback text
        }

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (hoverIcon != null) setIcon(hoverIcon);
            }

            public void mouseExited(MouseEvent e) {
                if (normalIcon != null) setIcon(normalIcon);
            }
        });
    }

    private void loadImages(String normalImagePath, String hoverImagePath) {
        try {
            URL normalUrl = getClass().getResource(normalImagePath);
            if (normalUrl == null) {
                throw new RuntimeException("Image not found: " + normalImagePath);
            }
            normalIcon = new ImageIcon(normalUrl);

            // Only load hover image if it's different
            if (!normalImagePath.equals(hoverImagePath)) {
                URL hoverUrl = getClass().getResource(hoverImagePath);
                if (hoverUrl == null) {
                    throw new RuntimeException("Image not found: " + hoverImagePath);
                }
                hoverIcon = new ImageIcon(hoverUrl);
            } else {
                hoverIcon = normalIcon;
            }

            setIcon(normalIcon);
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            // Fallback to text button if images fail to load
            setText("Button");
        }
    }
}