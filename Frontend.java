import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {

  Scanner in;
  BackendInterface backend;

  private final int MAX_SONGS = 999;

  /**
   * Default constructor for the frontend class
   * 
   * @param in      The scanner object that takes in user input
   * @param backend The backend that calculates data.
   */
  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }

  /**
   * Displays instructions for the syntax of user commands. And then repeatedly gives the user an
   * opportunity to issue new commands until they enter "quit". Uses the executeSingleCommand method
   * below to parse and run each command entered by the user. If the backend ever throws any
   * exceptions, they should be caught here and reported to the user. The user should then continue
   * to be able to issue subsequent commands until they enter "quit". This method must use the
   * scanner passed into the constructor to read commands input by the user.
   */
  @Override
  public void runCommandLoop() {
    displayCommandInstructions();

    String command;
    do {
      System.out.print("Enter command: "); //
      command = in.nextLine();

      if (command.equals("quit")) {
        break;
      }

      try {
        executeSingleCommand(command);
      } catch (Exception e) {
        System.err.println("Error processing command: " + e.getMessage());
        // e.printStackTrace();
      }

    } while (true);
  }

  /**
   * Displays instructions for the user to understand the syntax of commands that they are able to
   * enter. This should be displayed once from the command loop, before the first user command is
   * read in, and then later in response to the user entering the command: help.
   *
   * The lowercase words in the following examples are keywords that the user must match exactly in
   * their commands, while the upper case words are placeholders for arguments that the user can
   * specify. The following are examples of valid command syntax that your frontend should be able
   * to handle correctly.
   *
   * load FILEPATH danceability MAX danceability MIN to MAX speed MAX show MAX_COUNT show most
   * recent help quit
   */
  @Override
  public void displayCommandInstructions() {
    System.out.println();
    System.out.println("load FILEPATH");
    System.out.println("danceability MAX");
    System.out.println("danceability MIN to MAX");
    System.out.println("speed MAX");
    System.out.println("show MAX_COUNT");
    System.out.println("show most recent");
    System.out.println("help");
    System.out.println("quit");
    System.out.println();
  }

  /**
   * This method takes a command entered by the user as input. It parses that command to determine
   * what kind of command it is, and then makes use of the backend (which was passed to the
   * constructor) to update the state of that backend. When a show or help command are issued, this
   * method prints the appropriate results to standard out. When a command does not follow the
   * syntax rules described above, this method should print out an error message that describes at
   * least one defect in the syntax of the provided command argument.
   *
   * Some notes on the expected behavior of the different commands: load: results in backend loading
   * data from specified path danceability: updates backend's range of songs to return should not
   * result in any songs being displayed speed: updates backend's filter threshold should not result
   * in any songs being displayed show: displays list of songs with currently set thresholds
   * MAX_COUNT: argument limits the number of song titles displayed to the first MAX_COUNT in the
   * list returned from backend most recent: argument displays results returned from the backend's
   * fiveMost method help: displays command instructions quit: ends this program (handled by
   * runCommandLoop method above) (do NOT use System.exit(), as this will interfere with tests)
   *
   * @param command The command that the function must follow
   */
  @Override
  public void executeSingleCommand(String command) {
    String[] commandParts = command.strip().split("\\s+");
    String commandName = commandParts[0];

    switch (commandName) {
      case "load":
        if (commandParts.length == 2) {
          try {
            backend.readData(commandParts[1]);
            System.out.println("Successfully loaded: " + commandParts[1]);
          } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
          }
        } else {
          System.out.println("Error: Load command requires a file name.");
        }
        break;
          
case "danceability":
    if (commandParts.length == 2) {
        // Single argument provided; treat it as the maximum danceability
        Integer high = null;
        try {
            if (!commandParts[1].equals("null")) {
                high = Integer.parseInt(commandParts[1]);
            }
            List<String> filteredSongs = backend.getRange(null, high);
            printSongs(filteredSongs, filteredSongs.size());
        } catch (NumberFormatException e) {
            System.out.println("Error: Danceability value must be an integer or 'null'.");
        }
    } else if (commandParts.length == 4 && commandParts[2].equals("to")) {
        // Two arguments provided in the format "MIN to MAX"
        Integer low = null, high = null;
        try {
            if (!commandParts[1].equals("null")) {
                low = Integer.parseInt(commandParts[1]);
            }
            if (!commandParts[3].equals("null")) {
                high = Integer.parseInt(commandParts[3]);
            }
            List<String> filteredSongs = backend.getRange(low, high);
            printSongs(filteredSongs, filteredSongs.size());
        } catch (NumberFormatException e) {
            System.out.println("Error: Danceability values must be integers or 'null'.");
        }
    } else {
        System.out.println("Error: Invalid syntax for danceability command.");
    }
    break;


      case "speed":
        if (commandParts.length == 2) {
          Integer threshold = null;
          try {
            if (!commandParts[1].equals("null")) {
              threshold = Integer.parseInt(commandParts[1]);
            }
            List<String> speedFilteredSongs = backend.filterSongs(threshold);
            printSongs(speedFilteredSongs, speedFilteredSongs.size());
          } catch (NumberFormatException e) {
            System.out.println("Error: Speed threshold must be an integer or 'null'.");
          }
        } else {
          System.out.println("Error: speed command requires one argument.");
        }
        break;

      case "show":
        if (commandParts.length <= 3) {
          if (commandParts.length == 3 && commandParts[1].equals("most")
              && commandParts[2].equals("recent")) {
            List<String> mostRecent = backend.fiveMost();
            printSongs(mostRecent, mostRecent.size()); 
          } else if (commandParts.length == 1) {
            List<String> filteredSongs = backend.getRange(null, null);
            printSongs(filteredSongs, MAX_SONGS);
          } else if (commandParts.length == 2) {
            try {
              int count = Integer.parseInt(commandParts[1]);
              List<String> filteredSongs = backend.getRange(null, null);
              printSongs(filteredSongs, count);
            } catch (NumberFormatException e) {
              System.out.println("Error: Invalid number of arguments for the show command.");
            }
          } else {
            System.out.println("Error: Invalid number of arguments for show command.");
          }
        }
        break;

      case "help":
        displayCommandInstructions();
        break;

      default:
        System.out.println("Error: Invalid command.");
    }
  }


  /**
   * Private helper method to print songs in the Execute command.
   * 
   * @param songs The list of songs
   * @param count The number of songs to be printed.
   */
  private void printSongs(List<String> songs, int count) {
    if (songs.isEmpty()) {
      System.out.println("No songs found matching the criteria.");
      return;
    }

    for (int i = 0; i < Math.min(count, songs.size()); i++) {
      System.out.println((i + 1) + ". " + songs.get(i)); 
    }
  }
}
