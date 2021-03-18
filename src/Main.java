
import java.util.Scanner;

public class Main {

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
			spotifyFeed.setTracks(csvReader.parseData(spotifyFeed.getArtists()));
			spotifyFeed.updateColumnArId();
			spotifyFeed.updateTitlesAndNbStreams();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			spotifyFeed.disconnect();
		}



	}
}
