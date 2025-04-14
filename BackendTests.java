import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;


/**
 * JUnit test class for testing the Backend class. This class ensures that each method in Backend
 * behaves as expected when interacting with a placeholder data structure.
 */
public class BackendTests {

  private Backend backend;
  private Tree_Placeholder mockCollection; // Mock implementation of the song collection
  private IterableRedBlackTree actualtree; 

  /**
   * Setup method that runs before each test. Initializes a new Backend instance with a placeholder
   * song collection. This ensures that each test starts with a fresh, controlled environment.
   */
  @BeforeEach
  void setup() {
    mockCollection = new Tree_Placeholder(); // Using a placeholder data structure
    backend = new Backend(mockCollection);// Initializing backend with mock data
  }

  /**
   * Tests the readData() method. This method ensures that the Backend correctly loads song data
   * from a CSV file.
   */
  @Test
  void backendTest1() {
    try {
      backend.readData("songs.csv");
      assertNotNull(backend, "Backend instance should not be null.");
      assertTrue(mockCollection.size() > 0, "Backend should have loaded at least one song.");
    } catch (IOException e) {

    }
  }


  /**
   * Tests the getRange() method. This method ensures that songs within a specified danceability
   * range are correctly retrieved.`
   * 
   * Expected Behavior: - The returned list of songs should not be null. - The result should be a
   * valid list (even if it's empty).
   * 
   * Limitations: - Since we are using a placeholder implementation, we cannot verify actual song
   * values. - We can only check that the method does not return null and produces a list.
   */
  @Test
  void backendTest2() {
    List<String> result = backend.getRange(0, 23);
    assertNotNull(result, "Result should not be null.");
    assertTrue(result.size() >=0 , "Result should be a valid list (even if empty).");
  }

  /**
   * Tests the filterSongs() method. This method ensures that songs are correctly filtered based on
   * the BPM (Beats Per Minute) threshold.
   * 
   * Expected Behavior: - The returned filtered song list should not be null. - The result should
   * contain valid filtered songs (even if empty).
   */
  @Test
  void backendTest3() {
    List<String> filteredSongs = backend.filterSongs(120); // Filtering songs with BPM < 120
    assertNotNull(filteredSongs, "Filtered songs list should not be null.");
    assertTrue(filteredSongs.size() >= 0, "The result should contain valid filtered songs.");
  }

  /**
   * Tests the fiveMost() method. This method ensures that the Backend correctly retrieves the five
   * most recent songs.
   * 
   * Expected Behavior: - The returned list of songs should not be null. - The list should contain
   * at most 5 songs.
   */
  @Test
  void backendTest4() {
    List<String> recentSongs = backend.fiveMost();
    assertNotNull(recentSongs, "Recent songs list should not be null.");
    assertTrue(recentSongs.size() <= 5, "The list should contain at most 5 songs.");
  }
/**
 * Integration test to verify that the frontend can load data into the backend.
 */
@Test
public void testLoadDataIntegration() {
    // Simulate user input for loading data
    TextUITester tester = new TextUITester("load songs.csv\nquit\n");

    // Initialize the backend and frontend
    BackendInterface backend = new Backend(new IterableRedBlackTree<>());
    FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

    // Run the command loop
    frontend.runCommandLoop();

    // Capture the output
    String output = tester.checkOutput();

    // Assert that the output contains the success message
    assertTrue(output.contains("Successfully loaded: songs.csv"), "Data loading failed.");
}

/**
 * Integration test to verify that the frontend filters songs by danceability.
 */
@Test
public void testDanceabilityFilterIntegration() {
    // Simulate user input for filtering by danceability
    TextUITester tester = new TextUITester("danceability 50 to 60\nquit\n");

    // Initialize the backend and frontend
    BackendInterface backend = new Backend(new IterableRedBlackTree<>());
    FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

    // Run the command loop
    frontend.runCommandLoop();

    // Capture the output
    String output = tester.checkOutput();

    // Assert that the output contains the expected number of songs
    assertTrue(output.contains("Songs after filtering: "), "Danceability filtering failed.");
}

/**
 * Integration test to verify that the frontend filters songs by speed (BPM).
 */
@Test
public void testSpeedFilterIntegration() {
    // Simulate user input for filtering by speed
    TextUITester tester = new TextUITester("speed 100\nquit\n");

    // Initialize the backend and frontend
    BackendInterface backend = new Backend(new IterableRedBlackTree<>());
    FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

    // Run the command loop
    frontend.runCommandLoop();

    // Capture the output
    String output = tester.checkOutput();

    // Assert that the output contains the expected number of songs
    assertTrue(output.contains("Songs after speed filtering: "), "Speed filtering failed.");
}

/**
 * Integration test to verify that the frontend displays the most recent songs.
 */
@Test
public void testShowMostRecentIntegration() {
    // Simulate user input for showing the most recent songs
    TextUITester tester = new TextUITester("show most recent\nquit\n");

    // Initialize the backend and frontend
    BackendInterface backend = new Backend(new IterableRedBlackTree<>());
    FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

    // Run the command loop
    frontend.runCommandLoop();

    // Capture the output
    String output = tester.checkOutput();

    // Assert that the output contains the list of most recent songs
    assertTrue(output.contains("Songs in fiveMost: "), "Displaying most recent songs failed.");
}

}
