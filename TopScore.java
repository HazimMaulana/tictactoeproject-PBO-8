import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class TopScore extends JPanel {
    private Image backgroundImage;

    public TopScore(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/BG.png");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));

        JLabel titleLabel = new JLabel("TOP SCORES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 100));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        wrapPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(600, 600));
        contentPanel.setMaximumSize(new Dimension(600, 600));

        // Create panel for background with transparency and border
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setOpaque(true);
        leaderboardPanel.setBackground(new Color(0, 0, 0, 100));
        leaderboardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        leaderboardPanel.setPreferredSize(new Dimension(650, 350));

        JLabel instructionLabel = new JLabel("Sorted by Wins", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Bebas Neue", Font.BOLD, 30));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardPanel.add(instructionLabel);
        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        try {
            fetchLeaderboard(leaderboardPanel);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JLabel errorLabel = new JLabel("Error fetching leaderboard data", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Bebas Neue", Font.BOLD, 20));
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leaderboardPanel.add(errorLabel);
        }

        JButton backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        backButton.addActionListener(e -> {
            mainFrame.switchToScreen("beranda");
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        backButtonPanel.setOpaque(false); // Supaya transparan sesuai backgroundPanel
        backButtonPanel.add(backButton);

        hoverButton(backButton);
    
        wrapPanel.add(leaderboardPanel);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(wrapPanel, BorderLayout.CENTER);
        backgroundPanel.add(backButtonPanel, BorderLayout.SOUTH);

        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void fetchLeaderboard(JPanel leaderboardPanel) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_tictactoe"; 
        String username = "root";
        String password = "";
        String query = "SELECT name, wins FROM players ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                String playerName = rs.getString("name");
                int wins = rs.getInt("wins");
                String score = rank + ". " + playerName + " - " + wins + " Wins";

                JLabel scoreLabel = new JLabel(score, SwingConstants.CENTER);
                scoreLabel.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
                scoreLabel.setForeground(Color.WHITE);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                leaderboardPanel.add(scoreLabel);
                leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                rank++;
            }
        }
    }

    private void hoverButton(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }
}
