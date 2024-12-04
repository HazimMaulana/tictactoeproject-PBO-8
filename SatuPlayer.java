import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.sql.*;

public class SatuPlayer extends JFrame {
    private Image backgroundImage;

    public SatuPlayer() {
        setTitle("1 Player");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        backgroundImage = Toolkit.getDefaultToolkit().getImage("image/background.jpg");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        JLabel titleLabel = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 100));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    
        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 120));
        wrapPanel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(400, 300));
        inputPanel.setMaximumSize(new Dimension(400, 300));

        JLabel namaLabel = new JLabel("PLAYER NAME");
        namaLabel.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        namaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        namaLabel.setForeground(Color.WHITE);
        
        JTextField namePlayer = new JTextField(30);
        namePlayer.setMaximumSize(new Dimension(300, 40));
        namePlayer.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
        namePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("SELECT PLAY TIME (SECOND)");
        timeLabel.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(Color.WHITE);

        JComboBox<String> timePlay = new JComboBox<>(new String[]{"30", "45", "60", "90", "120"});
        timePlay.setMaximumSize(new Dimension(300, 40));
        timePlay.setFont(new Font("Bebas Neue", Font.PLAIN, 20));
        timePlay.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        startButton.setMaximumSize(new Dimension(130, 40));
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.WHITE);

        JButton backButton = new JButton("BACK");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Bebas Neue", Font.BOLD, 24));
        backButton.setMaximumSize(new Dimension(130, 40));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        JPanel buttonSelect = new JPanel();
        buttonSelect.setOpaque(false);

        buttonSelect.add(backButton);
        buttonSelect.add(startButton);

        startButton.addActionListener(e -> {
            String playerName = namePlayer.getText().trim();
            String selectedTime = (String) timePlay.getSelectedItem();

            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    updateDatabase(playerName);
                    JOptionPane.showMessageDialog(this, "Player: " + playerName + "\nTime: " + selectedTime + " seconds");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            Beranda beranda = new Beranda();
            beranda.setVisible(true);
        });

        hoverButton(backButton);
        hoverButton(startButton);

        inputPanel.add(namaLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(namePlayer);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(timeLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(timePlay);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        inputPanel.add(buttonSelect);
        
        wrapPanel.add(inputPanel);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(wrapPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void updateDatabase(String playerName) throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/db_tictactoe";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String checkQuery = "SELECT wins FROM players WHERE name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, playerName);

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                int currentWins = resultSet.getInt("wins");
                String updateQuery = "UPDATE players SET wins = ? WHERE name = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentWins + 1);
                updateStmt.setString(2, playerName);
                updateStmt.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO players (name, wins) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, playerName);
                insertStmt.setInt(2, 1); // Default wins = 1
                insertStmt.executeUpdate();
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
