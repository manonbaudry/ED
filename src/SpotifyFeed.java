import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpotifyFeed {
    private Connection connection;
    private final String url = "jdbc:oracle:thin:@oracle.fil.univ-lille1.fr:1521:filora";

    private PreparedStatement statement;
    private CallableStatement insert ;
	private Map<String, Integer> artists;
	private List<Track> tracks;

    public SpotifyFeed(String login, String pwd) {
        try {
            connection = DriverManager.getConnection(url, login, pwd);
            connection.createStatement();
			this.artists = getArtists();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
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

	public void updateColumnArId() {
		// Pour chaque album de la bdd
		for (Integer id : getAlbums().keySet()) {
			// On récupére l'artiste qui apparaît le plus dans les enregistrements de
			// l'album
			String artist = getPrincipalArtist(id);
			if (artist != null) {
				int id_artist = getArtists().get(artist);
				try {
					String query = "update SID_VENTE set ar_id = ? where al_id = ?";
					this.statement = this.connection.prepareStatement(query);
					this.statement.setInt(1, id_artist);
					this.statement.setInt(2, id);
					ResultSet rs = this.statement.executeQuery();
					rs.close();
					this.statement.close();
					System.out.println("Ligne mis à jour pour l'album : " + id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

		public Map<Integer, String> getAlbums () {
			HashMap<Integer, String> albums = new HashMap<Integer, String>();
			String query = "SELECT al_id,al_titre FROM SID_ALBUM";
			try {
				this.statement = this.connection.prepareStatement(query);
				ResultSet rs = this.statement.executeQuery();
				while (rs.next()) {
					String titre = new String(rs.getString("al_titre").getBytes("ISO-8859-1"), "UTF-8");
					int id = rs.getInt("al_id");
					albums.put(id, titre);
				}
				rs.close();
				this.statement.close();
				return albums;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	public String getPrincipalArtist(int idAlbum) {
		Map<String, Integer> enregistrements = getEnregistrements(idAlbum);
		HashMap<String, Integer> musicCount = new HashMap<String, Integer>();
		for (String enregistrement : enregistrements.keySet()) {
			Track track = getMusicFromTrackName(enregistrement);
			if (track != null) {
				String artist = getMusicFromTrackName(enregistrement).getArtist();
				if (musicCount.containsKey(artist)) {
					musicCount.put(artist, musicCount.get(artist) + 1);
				} else {
					musicCount.put(artist, 1);
				}
			}
		}
		int k = 0;
		String a_artist = null;
		for (String artist : musicCount.keySet()) {
			if (musicCount.get(artist) > k) {
				k = musicCount.get(artist);
				a_artist = artist;
			}
		}
		if (a_artist == null) {
			try {
				String query =
						"select ar_id from SIO_ENREGISTREMENT en " +
						"join sio_contient co on en.en_id = co.en_id join sio_interprete inte on inte.en_id = en.en_id " +
						"where al_id = ?";
				this.statement = this.connection.prepareStatement(query);
				this.statement.setInt(1, idAlbum);
				ResultSet rs = this.statement.executeQuery();
				rs.next();
				int id = rs.getInt("ar_id");
				rs.close();
				this.statement.close();
				for (String art : getArtists().keySet()) {
					if (getArtists().get(art) == id) {
						return art;
					}
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		} else {
			return a_artist;
		}
	}

	public Map<String, Integer> getEnregistrements(int idAlbum) {
		HashMap<String, Integer> enregistrements = new HashMap<String, Integer>();
		String query = "select en.en_id, en.en_titre, al_id from SIO_ENREGISTREMENT en join sio_contient co on en.en_id = co.en_id where al_id = ?";
		try {
			this.statement = this.connection.prepareStatement(query);
			this.statement.setInt(1, idAlbum);
			ResultSet rs = this.statement.executeQuery();
			while (rs.next()) {
				String titre = new String(rs.getString("en_titre").getBytes("ISO-8859-1"), "UTF-8");
				int id = rs.getInt("en_id");
				enregistrements.put(titre, id);
			}
			rs.close();
			this.statement.close();
			return enregistrements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Track getMusicFromTrackName(String track) {
		for (Track currentTrack : this.tracks) {
			if (currentTrack.getName().equalsIgnoreCase(track)) {
				return currentTrack;
			}
		}
		return null;
	}

	public void updateTitlesAndNbStreams() {
		HashMap<Integer, List< Date>> album_date = album_date_vente();
		for (Integer idAlbum : album_date.keySet()) {
			List< Date> list = album_date.get(idAlbum);
			for ( Date date : list) {
				int nb_titre_200 = nb_titres_top_200(idAlbum, date);
				int maxStream = max_stream(idAlbum, date);
				int totalStream = total_stream(idAlbum, date);
				try {
					String query = "update SID_VENTE set nb_titres_top_200 = ? , max_stream = ? , total_stream = ? where al_id = ? and date_vente = ?";
					this.statement = this.connection.prepareStatement(query);
					this.statement.setInt(1, nb_titre_200);
					this.statement.setInt(2, maxStream);
					this.statement.setInt(3, totalStream);
					this.statement.setInt(4, idAlbum);
					this.statement.setDate(5, (java.sql.Date) date);
					ResultSet rs = this.statement.executeQuery();
					rs.close();
					this.statement.close();
					System.out.println("Ligne mis à jour pour l'album : " + idAlbum);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public HashMap<Integer, List<Date>> album_date_vente() {
		String query = "select al_id , date_vente from SID_VENTE";
		HashMap<Integer, List< Date>> map = new HashMap<Integer, List< Date>>();
		try {
			this.statement = this.connection.prepareStatement(query);
			ResultSet rs = this.statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("al_id");
				 Date date = rs.getDate("date_vente");
				if (!map.containsKey(id)) {
					ArrayList< Date> list = new ArrayList< Date>();
					list.add(date);
					map.put(id, list);
				} else {
					ArrayList<Date> list = (ArrayList<Date>) map.get(id);
					if (!list.contains(date)) {
						list.add(date);
					}
					map.replace(id, list);
				}
			}
			rs.close();
			this.statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	public int nb_titres_top_200(int idAlbum, Date date) {
		Map<String, Integer> enregistrement = getEnregistrements(idAlbum);
		List<Track> fromDate = getFromDate(date);
		int nb_titre = 0;
		for (Track track : fromDate) {
			if (enregistrement.containsKey(track.getName())) {
				nb_titre++;
			}
		}
		return nb_titre;
	}

	public List<Track> getFromDate(Date date) {
		List<Track> tracks = new ArrayList<>();
		for (Track track : this.tracks) {
			if (track.getDate().equals(date)) {
				tracks.add(track);
			}
		}
		return tracks;
	}

	public int max_stream(int idAlbum, Date date) {
		Map<String, Integer> enregistrement = getEnregistrements(idAlbum);
		List< Track> fromDate = getFromDate(date);
		int max = 0;
		for ( Track  track : fromDate) {
			if (enregistrement.containsKey( track.getName())) {
				if ( track.getNbStreams() > max) {
					max =  track.getNbStreams();
				}
			}
		}
		return max;
	}

	public int total_stream(int idAlbum, Date date) {
		Map<String, Integer> enregistrement = getEnregistrements(idAlbum);
		List< Track> fromDate = getFromDate(date);
		int total = 0;
		for ( Track track : fromDate) {
			if (enregistrement.containsKey(track.getName())) {
				total += track.getNbStreams();
			}
		}
		return total;
	}
}
