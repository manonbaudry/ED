import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class SpotifyFeed {
    private Connection connection;
    private final String url = "jdbc:oracle:thin:@oracle.fil.univ-lille1.fr:1521:filora";

    private Statement statement;
    private CallableStatement insert ;

    public SpotifyFeed(String login, String pwd) {
        try {
            connection = DriverManager.getConnection(url, login, pwd);
            statement = connection.createStatement();
        }catch (SQLException e){  
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    
    public void insertTracks(Track track) {
    	try {
    		int position = track.getPosition();
    		String track_name = track.getName();
    		String artist = track.getArtist();
    		int streams = track.getNbStreams();
    		String date = track.getDate();
    		
    		String query ="INSERT INTO TRACKS VALUES("+position+",\'"+track_name+"\',\'"+artist+"\',"+streams+",to_date(\'"+date+"\','DD-MM-YYYY'))";
    		
    		System.out.println("togo");
    		//statement.executeUpdate("INSERT INTO TRACKS VALUES(17,'River (feat. Ed Sheeran)','Eminem',77227,to_date('09/01/2018','MM-DD-YYYY'))");
    		statement.executeUpdate(query);
    		System.out.println("ok");
    	}catch (SQLException e) {
    		e.printStackTrace();
		}			
    }

    /**public void updateSpotify(String position, String track_name, String artist, String streams, String date) {
    	try {
			String query = 
					"update SID_VENTE\n" + 
					"SET \n" + 
					"ar_id = \n" + 
					"(Select ARTISTE.ar_id where ARTISTE.ar_nom ="+ artist + "\n" + 
					"FROM SID_Artiste ARTISTE \n" + 
					"),\n" + 
					"nb_titre_top_200 = \n" + 
					"(\n" + 
					"SELECT COUNT(*)\n" + 
					"FROM TRACK\n" + 
					"where TRACK.date = date_vente \n" + 
					"	AND TRACK.position <=200\n" + 
					"),\n" + 
					"max_stream =\n" + 
					"(\n" + 
					"SELECT MAX(TRACK.streams)\n" + 
					"FROM TRACK  T\n" + 
					"WHERE T.date = date_vente \n" + 
					"AND  ( SELECT streams from TRACK where track_name IN\n" + 
					"(SELECT en_titre from SIO_enregistrement ENREGISTREMENT \n" + 
					"INNER JOIN SIO_contient CONTIENT on ENREGISTREMENT.en_id = CONTIENT.en_ID \n" + 
					"AND CONTIENT.al_id = THIS.al_id\n" + 
					") \n" + 
					")\n" + 
					"),\n" + 
					"total_stream =\n" + 
					"(SELECT SUM(TRACK.streams) \n" + 
					"FROM TRACK  T\n" + 
					"where T.date = date_vente \n" + 
					"AND  (SELECT streams from TRACK where track_name IN\n" + 
					"(SELECT en_titre from SIO_enregistrement ENREGISTREMENT \n" + 
					"INNER JOIN SIO_contient CONTIENT on ENREGISTREMENT.en_id = CONTIENT.en_ID \n" + 
					"AND CONTIENT.al_id = THIS.al_id\n" + 
					") \n" + 
					")\n" + 
					");\n" + 
					"\n";

    	 }catch (SQLException e){
             e.printStackTrace();
         }
    }*/
}
