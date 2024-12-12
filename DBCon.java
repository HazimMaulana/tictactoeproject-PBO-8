import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class DBCon {
    private final String url = "jdbc:mysql://localhost:3306/db_tictactoe";
    private final String username = "root";
    private final String password = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void start(MainFrame mainFrame, String playerName1, String playerName2, String selectedTime) {
        try {
            updateDatabase(playerName1);
            updateDatabase(playerName2);
    
            System.out.println("Player 1: " + playerName1);
            System.out.println("Player 2: " + playerName2);
            System.out.println("Play Time: " + selectedTime + " seconds");
    
            JOptionPane.showMessageDialog(
                null,
                "Player 1: " + playerName1 + "\nPlayer 2: " + playerName2 + "\nPlay Time: " + selectedTime + " seconds",
                "Game Info",
                JOptionPane.INFORMATION_MESSAGE
            );
    
            // Panggil PlayBoard setelah JOptionPane
            PlayBoard playBoard = new PlayBoard(mainFrame, playerName1, playerName2, selectedTime);
            // playBoard.setVisible(true);    
        } catch (SQLException ex) {
            ex.printStackTrace();
    
            JOptionPane.showMessageDialog(
                null,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
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
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();
            }
        }
    }
}
