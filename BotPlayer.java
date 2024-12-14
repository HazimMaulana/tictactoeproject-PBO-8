import java.sql.SQLException;

public class BotPlayer extends Players{
    
    BotPlayer(String nama){
        super(nama);
        DBCon dbCon = new DBCon();
        try {
            dbCon.addToDb(nama);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    public void play(MainFrame main, String nama1, String nama2, String time) {
        PlayBoard play = new PlayBoard(main, nama1, nama2, time);
    }
}
