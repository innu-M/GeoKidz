package com.countryquiz.view.panel;

import com.countryquiz.view.components.*;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends BackgroundPanel {
    private static JTextField usernameField;
    private static JPasswordField passwordField;

    public LoginPanel(Runnable onLogin, Runnable onRegister, Runnable onBack) {
        super("/images/allbg.png");
        initUI(onLogin, onRegister, onBack);
    }

    private void initUI(Runnable onLogin, Runnable onRegister, Runnable onBack) {
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
        JLabel title = new JLabel("");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
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

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        TextOverlayButton loginBtn = new TextOverlayButton("","/images/login.png");
        loginBtn.addActionListener(e -> {
            if (validateInput()) {
                SwingUtilities.invokeLater(() -> {
                    onLogin.run(); // Ensure this runs on EDT
                });
            }
        });
        add(loginBtn, gbc);

        // Register Button
        gbc.gridy = 5;
        TextOverlayButton registerBtn = new TextOverlayButton("","/images/register.png");
        registerBtn.addActionListener(e -> onRegister.run());
        add(registerBtn, gbc);
    }

    private boolean validateInput() {
        if (getUsername().isEmpty() || getPassword().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public static String getUsername() {
        return usernameField.getText().trim();
    }

    public static String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }
}