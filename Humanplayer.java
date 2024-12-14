import java.sql.SQLException;

public class Humanplayer extends Players{
    
    Humanplayer(String nama){
        super(nama);
        DBCon dbCon = new DBCon();
        try {
            dbCon.addToDb(nama);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
}
