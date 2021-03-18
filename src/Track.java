import java.util.Date;

/**
 * represents information about music in Spotify
 */
public class Track {
    private int position;
    private String name;
    private String artist;
    private int nbStreams;
    private String date;
    private int id;

    public Track(int position, String name, String artist, int nbStreams, String date) {
        this.position = position;
        this.name = name;
        this.artist = artist;
        this.nbStreams = nbStreams;
        this.date = date;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getNbStreams() {
        return nbStreams;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Track{" +
                "position=" + position +
                ", name=" + name + ' ' +
                ", artist=" + artist + ' ' +
                ", nbStreams=" + nbStreams +
                ", date=" + date +
                '}';
    }
}
