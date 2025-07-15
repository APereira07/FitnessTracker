import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages SQLite database operations for Fitness Records.
 * Provides methods for creating the table, CRUD operations, and custom queries.
 */
public class FitnessDatabaseManager {
    // The JDBC URL for connecting to the SQLite database file
    private final String dbUrl;

    /**
     * Constructor accepts the SQLite database filename and constructs the connection URL.
     * It also ensures the FitnessRecords table exists by creating it if necessary.
     *
     * @param dbFileName Filename of the SQLite database, e.g., "fitness.db"
     */
    public FitnessDatabaseManager(String dbFileName) {
        // Build the JDBC URL string for SQLite connection
        this.dbUrl = "jdbc:sqlite:" + dbFileName;

        // Create the FitnessRecords table if it does not already exist
        createTableIfNotExists();
    }

    /**
     * Creates the FitnessRecords table with the appropriate schema
     * if it does not already exist in the database.
     * The table stores ID, full name, age, weight, steps taken today, and calories burned.
     */
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS FitnessRecords (
                id INTEGER PRIMARY KEY,
                fullName TEXT NOT NULL,
                age INTEGER NOT NULL,
                weight REAL NOT NULL,
                stepsToday INTEGER NOT NULL,
                caloriesBurned REAL NOT NULL
            );
            """;

        // Use try-with-resources to ensure connection and statement are closed
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            // Execute the SQL statement to create the table if needed
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Retrieves all fitness records from the FitnessRecords table.
     * Each row is converted into a FitnessRecord object and collected into a list.
     *
     * @return List of FitnessRecord objects representing all records; empty list if none found
     */
    public List<FitnessRecord> getAllRecords() {
        List<FitnessRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM FitnessRecords ORDER BY id";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate through each row in the result set
            while (rs.next()) {
                // Create FitnessRecord object from current row data
                FitnessRecord record = new FitnessRecord(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getInt("age"),
                        rs.getDouble("weight"),
                        rs.getInt("stepsToday"),
                        rs.getDouble("caloriesBurned")
                );
                // Add the record to the list
                records.add(record);
            }

            // Debug output showing how many records were fetched
            System.out.println(records.size() + " records fetched from the database.");

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }
        return records;
    }

    /**
     * Inserts a new fitness record into the FitnessRecords table.
     *
     * @param record The FitnessRecord object containing the data to be inserted.
     */
    public void insertRecord(FitnessRecord record) {
        String sql = "INSERT INTO FitnessRecords (id, fullName, age, weight, stepsToday, caloriesBurned) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters of the prepared statement based on the record fields
            pstmt.setInt(1, record.getId());
            pstmt.setString(2, record.getFullName());
            pstmt.setInt(3, record.getAge());
            pstmt.setDouble(4, record.getWeight());
            pstmt.setInt(5, record.getStepsToday());
            pstmt.setDouble(6, record.getCaloriesBurned());

            // Execute the insertion
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting record: " + e.getMessage());
        }
    }

    /**
     * Deletes a fitness record identified by the provided ID.
     *
     * @param id The unique ID of the fitness record to delete.
     */
    public void deleteRecord(int id) {
        String sql = "DELETE FROM FitnessRecords WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the ID parameter for the delete statement
            pstmt.setInt(1, id);

            // Execute the deletion and check if any row was affected
            int affected = pstmt.executeUpdate();

            // Inform if no matching record was found
            if (affected == 0) {
                System.out.println("No record found with ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    /**
     * Updates an existing fitness record in the database.
     * All fields except the ID can be updated.
     *
     * @param record FitnessRecord object containing updated data; ID specifies the record to update.
     */
    public void updateRecord(FitnessRecord record) {
        String sql = """
            UPDATE FitnessRecords SET
                fullName = ?,
                age = ?,
                weight = ?,
                stepsToday = ?,
                caloriesBurned = ?
            WHERE id = ?;
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters to update values in the record
            pstmt.setString(1, record.getFullName());
            pstmt.setInt(2, record.getAge());
            pstmt.setDouble(3, record.getWeight());
            pstmt.setInt(4, record.getStepsToday());
            pstmt.setDouble(5, record.getCaloriesBurned());
            pstmt.setInt(6, record.getId());

            // Execute the update and check if any record was updated
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                System.out.println("No record found with ID " + record.getId());
            }

        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    /**
     * Calculates the average number of steps recorded today across all fitness records.
     *
     * @return The average steps as a double; returns 0 if no records exist or error occurs.
     */
    public double calculateAverageSteps() {
        String sql = "SELECT AVG(stepsToday) AS avgSteps FROM FitnessRecords";
        double avgSteps = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // If a result is returned, get the average steps value
            if (rs.next()) {
                avgSteps = rs.getDouble("avgSteps");
            }

        } catch (SQLException e) {
            System.out.println("Error calculating average steps: " + e.getMessage());
        }

        return avgSteps;
    }
}
