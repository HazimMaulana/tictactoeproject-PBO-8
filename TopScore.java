import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class TopScore extends JFrame {
    private Image backgroundImage;

    public TopScore() {
        setTitle("TOP SCORE");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 70));
        wrapPanel.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(600, 600));
        contentPanel.setMaximumSize(new Dimension(600, 600));

        JLabel instructionLabel = new JLabel("Leaderboard (Sorted by Wins)", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Bebas Neue", Font.BOLD, 30));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        try {
            fetchLeaderboard(contentPanel);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JLabel errorLabel = new JLabel("Error fetching leaderboard data", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Bebas Neue", Font.BOLD, 20));
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(errorLabel);
        }

        JButton backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        backButton.addActionListener(e -> {
            dispose();
            Beranda beranda = new Beranda(); 
            beranda.setVisible(true);
        });

        hoverButton(backButton);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(backButton);

        wrapPanel.add(contentPanel);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(wrapPanel, BorderLayout.CENTER);

        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void fetchLeaderboard(JPanel contentPanel) throws SQLException {
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

                contentPanel.add(scoreLabel);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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
