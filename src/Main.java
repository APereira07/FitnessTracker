// Import all classes from the java.io package.
// This includes classes for file handling, such as BufferedReader, FileReader, PrintWriter, etc.
import java.io.*;

// Import all classes from the java.util package.
// This includes data structures like List, ArrayList, Scanner, and utility classes like Collections, etc.
import java.util.*;


/**
 * Main class for the Fitness Tracker application.
 * Handles the user interface, CRUD operations, and input validation.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<FitnessRecord> records = new ArrayList<>();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMenu(); // Display menu
            int choice = getValidatedInt("Enter your choice: ", 1, 8);
            switch (choice) {
                case 1 -> loadFromFile();       // Load data
                case 2 -> displayRecords();     // Display data
                case 3 -> addRecord();          // Create data
                case 4 -> removeRecord();       // Remove data
                case 5 -> updateRecord();       // Update data
                case 6 -> customFeature();      // Custom feature
                case 7 -> saveToFile();         // Save data
                case 8 -> exit = true;          // Exit app
            }
        }
        System.out.println("Exiting... Goodbye!");
    }

    // Display the application menu
    private static void printMenu() {
        System.out.println("""
            --- Fitness Tracker Menu ---
            1. Load Data from File
            2. Display All Records
            3. Add New Record
            4. Remove Record
            5. Update Record
            6. Calculate Average Steps (Custom Feature)
            7. Save Records to File
            8. Exit
        """);
    }

    // Load fitness records from a text file
    private static void loadFromFile() {
        System.out.print("Enter filename to load: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Filename cannot be empty.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            records.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                records.add(new FitnessRecord(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3]),
                        Integer.parseInt(parts[4]),
                        Double.parseDouble(parts[5])
                ));
            }
            System.out.println("Data loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Print all fitness records to the console
    private static void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No records to display.");
        } else {
            for (FitnessRecord record : records) {
                System.out.println(record);
            }
        }
    }

    // Add a new fitness record with input validation
    private static void addRecord() {
        int id = getValidatedInt("Enter ID: ");
        String name = getValidatedString("Enter full name: ");
        int age = getValidatedInt("Enter age: ");
        double weight = getValidatedDouble("Enter weight (lbs): ");
        int steps = getValidatedInt("Enter steps today: ");
        double calories = getValidatedDouble("Enter calories burned: ");

        FitnessRecord newRecord = new FitnessRecord(id, name, age, weight, steps, calories);
        records.add(newRecord);
        System.out.println("Record added:\n" + newRecord);
    }

    // Remove a record by its ID
    private static void removeRecord() {
        int id = getValidatedInt("Enter ID to remove: ");
        FitnessRecord recordToRemove = records.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
        if (recordToRemove != null) {
            records.remove(recordToRemove);
            System.out.println("Record removed.");
        } else {
            System.out.println("Record not found.");
        }
    }

    // Update all fields of a selected record
    private static void updateRecord() {
        int id = getValidatedInt("Enter ID to update: ");
        for (FitnessRecord r : records) {
            if (r.getId() == id) {
                String newName = getValidatedString("Enter new name: ");
                int newAge = getValidatedInt("Enter new age: ");
                double newWeight = getValidatedDouble("Enter new weight: ");
                int newSteps = getValidatedInt("Enter new steps: ");
                double newCalories = getValidatedDouble("Enter new calories: ");
                r.setFullName(newName);
                r.setAge(newAge);
                r.setWeight(newWeight);
                r.setStepsToday(newSteps);
                r.setCaloriesBurned(newCalories);
                System.out.println("Record updated:\n" + r);
                return;
            }
        }
        System.out.println("Record not found.");
    }

    // Custom feature: Calculate and print the average number of steps
    private static void customFeature() {
        if (records.isEmpty()) {
            System.out.println("No records to calculate.");
            return;
        }
        double avgSteps = records.stream().mapToInt(FitnessRecord::getStepsToday).average().orElse(0);
        System.out.printf("Average Steps Today: %.2f%n", avgSteps);
    }

    // Save all records to a text file
    private static void saveToFile() {
        System.out.print("Enter filename to save: ");
        String filename = scanner.nextLine().trim();
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (FitnessRecord r : records) {
                writer.printf("%d,%s,%d,%.1f,%d,%.2f%n",
                        r.getId(), r.getFullName(), r.getAge(), r.getWeight(),
                        r.getStepsToday(), r.getCaloriesBurned());
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Validates integer input from user
    private static int getValidatedInt(String prompt) {
        return getValidatedInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    // Validates integer input within a range
    private static int getValidatedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.println("Input must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer input.");
            }
        }
    }

    // Validates double input from user
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

    // Validates that user enters a non-empty string
    private static String getValidatedString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }
}
