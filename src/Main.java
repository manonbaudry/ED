import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
			List<Track> tracks = csvReader.parseData(spotifyFeed.getArtists());


		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			spotifyFeed.disconnect();
		}



	}
}
