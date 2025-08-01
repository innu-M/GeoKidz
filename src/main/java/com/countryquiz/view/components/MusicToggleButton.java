package com.countryquiz.view.components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicToggleButton extends JToggleButton {
    private boolean isMusicOn = true;

    public MusicToggleButton(Runnable toggleMusic) {
        super("🔊 Music On");  // Initial text/icon

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMusicOn = !isMusicOn;
                setText(isMusicOn ? "🔊 Music On" : "🔇 Music Off");
                toggleMusic.run(); // Callback
            }
        });
    }
}
