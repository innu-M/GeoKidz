package com.countryquiz;

import com.countryquiz.controller.*;
import com.countryquiz.view.panel.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JPanel {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final GameController gameController;
    private final AudioController audioController;
    private final Map<String, JPanel> panels;

    public Main() {
        gameController = new GameController();
        audioController = new AudioController();
        panels = new HashMap<>();
        initializeFrame();
        createPanels();
        showWelcomePanel();
        audioController.playMusic();
    }

    private void initializeFrame() {
        frame = new JFrame("Country Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false); // 🟡 Fixed-size window
        frame.setLocationRelativeTo(null); // center on screen

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

        LoginPanel loginPanel = new LoginPanel(
                this::handleLogin,
                this::showRegisterPanel,
                this::showWelcomePanel
        );
        panels.put("login", loginPanel);

        RegisterPanel registerPanel = new RegisterPanel(
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

        // Add authenticated panels to main panel
        panels.forEach((name, panel) -> {
            if (!name.equals("welcome") && !name.equals("login") && !name.equals("register")) {
                mainPanel.add(panel, name);
            }
        });
    }

    private void handleLogin() {
        String username = LoginPanel.getUsername();
        String password = LoginPanel.getPassword();

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
        String username = RegisterPanel.getUsername();
        String password = RegisterPanel.getPassword();

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


    private void showQuizPanel(int level) {
        String panelName = "quiz" + level;

        if (!panels.containsKey(panelName)) {
            panels.put(panelName, new QuizPanel(
                    gameController,
                    this::showLevelPanel, // Properly connected back action
                    audioController,
                    level
            ));
            mainPanel.add(panels.get(panelName), panelName);
        }

        cardLayout.show(mainPanel, panelName);
    }


    public void showLevelPanel() {
        // Create fresh level actions
        Runnable[] levelActions = new Runnable[5];
        for (int i = 0; i < 5; i++) {
            final int level = i + 1;
            levelActions[i] = () -> showQuizPanel(level);
        }

        // Create or update the level panel
        LevelPanel levelPanel = new LevelPanel(gameController, levelActions, this::showMenuPanel);
        panels.put("levels", levelPanel);
        mainPanel.add(levelPanel, "levels");

        // Show the panel
        cardLayout.show(mainPanel, "levels");

        // Refresh the panel
        levelPanel.refreshLevels();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Main app = new Main();
                app.frame.setVisible(true); // 🔵 Show the window
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}