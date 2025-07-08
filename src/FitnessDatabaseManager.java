import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages SQLite database operations for Fitness Records.
 */
public class FitnessDatabaseManager {
    private final String dbUrl;

    /**
     * Constructor accepts the SQLite DB filename and initializes connection URL.
     * @param dbFileName filename of the SQLite database, e.g., "fitness.db"
     */
    public FitnessDatabaseManager(String dbFileName) {
        this.dbUrl = "jdbc:sqlite:" + dbFileName;
        createTableIfNotExists();
    }

    /**
     * Creates the fitness_records table if it doesn't already exist.
     */
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS fitness_records (
                id INTEGER PRIMARY KEY,
                fullName TEXT NOT NULL,
                age INTEGER NOT NULL,
                weight REAL NOT NULL,
                stepsToday INTEGER NOT NULL,
                caloriesBurned REAL NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Retrieves all fitness records from the database.
     * Includes debug prints to confirm correct retrieval.
     * @return List of FitnessRecord objects (empty if none found)
     */
    public List<FitnessRecord> getAllRecords() {
        List<FitnessRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM fitness_records ORDER BY id";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                FitnessRecord record = new FitnessRecord(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getInt("age"),
                        rs.getDouble("weight"),
                        rs.getInt("stepsToday"),
                        rs.getDouble("caloriesBurned")
                );
                records.add(record);
            }

            System.out.println(records.size() + " records fetched from the database."); // Debug print

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }
        return records;
    }

    /**
     * Inserts a new fitness record into the database.
     * @param record the FitnessRecord to add
     */
    public void insertRecord(FitnessRecord record) {
        String sql = "INSERT INTO fitness_records (id, fullName, age, weight, stepsToday, caloriesBurned) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getId());
            pstmt.setString(2, record.getFullName());
            pstmt.setInt(3, record.getAge());
            pstmt.setDouble(4, record.getWeight());
            pstmt.setInt(5, record.getStepsToday());
            pstmt.setDouble(6, record.getCaloriesBurned());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting record: " + e.getMessage());
        }
    }

    /**
     * Deletes a record from the database by its ID.
     * @param id the ID of the record to delete
     */
    public void deleteRecord(int id) {
        String sql = "DELETE FROM fitness_records WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();

            if (affected == 0) {
                System.out.println("No record found with ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    /**
     * Updates an existing fitness record by ID.
     * @param record FitnessRecord containing updated data
     */
    public void updateRecord(FitnessRecord record) {
        String sql = """
            UPDATE fitness_records SET
                fullName = ?,
                age = ?,
                weight = ?,
                stepsToday = ?,
                caloriesBurned = ?
            WHERE id = ?;
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getFullName());
            pstmt.setInt(2, record.getAge());
            pstmt.setDouble(3, record.getWeight());
            pstmt.setInt(4, record.getStepsToday());
            pstmt.setDouble(5, record.getCaloriesBurned());
            pstmt.setInt(6, record.getId());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                System.out.println("No record found with ID " + record.getId());
            }

        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    /**
     * Calculates the average number of steps across all records.
     * @return the average steps as double, or 0 if no records
     */
    public double calculateAverageSteps() {
        String sql = "SELECT AVG(stepsToday) AS avgSteps FROM fitness_records";
        double avgSteps = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                avgSteps = rs.getDouble("avgSteps");
            }

        } catch (SQLException e) {
            System.out.println("Error calculating average steps: " + e.getMessage());
        }

        return avgSteps;
    }
}
