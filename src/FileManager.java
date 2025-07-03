import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager class handles file operations for
 * loading and saving fitness records to text files.
 */
public class FileManager {

    /**
     * Loads fitness records from the given filename.
     * Expects CSV format: id,fullName,age,weight,stepsToday,caloriesBurned
     *
     * @param filename the file to read from
     * @return List of FitnessRecord objects loaded from the file
     * @throws IOException if file reading fails
     * @throws NumberFormatException if data parsing fails
     */
    public static List<FitnessRecord> loadFromFile(String filename) throws IOException, NumberFormatException {
        List<FitnessRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 6) continue; // Skip invalid lines
                int id = Integer.parseInt(parts[0].trim());
                String fullName = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                double weight = Double.parseDouble(parts[3].trim());
                int stepsToday = Integer.parseInt(parts[4].trim());
                double caloriesBurned = Double.parseDouble(parts[5].trim());

                records.add(new FitnessRecord(id, fullName, age, weight, stepsToday, caloriesBurned));
            }
        }
        return records;
    }

    /**
     * Saves the given list of fitness records to the specified file.
     * Each record is saved in CSV format.
     *
     * @param filename the file to write to
     * @param records list of FitnessRecord objects to save
     * @throws IOException if file writing fails
     */
    public static void saveToFile(String filename, List<FitnessRecord> records) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (FitnessRecord r : records) {
                pw.printf("%d,%s,%d,%.1f,%d,%.2f%n",
                        r.getId(), r.getFullName(), r.getAge(),
                        r.getWeight(), r.getStepsToday(), r.getCaloriesBurned());
            }
        }
    }
}
