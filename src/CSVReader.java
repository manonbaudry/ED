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

    public HashMap parseData(){
        try {
            HashMap<Integer, Track> tracks = new HashMap<Integer, Track>();
            String line;
            boolean isFirstLine = true;
            while((line = bufferedReader.readLine()) != null) {
                if(isFirstLine){
                    isFirstLine = false;
                }else{
                    String[] data = line.split(";");
                    Track track = new Track(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), new SimpleDateFormat("yyyy-MM-dd").parse(data[4]));
                    tracks.put(Integer.parseInt(data[0]), track);
                }
            }
            bufferedReader.close();
            return tracks;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
