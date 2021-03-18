import java.io.Console;
import java.util.HashMap;

public class Main {
	SpotifyFeed spotifyFeed;
	
	public static void main(String[] args) {
		SpotifyFeed spotifyFeed=null;
		try {
			spotifyFeed = new SpotifyFeed("DEFEVER_ED", "un_mot_de_passe");
			CSVReader csvReader = new CSVReader("src/france.csv");
			HashMap<Integer, Track> tracks = csvReader.parseData();
			System.out.println(tracks.size());
			for(int id : tracks.keySet()){
				spotifyFeed.insertTracks(tracks.get(id));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			spotifyFeed.disconnect();
		}



	}
}
