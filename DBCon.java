import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBCon {
    private final String url = "jdbc:mysql://localhost:3306/db_tictactoe";
    private final String username = "root";
    private final String password = "";
    private String name1, name2;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void start(MainFrame mainFrame, String playerName1, String playerName2, String selectedTime) {
        try {          
            
            updateDatabase(playerName1);
            updateDatabase(playerName2);

            String dbPlayerName1 = getPlayerName(playerName1);
            String dbPlayerName2 = getPlayerName(playerName2);
            
            name1 = dbPlayerName1;
            name2 = dbPlayerName2;

            System.out.println("Player 1: " + dbPlayerName1);
            System.out.println("Player 2: " + dbPlayerName2);
            System.out.println("Play Time: " + selectedTime + " seconds");

            JOptionPane.showMessageDialog(
                null,
                "Player 1: " + dbPlayerName1 + "\nPlayer 2: " + dbPlayerName2 + "\nPlay Time: " + selectedTime + " seconds",
                "Game Info",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Panggil PlayBoard setelah JOptionPane
            PlayBoard playBoard = new PlayBoard(mainFrame, dbPlayerName1, dbPlayerName2, selectedTime);
            playBoard.setName(dbPlayerName1);

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

    public String getPlayerName(String playerName) throws SQLException {
        String retrievedName = null;
        String query = "SELECT name FROM players WHERE name = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, playerName);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                retrievedName = resultSet.getString("name");
            }
        }

        return retrievedName != null ? retrievedName : playerName;
    }

    public String getName1(){
        // String dbPlayerName1 = getPlayerName(name1);
        return name1;
    }

    public String getName2(){
        // String dbPlayerName2 = getPlayerName(name2);
        return name2;
    }
}
