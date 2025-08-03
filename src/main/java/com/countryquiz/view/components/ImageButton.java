package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ImageButton extends JButton {
    private ImageIcon normalIcon;
    private ImageIcon hoverIcon;

    // Constructor with resized images and action listener
    public ImageButton(String normalImagePath, String hoverImagePath, int width, int height, ActionListener action) {
        loadAndResizeImages(normalImagePath, hoverImagePath, width, height);
        commonSetup();
        addActionListener(action);
    }

    // Constructor with only one image path and size (same image for hover)
    public ImageButton(String imagePath, int width, int height) {
        loadAndResizeImages(imagePath, imagePath, width, height);
        commonSetup();
    }

    // Constructor with full-size image paths and action listener
    public ImageButton(String normalImagePath, String hoverImagePath, ActionListener action) {
        loadImages(normalImagePath, hoverImagePath);
        commonSetup();
        addActionListener(action);
    }

    // Reused UI setup code
    private void commonSetup() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (hoverIcon != null) setIcon(hoverIcon);
            }

            public void mouseExited(MouseEvent e) {
                if (normalIcon != null) setIcon(normalIcon);
            }
        });
    }

    // Load original-size icons
    private void loadImages(String normalImagePath, String hoverImagePath) {
        try {
            URL normalUrl = getClass().getResource(normalImagePath);
            if (normalUrl == null) throw new RuntimeException("Image not found: " + normalImagePath);
            normalIcon = new ImageIcon(normalUrl);

            if (!normalImagePath.equals(hoverImagePath)) {
                URL hoverUrl = getClass().getResource(hoverImagePath);
                if (hoverUrl == null) throw new RuntimeException("Image not found: " + hoverImagePath);
                hoverIcon = new ImageIcon(hoverUrl);
            } else {
                hoverIcon = normalIcon;
            }

            setIcon(normalIcon);
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            setText("Button");
        }
    }

    // Load and resize icons
    private void loadAndResizeImages(String normalImagePath, String hoverImagePath, int width, int height) {
        try {
            URL normalUrl = getClass().getResource(normalImagePath);
            if (normalUrl == null) throw new RuntimeException("Image not found: " + normalImagePath);
            Image normalImage = new ImageIcon(normalUrl).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            normalIcon = new ImageIcon(normalImage);

            if (!normalImagePath.equals(hoverImagePath)) {
                URL hoverUrl = getClass().getResource(hoverImagePath);
                if (hoverUrl == null) throw new RuntimeException("Image not found: " + hoverImagePath);
                Image hoverImage = new ImageIcon(hoverUrl).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                hoverIcon = new ImageIcon(hoverImage);
            } else {
                hoverIcon = normalIcon;
            }

            setIcon(normalIcon);
        } catch (Exception e) {
            System.err.println("Error loading/resizing images: " + e.getMessage());
            setText("Button");
        }
    }
}
