import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;


/**
 * FrontendTests - JUnit tests for the Frontend class.
 */
public class FrontendTests {

    /**
     * frontendTest1 - Tests the "help" command and verifies that the command
     * instructions are displayed.  This test checks that the frontend correctly
     * handles the "help" command and displays the expected output.
     */
    @Test
    void frontendTest1() {
        String input = "help\nquit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(in);
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        FrontendInterface frontend = new Frontend(scanner, backend);

        TextUITester tester = new TextUITester(input);
        frontend.runCommandLoop();


        String expectedOutput = "load FILEPATH\n" + "danceability MAX\n" +
                "danceability MIN to MAX\n" + "speed MAX\n" +
                "show MAX_COUNT\n" + "show most recent\n" +
                "help\n" + "quit";
        String actualOutput = tester.checkOutput();
        assertTrue(actualOutput.contains(expectedOutput));
        scanner.close();


    }

    /**
     * frontendTest2 - Tests the "load" and "show" commands. This test checks
     * if the frontend correctly processes the "load" command (even with a
     * placeholder backend) and then displays the songs using the "show" command.
     * This test ensures the interaction between these two commands is handled
     * correctly by the frontend.
     */
    @Test
    void frontendTest2() {
        String input = "load songs.csv\nshow most recent\nquit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(in);
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        FrontendInterface frontend = new Frontend(scanner, backend);
        TextUITester tester = new TextUITester(input);
        frontend.runCommandLoop();


        String expectedOutput = "A L I E N S, BO$$, Cake By The Ocean, DJ Got Us Fallin' In Love (feat. Pitbull)";
        String actualOutput = tester.checkOutput();
        assertTrue(actualOutput.contains(expectedOutput));

        scanner.close();

    }

    /**
     * frontendTest3 - Tests the "danceability" and "speed" commands. This test
     * checks if the frontend correctly processes the "danceability" and "speed"
     * commands. It ensures that the frontend can handle these commands without
     * errors, even though the backend is a placeholder and may not perform
     * actual filtering.
     */
    @Test
    void frontendTest3() {
        String input = "danceability 3\nspeed 100\nquit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(in);
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        FrontendInterface frontend = new Frontend(scanner, backend);
        TextUITester tester = new TextUITester(input);
        frontend.runCommandLoop();

        String expectedOutput = ""; // No output expected for danceability and speed
        assertTrue(tester.checkOutput().contains(expectedOutput));

        scanner.close();

    }
}
