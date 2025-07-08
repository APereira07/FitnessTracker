import java.util.*;
import javax.swing.*;

/**
 * Main class for the Fitness Tracker application.
 * Users can interact with the app through a console-based menu or a GUI.
 * This version uses an SQLite database to store and retrieve fitness records
 * via the FitnessDatabaseManager class.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FitnessDatabaseManager dbManager = new FitnessDatabaseManager("fitness.db");

    public static void main(String[] args) {
        // Uncomment this line to launch GUI instead of console
        // SwingUtilities.invokeLater(FitnessTrackerGUI::new);

        boolean exit = false;
        while (!exit) {
            printMenu(); // Display menu
            int choice = getValidatedInt("Enter your choice: ", 1, 8);
            switch (choice) {
                case 1 -> loadFromDatabase();
                case 2 -> displayRecords();
                case 3 -> addRecord();
                case 4 -> removeRecord();
                case 5 -> updateRecord();
                case 6 -> customFeature();
                case 7 -> saveSampleRecords();
                case 8 -> exit = true;
                default -> System.out.println("Invalid choice."); // Just a safeguard
            }
        }
        System.out.println("Exiting... Goodbye!");
    }

    /**
     * Prints the main menu options to the user.
     */
    private static void printMenu() {
        System.out.println("""
            --- Fitness Tracker Menu (Database Mode) ---
            1. Load Records from Database
            2. Display All Records
            3. Add New Record
            4. Remove Record by ID
            5. Update Record by ID
            6. Calculate Average Steps (Custom Feature)
            7. Save Sample Records to DB
            8. Exit
            """);
    }

    /**
     * Simulates loading data from the database.
     * Actual loading happens in displayRecords method when fetching live.
     */
    private static void loadFromDatabase() {
        System.out.println("Attempting to load data from database...");
        List<FitnessRecord> records = dbManager.getAllRecords();
        System.out.println(records.size() + " records loaded.");
    }

    /**
     * Fetches all records from the database and prints them.
     */
    private static void displayRecords() {
        List<FitnessRecord> records = dbManager.getAllRecords();
        if (records.isEmpty()) {
            System.out.println("No records found in the database.");
        } else {
            System.out.println("Displaying all records:");
            for (FitnessRecord r : records) {
                System.out.println(r);
            }
        }
    }

    /**
     * Prompts user for input and inserts a new record into the database.
     */
    private static void addRecord() {
        int id = getValidatedInt("Enter ID: ");
        String name = getValidatedString("Enter full name: ");
        int age = getValidatedInt("Enter age: ");
        double weight = getValidatedDouble("Enter weight (lbs): ");
        int steps = getValidatedInt("Enter steps today: ");
        double calories = getValidatedDouble("Enter calories burned: ");

        FitnessRecord newRecord = new FitnessRecord(id, name, age, weight, steps, calories);
        dbManager.insertRecord(newRecord);
        System.out.println("Record added to database.");
    }

    /**
     * Prompts user for ID and deletes that record from the database.
     */
    private static void removeRecord() {
        int id = getValidatedInt("Enter ID to delete: ");
        dbManager.deleteRecord(id);
        System.out.println("Record deleted from database.");
    }

    /**
     * Prompts user for updated info and updates the record in the database.
     */
    private static void updateRecord() {
        int id = getValidatedInt("Enter ID to update: ");
        String name = getValidatedString("Enter new name: ");
        int age = getValidatedInt("Enter new age: ");
        double weight = getValidatedDouble("Enter new weight (lbs): ");
        int steps = getValidatedInt("Enter new steps: ");
        double calories = getValidatedDouble("Enter new calories: ");

        FitnessRecord updatedRecord = new FitnessRecord(id, name, age, weight, steps, calories);
        dbManager.updateRecord(updatedRecord);
        System.out.println("Record updated in database.");
    }

    /**
     * Calculates and displays the average steps from all records.
     */
    private static void customFeature() {
        double avgSteps = dbManager.calculateAverageSteps();
        System.out.printf("Average Steps Today: %.2f%n", avgSteps);
    }

    /**
     * Inserts a couple of sample fitness records into the database for testing.
     */
    private static void saveSampleRecords() {
        dbManager.insertRecord(new FitnessRecord(1, "John Smith", 30, 175.0, 9000, 300.5));
        dbManager.insertRecord(new FitnessRecord(2, "Jane Doe", 28, 150.5, 8500, 270.3));
        System.out.println("Sample records saved to database.");
    }

    // Input validation methods omitted for brevity, keep as is from previous code...

    private static int getValidatedInt(String prompt) {
        return getValidatedInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static int getValidatedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("Input must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer input.");
            }
        }
    }

    private static double getValidatedDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal input.");
            }
        }
    }

    private static String getValidatedString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }
}
