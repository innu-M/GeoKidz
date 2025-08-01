package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image background;

    public BackgroundPanel(String imagePath) {
        this.background = new ImageIcon(getClass().getResource(imagePath)).getImage();
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}