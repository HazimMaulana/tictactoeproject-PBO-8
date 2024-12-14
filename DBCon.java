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

    // Method untuk mendapatkan koneksi database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void addToDb(String playerName) throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/db_tictactoe";
        String dbUser = "root";
        String dbPassword = "";
    
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Cek apakah pemain sudah ada dalam database
            String checkQuery = "SELECT name FROM players WHERE name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, playerName);
            
            ResultSet resultSet = checkStmt.executeQuery();
            if (!resultSet.next()) {
                // Jika pemain belum ada, tambahkan ke dalam database
                String insertQuery = "INSERT INTO players (name, wins) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, playerName);
                insertStmt.setInt(2, 0); // Set wins awal sebagai 0
                insertStmt.executeUpdate();
                System.out.println("Pemain baru ditambahkan: " + playerName);
            } else {
                System.out.println("Pemain sudah ada: " + playerName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Error saat menambahkan pemain ke database", ex);
        }
    }
    

    // Method untuk memulai permainan
    public void start(MainFrame mainFrame, String playerName1, String playerName2, String selectedTime) throws SQLException {
        try {
            // Update pemain ke database
            updateDatabase(playerName1);
            updateDatabase(playerName2);

            // Ambil nama pemain dari database
            String dbPlayerName1 = getPlayerName(playerName1);
            String dbPlayerName2 = getPlayerName(playerName2);

            // Simpan nama pemain untuk referensi
            name1 = dbPlayerName1;
            name2 = dbPlayerName2;

            // Tampilkan informasi permainan
            JOptionPane.showMessageDialog(
                null,
                "Player 1: " + dbPlayerName1 + "\nPlayer 2: " + dbPlayerName2 + "\nPlay Time: " + selectedTime + " seconds",
                "Game Info",
                JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("Berhasil simpan nama");
            
            PlayBoard playBoard =  new PlayBoard(mainFrame, dbPlayerName1, dbPlayerName2, selectedTime);
            playBoard.setName(dbPlayerName1);
            

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

    // Method untuk memperbarui database dengan pemain baru atau skor
    public void updateDatabase(String playerName) throws SQLException {
        String queryCheck = "SELECT wins FROM players WHERE name = ?";
        String queryInsert = "INSERT INTO players (name, wins) VALUES (?, 0)";
        String queryUpdate = "UPDATE players SET wins = wins + 1 WHERE name = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement checkStmt = connection.prepareStatement(queryCheck);
            checkStmt.setString(1, playerName);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                // Jika pemain sudah ada, tingkatkan skor
                PreparedStatement updateStmt = connection.prepareStatement(queryUpdate);
                updateStmt.setString(1, playerName);
                updateStmt.executeUpdate();
            } else {
                // Jika pemain belum ada, tambahkan ke database
                PreparedStatement insertStmt = connection.prepareStatement(queryInsert);
                insertStmt.setString(1, playerName);
                insertStmt.executeUpdate();
            }
        }
    }

    // Method untuk mendapatkan nama pemain dari database
    public String getPlayerName(String playerName) throws SQLException {
        String query = "SELECT name FROM players WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, playerName);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        }
        return playerName; // Jika nama tidak ditemukan, kembalikan nama input
    }
}
