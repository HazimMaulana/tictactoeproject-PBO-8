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
    private void updateDatabase(String playerName) throws SQLException {
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

    // Getter untuk nama pemain
    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }
}
