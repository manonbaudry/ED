import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("login : ");
//        String login = scanner.nextLine();
//        System.out.print("password : ");
//        String pwd = scanner.nextLine();
//        SpotifyFeed spotifyFeed = new SpotifyFeed(login, pwd);
        CSVReader csvReader = new CSVReader("src/france.csv");
        HashMap<Integer, Track> tracks = csvReader.parseData();
//        spotifyFeed.disconnect();
    }
}
