import org.junit.jupiter.api.*; // JUnit 5 testing annotations
import java.util.*;             // For ArrayList and List
import java.io.*;               // For File I/O
import static org.junit.jupiter.api.Assertions.*; // For assertions

/**
 * Unit tests for Fitness Tracker functionality.
 * Verifies the logic of adding, removing, updating, file I/O, and the custom feature (average steps).
 */
class MainTest {

    private List<FitnessRecord> testRecords;

    /**
     * Runs before each test. Creates a sample dataset to use for testing.
     */
    @BeforeEach
    void setUp() {
        testRecords = new ArrayList<>();
        testRecords.add(new FitnessRecord(1, "Test User", 25, 160.0, 8000, 500.0));
        testRecords.add(new FitnessRecord(2, "User Two", 30, 180.0, 6000, 450.0));
    }

    /**
     * Verifies that adding a new FitnessRecord increases the list size.
     */
    @Test
    void testAddRecord() {
        int sizeBefore = testRecords.size();
        testRecords.add(new FitnessRecord(3, "New User", 28, 175.0, 7000, 470.0));
        assertEquals(sizeBefore + 1, testRecords.size(), "Record was not added correctly.");
    }

    /**
     * Verifies that removing a FitnessRecord deletes the correct object from the list.
     */
    @Test
    void testRemoveRecord() {
        FitnessRecord toRemove = testRecords.get(0);
        testRecords.remove(toRemove);
        assertFalse(testRecords.contains(toRemove), "Record was not removed successfully.");
    }

    /**
     * Verifies that updating fields on a FitnessRecord reflects the new values correctly.
     */
    @Test
    void testUpdateRecord() {
        FitnessRecord r = testRecords.get(0);
        r.setAge(26);                // Updating age
        r.setStepsToday(9000);       // Updating step count
        assertEquals(26, r.getAge(), "Age was not updated correctly.");
        assertEquals(9000, r.getStepsToday(), "Step count was not updated correctly.");
    }

    /**
     * Verifies that the custom feature (average step calculation) works as expected.
     */
    @Test
    void testAverageStepsCalculation() {
        // (8000 + 6000) / 2 = 7000.0
        double avg = testRecords.stream().mapToInt(FitnessRecord::getStepsToday).average().orElse(0);
        assertEquals(7000.0, avg, "Average step calculation is incorrect.");
    }

    /**
     * Verifies that a record can be loaded correctly from a file.
     */
    @Test
    void testLoadFromFile() {
        // Step 1: Write test data to a temporary file
        try (PrintWriter writer = new PrintWriter("testdata.txt")) {
            writer.println("3,Sample User,35,170.0,5000,350.0");
        } catch (IOException e) {
            fail("Failed to create test file.");
        }

        // Step 2: Load data from the file and create a new record
        List<FitnessRecord> loaded = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("testdata.txt"))) {
            String line = br.readLine(); // Only one line
            String[] parts = line.split(",");
            loaded.add(new FitnessRecord(
                    Integer.parseInt(parts[0]), parts[1],
                    Integer.parseInt(parts[2]), Double.parseDouble(parts[3]),
                    Integer.parseInt(parts[4]), Double.parseDouble(parts[5])));
        } catch (IOException | NumberFormatException e) {
            fail("Error loading file.");
        }

        // Step 3: Validate that the record was loaded correctly
        assertEquals(1, loaded.size(), "Incorrect number of records loaded.");
        assertEquals("Sample User", loaded.get(0).getFullName(), "Record data does not match.");
    }
}
