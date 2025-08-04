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
        setLayout(null); // ✅ Absolute positioning

        // Back Button
        BackButton backBtn = new BackButton(onBack);
        backBtn.setBounds(20, 20, 190, 100); // x, y, width, height
        add(backBtn);

        // Title (optional)
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Tahoma", Font.BOLD, 32));
        title.setForeground(Color.black);
        title.setBounds(350, 100, 200, 40);
        add(title);

        // Username Label and Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.black);
        userLabel.setBounds(250, 200, 100, 40);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(320, 200, 200, 40);
        add(usernameField);

        // Password Label and Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.black);
        passLabel.setBounds(250, 260, 100, 40);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(320, 260, 200, 40);
        add(passwordField);

        // Login Button
        TextOverlayButton loginBtn = new TextOverlayButton("", "/images/login.png");
        loginBtn.setBounds(170, 360, 150, 90); // ← Change x, y, width, height
        loginBtn.addActionListener(e -> {
            if (validateInput()) {
                SwingUtilities.invokeLater(onLogin);
            }
        });
        add(loginBtn);

        // Register Button
        TextOverlayButton registerBtn = new TextOverlayButton("", "/images/register.png");
        registerBtn.setBounds(450, 360, 150, 90); // ← Adjust as needed
        registerBtn.addActionListener(e -> onRegister.run());
        add(registerBtn);
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
