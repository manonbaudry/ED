import java.io.Console;
import java.util.HashMap;

public class Main {
	SpotifyFeed spotifyFeed;
	
	public static void main(String[] args) {
		SpotifyFeed spotifyFeed=null;
		try {
			spotifyFeed = new SpotifyFeed("DESPELCHIN_ED", "origan");
			CSVReader csvReader = new CSVReader("src/france.csv");
			HashMap<Integer, Track> tracks = csvReader.parseData();
			//for (int i = 1; i < tracks.size(); i++) {
			//	System.out.println(i);
			//}
			System.out.println(tracks.size());
			spotifyFeed.insertTracks(tracks.get(1));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			spotifyFeed.disconnect();
		}



	}
}
