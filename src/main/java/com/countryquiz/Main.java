package com.countryquiz;

import com.countryquiz.controller.*;
import com.countryquiz.view.panel.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final GameController gameController;
    private final AudioController audioController;
    private final Map<String, JPanel> panels;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;

    public Main() {
        gameController = new GameController();
        audioController = new AudioController();
        panels = new HashMap<>();
        initializeFrame();
        createPanels();
        showWelcomePanel();
        audioController.playMusic();
    }

    private void verifyResources() {
        checkResourceExists("/images/button_bg.png");
        checkResourceExists("/data/countries.json");
    }

    private void checkResourceExists(String path) {
        if (getClass().getResource(path) == null) {
            String msg = "Critical resource missing: " + path;
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(msg);
        }
    }

    private void initializeFrame() {
        frame = new JFrame("Country Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
    }

    private void createPanels() {
        // Unauthenticated panels
        panels.put("welcome", new WelcomePanel(
                this::showLoginPanel,
                audioController::toggleMusic
        ));

        loginPanel = new LoginPanel(
                this::handleLogin,
                this::showRegisterPanel,
                this::showWelcomePanel
        );
        panels.put("login", loginPanel);

        registerPanel = new RegisterPanel(
                this::handleRegister,
                this::showLoginPanel,
                audioController::toggleMusic
        );
        panels.put("register", registerPanel);

        // Add to main panel
        panels.forEach(mainPanel::add);
    }

    private void createAuthenticatedPanels() {
        if (panels.containsKey("menu")) return;

        // Menu Panel
        panels.put("menu", new MenuPanel(
                gameController,
                this::showLevelPanel,
                this::handleLogout,
                audioController
        ));

        // Level Panel
        Runnable[] levelActions = new Runnable[5];
        for (int i = 0; i < 5; i++) {
            final int level = i + 1;
            levelActions[i] = () -> showQuizPanel(level);
        }

        panels.put("levels", new LevelPanel(
                gameController,
                levelActions,
                this::showMenuPanel
        ));

        // Add to main panel
        panels.forEach((name, panel) -> {
            if (!name.equals("welcome") && !name.equals("login") && !name.equals("register")) {
                mainPanel.add(panel, name);
            }
        });
    }

    private void handleLogin() {
        String username = loginPanel.getUsername();
        String password = loginPanel.getPassword();

        if (gameController.login(username, password)) {
            SwingUtilities.invokeLater(() -> {
                createAuthenticatedPanels();
                showMenuPanel();
            });
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = registerPanel.getUsername();
        String password = registerPanel.getPassword();

        if (gameController.register(username, password)) {
            JOptionPane.showMessageDialog(frame,
                    "Registration successful! Please login",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            showLoginPanel();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Username already exists",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        gameController.logout();
        showLoginPanel();
    }

    // Navigation methods
    private void showWelcomePanel() {
        cardLayout.show(mainPanel, "welcome");
        frame.setVisible(true);
    }

    private void showLoginPanel() {
        cardLayout.show(mainPanel, "login");
    }

    private void showRegisterPanel() {
        cardLayout.show(mainPanel, "register");
    }

    private void showMenuPanel() {
        MenuPanel menuPanel = (MenuPanel) panels.get("menu");
        if (menuPanel != null) {
            menuPanel.refreshUI();
        }
        cardLayout.show(mainPanel, "menu");
    }

    private void showLevelPanel() {
        if (gameController.isLoggedIn()) {
            cardLayout.show(mainPanel, "levels");
        } else {
            showLoginPanel();
        }
    }

    private void showQuizPanel(int levelType) {
        String panelName = "quiz" + levelType;
        if (!panels.containsKey(panelName)) {
            panels.put(panelName, new QuizPanel(
                    gameController,
                    this::showLevelPanel,
                    audioController,
                    levelType
            ));
            mainPanel.add(panels.get(panelName), panelName);
        }
        cardLayout.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}