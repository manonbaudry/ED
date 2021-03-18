import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	SpotifyFeed spotifyFeed;
	
	public static void main(String[] args) {
		SpotifyFeed spotifyFeed = null;
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.print("login : ");
			String login = scanner.nextLine();
			System.out.print("password : ");
			String pwd = scanner.nextLine();

			spotifyFeed = new SpotifyFeed(login, pwd);
			CSVReader csvReader = new CSVReader("src/france.csv");
			HashMap<Integer, Track> tracks = csvReader.parseData();
			//for (int i = 1; i < tracks.size(); i++) {
			//	System.out.println(i);
			//}
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
