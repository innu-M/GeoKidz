package com.countryquiz.view.panel;

import com.countryquiz.controller.GameController;
import com.countryquiz.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private JTable leaderboardTable;
    private JComboBox<String> sortCombo;
    private GameController gameController;

    public LeaderboardPanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Sort options panel
        String[] sortOptions = {"Total Score", "Name", "Flags", "Capitals", "Currencies", "Languages"};
        sortCombo = new JComboBox<>(sortOptions);
        sortCombo.addActionListener(e -> updateLeaderboard());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Sort by:"));
        topPanel.add(sortCombo);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        leaderboardTable = new JTable();
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        leaderboardTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initial data load
        updateLeaderboard();
    }

    private void updateLeaderboard() {
        List<User> users = gameController.getUserDatabase().getAllUsers();
        String sortBy = (String) sortCombo.getSelectedItem();

        // Sort users based on selection
        switch (sortBy) {
            case "Name":
                users.sort(Comparator.comparing(User::getUsername));
                break;
            case "Flags":
                users.sort((u1, u2) -> Integer.compare(u2.getLevelScore(1), u1.getLevelScore(1)));
                break;
            case "Capitals":
                users.sort((u1, u2) -> Integer.compare(u2.getLevelScore(2), u1.getLevelScore(2)));
                break;
            case "Currencies":
                users.sort((u1, u2) -> Integer.compare(u2.getLevelScore(3), u1.getLevelScore(3)));
                break;
            case "Languages":
                users.sort((u1, u2) -> Integer.compare(u2.getLevelScore(4), u1.getLevelScore(4)));
                break;
            default: // Total Score
                users.sort((u1, u2) -> Integer.compare(
                        u2.getLevelScore(1) + u2.getLevelScore(2) + u2.getLevelScore(3) + u2.getLevelScore(4),
                        u1.getLevelScore(1) + u1.getLevelScore(2) + u1.getLevelScore(3) + u1.getLevelScore(4)
                ));
        }

        // Create table model
        String[] columns = {"Rank", "Username", "Total", "Flags", "Capitals", "Currencies", "Languages"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Populate table
        int rank = 1;
        for (User user : users) {
            model.addRow(new Object[]{
                    rank++,
                    user.getUsername(),
                    user.getLevelScore(1) + user.getLevelScore(2) + user.getLevelScore(3) + user.getLevelScore(4),
                    user.getLevelScore(1) + "/10",
                    user.getLevelScore(2) + "/10",
                    user.getLevelScore(3) + "/10",
                    user.getLevelScore(4) + "/10"
            });
        }

        leaderboardTable.setModel(model);

        // Set column widths
        leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Rank
        leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Username
        leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Total
        for (int i = 3; i < 7; i++) {
            leaderboardTable.getColumnModel().getColumn(i).setPreferredWidth(90); // Level scores
        }
    }
}