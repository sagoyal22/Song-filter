import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



public class Backend implements BackendInterface {

  // fields
  private IterableSortedCollection<Song> tree;
  private Integer threshold;
  private Integer low;
  private Integer high;
  private static final Comparator<Song> DANCEABILITY_COMPARATOR = new Comparator<Song>() {
    @Override
    public int compare(Song s1, Song s2) {
      return Integer.compare(s1.getDanceability(), s2.getDanceability());
    }
  };

  public Backend(IterableSortedCollection<Song> tree) {
    this.tree = tree;
  }

  @Override
  public void readData(String filename) throws IOException {
    Scanner scanner = null;
    try {
      scanner = new Scanner(new File(filename));
      if (!scanner.hasNextLine()) {
        throw new IOException("Error: The file '" + filename + "' is empty.");
      }

      // Read headers
      String headerLine = scanner.nextLine();
      String[] headers = headerLine.split(",");

      // Create a map to hold column indices dynamically, initialize them to -1
      int titleIndex = -1, artistIndex = -1, genreIndex = -1, yearIndex = -1, bpmIndex = -1;
      int nrgyIndex = -1, dnceIndex = -1, dBIndex = -1, liveIndex = -1, valIndex = -1;
      int durIndex = -1, acousIndex = -1, spchIndex = -1, popIndex = -1;

      // Loop through headers and assign indexes
      for (int i = 0; i < headers.length; i++) {
        String header = headers[i].trim().toLowerCase();
        switch (header) {
          case "title":
            titleIndex = i;
            break;
          case "artist":
            artistIndex = i;
            break;
          case "top genre":
            genreIndex = i;
            break;
          case "year":
            yearIndex = i;
            break;
          case "bpm":
            bpmIndex = i;
            break;
          case "nrgy":
            nrgyIndex = i;
            break;
          case "dnce":
            dnceIndex = i;
            break;
          case "db":
            dBIndex = i;
            break;
          case "live":
            liveIndex = i;
            break;
          case "val":
            valIndex = i;
            break;
          case "dur":
            durIndex = i;
            break;
          case "acous":
            acousIndex = i;
            break;
          case "spch":
            spchIndex = i;
            break;
          case "pop":
            popIndex = i;
            break;
        }
      }

      // Ensure that the required columns exist
      if (titleIndex == -1 || artistIndex == -1 || genreIndex == -1 || yearIndex == -1
          || bpmIndex == -1 || dBIndex == -1) {
        throw new IOException("Missing required columns in CSV file. Check header names.");
      }

      // Read data and create Song objects
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if (columns.length < headers.length) {
          System.err
              .println("Warning: Skipping row due to insufficient columns. Row content: " + line);
          continue;
        }

        // Extract values for each song's attributes
        String title = columns[titleIndex].trim();
        String artist = columns[artistIndex].trim();
        String genre = columns[genreIndex].trim();
        int year = Integer.parseInt(columns[yearIndex].trim());
        int bpm = Integer.parseInt(columns[bpmIndex].trim());
        int nrgy = Integer.parseInt(columns[nrgyIndex].trim());
        int dnce = Integer.parseInt(columns[dnceIndex].trim());
        int dB = Integer.parseInt(columns[dBIndex].trim());
        int live = Integer.parseInt(columns[liveIndex].trim());
        int val = Integer.parseInt(columns[valIndex].trim());
        int dur = Integer.parseInt(columns[durIndex].trim());
        int acous = Integer.parseInt(columns[acousIndex].trim());
        int spch = Integer.parseInt(columns[spchIndex].trim());
        int pop = Integer.parseInt(columns[popIndex].trim());

        // Create a new Song object
        Song song = new Song(title, artist, genre, year, bpm, nrgy, dnce, dB, live,
            DANCEABILITY_COMPARATOR);

        // Insert the song into the collection (tree), using the comparator for sorting
        tree.insert(song);
      }



    } finally {
      if (scanner != null) {
        scanner.close(); // Close scanner manually
      }
    }
  }


  @Override
  public List<String> getRange(Integer low, Integer high) {
    if (low != null && high != null && low > high) {
      return new ArrayList<>();
    }
    this.low = low;
    this.high = high;

    List<String> result = new ArrayList<>();
    System.out.println("Filtering songs: Danceability(" + low + " to " + high
        + "), Speed threshold: " + threshold);

    for (Song song : tree) {
      int danceability = song.getDanceability();
      int bpm = song.getBPM(); // Speed threshold

      boolean checkDanceability =
          (low == null || danceability >= low) && (high == null || danceability <= high);
      boolean checkSpeed = (threshold == null || bpm < threshold);

      if (checkDanceability && checkSpeed) {
        result.add(song.getTitle());
      }
    }

    System.out.println("Songs after filtering: " + result.size());
    return result;
  }

  @Override
  public List<String> filterSongs(Integer threshold) {
    this.threshold = threshold;
    List<String> filteredSongs = new ArrayList<>();
    System.out.println("Filtering songs with Speed threshold: " + threshold);

    for (Song song : tree) {
      int danceability = song.getDanceability();
      int bpm = song.getBPM();

      boolean checkDanceability =
          (low == null || danceability >= low) && (high == null || danceability <= high);
      boolean checkSpeed = (threshold == null || bpm < threshold);

      if (checkDanceability && checkSpeed) {
        filteredSongs.add(song.getTitle());
      }
    }

    System.out.println("Songs after speed filtering: " + filteredSongs.size());
    return filteredSongs;
  }

  @Override
  public List<String> fiveMost() {
    List<String> recentSongs = new ArrayList<>();
    Iterator<Song> iterator = tree.iterator();

    System.out.println("Applying filters: Danceability(" + low + " to " + high
        + "), Speed threshold: " + threshold);

    while (iterator.hasNext() && recentSongs.size() < 5) {
      Song song = iterator.next();
      int danceability = song.getDanceability();
      int bpm = song.getBPM();

      boolean checkDanceability =
          (low == null || danceability >= low) && (high == null || danceability <= high);
      boolean checkSpeed = (threshold == null || bpm < threshold);

      if (checkDanceability && checkSpeed) {
        recentSongs.add(song.getTitle());
      }
    }

    System.out.println("Songs in fiveMost: " + recentSongs);
    return recentSongs;
  }


}
