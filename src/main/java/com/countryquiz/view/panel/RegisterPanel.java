package com.countryquiz.view.panel;

import com.countryquiz.view.components.*;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends BackgroundPanel {
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(Runnable onRegister, Runnable onBack, Runnable toggleMusic) {
        super("/images/allbg.png");
        initUI(onRegister, onBack, toggleMusic);
    }

    private void initUI(Runnable onRegister, Runnable onBack, Runnable toggleMusic) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back Button
        BackButton backBtn = new BackButton(onBack);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(backBtn, gbc);

        // Title
        JLabel title = new JLabel("Register");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Username Field
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Confirm Password Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        add(confirmPasswordField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        TextOverlayButton registerBtn = new TextOverlayButton("","/images/register.png");
        registerBtn.addActionListener(e -> {
            if (validateInput()) {
                onRegister.run();
            }
        });
        add(registerBtn, gbc);
    }

    private boolean validateInput() {
        if (getUsername().isEmpty() || getPassword().isEmpty()) {
            showError("Username and password cannot be empty");
            return false;
        }
        if (!getPassword().equals(getConfirmPassword())) {
            showError("Passwords do not match");
            return false;
        }
        if (getPassword().length() < 4) {
            showError("Password must be at least 4 characters");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static String getUsername() {
        return usernameField.getText().trim();
    }

    public static String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword()).trim();
    }
}