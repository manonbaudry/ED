import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class SpotifyFeed {
    private Connection connection;
    private final String url = "jdbc:oracle:thin:@oracle.fil.univ-lille1.fr:1521:filora";

    private PreparedStatement statement;
    private CallableStatement insert ;
	private Map<String, Integer> artists;

    public SpotifyFeed(String login, String pwd) {
        try {
            connection = DriverManager.getConnection(url, login, pwd);
            connection.createStatement();
			this.artists = getArtists();
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

	public Map<String, Integer> getArtists() {
		HashMap<String, Integer> artists = new HashMap<String, Integer>();
		String query = "SELECT ar_id,ar_nom FROM SID_ARTISTE";
		try {
			this.statement = this.connection.prepareStatement(query);
			ResultSet rs = this.statement.executeQuery();
			while (rs.next()) {
				String artist = rs.getString("ar_nom");
				int id = rs.getInt("ar_id");
				artists.put(artist, id);
			}
			rs.close();
			this.statement.close();
			return artists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


//    public void insertTracks(Track track) {
//    	try {
//    		int position = track.getPosition();
//    		String track_name = track.getName();
//    		String artist = track.getArtist();
//    		int streams = track.getNbStreams();
//    		String date = track.getDate();
//
//
//			//insert = connection.prepareCall(query);
//			insert.execute();
//    	}catch (SQLException e) {
//    		e.printStackTrace();
//		}finally {
//    		disconnect();
//		}
//    }
}
