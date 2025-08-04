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
        setLayout(null); // ❗ Use absolute positioning
        initUI(onRegister, onBack, toggleMusic);
    }

    private void initUI(Runnable onRegister, Runnable onBack, Runnable toggleMusic) {
        // Back Button
        BackButton backBtn = new BackButton(onBack);
        backBtn.setBounds(20, 20, 190, 100); // x, y, width, height
        add(backBtn);

        // Title Label
        JLabel title = new JLabel("Register");
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        title.setForeground(Color.BLACK);
        title.setBounds(320, 120, 250, 50); // Centered-ish
        add(title);

        // Username Label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userLabel.setBounds(210, 200, 120, 30);
        add(userLabel);

        // Username Field
        usernameField = new JTextField();
        usernameField.setBounds(330, 200, 200, 30);
        add(usernameField);

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passLabel.setBounds(210, 250, 120, 30);
        add(passLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(330, 250, 200, 30);
        add(passwordField);

        // Confirm Password Label
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        confirmPassLabel.setBounds(154, 300, 170, 30);
        add(confirmPassLabel);

        // Confirm Password Field
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(330, 300, 200, 30);
        add(confirmPasswordField);

        // Register Button
        TextOverlayButton registerBtn = new TextOverlayButton("", "/images/register.png");
        registerBtn.setBounds(310, 400, 180, 70); // Change width & height here
        registerBtn.addActionListener(e -> {
            if (validateInput()) {
                onRegister.run();
            }
        });
        add(registerBtn);
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
