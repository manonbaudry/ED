import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVReader {
    private BufferedReader bufferedReader;

    public CSVReader(String filePath) {
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Track> parseData(Map<String, Integer> artists){
        try {
            List<Track> tracks = new ArrayList();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                    String[] data = line.split(";");
                if (artists.containsKey(data[2])) {
                    Track track = new Track(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), data[4]);
                    tracks.add(track);
                }
            }
            tracks.remove(1);
            bufferedReader.close();
            return tracks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
