import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * FitnessTrackerGUI class builds a graphical user interface for
 * the Fitness Tracker app, providing CRUD operations and user interaction.
 * Now fully integrated with SQLite database via FitnessDatabaseManager.
 */
public class FitnessTrackerGUI extends JFrame {
    private FitnessDatabaseManager dbManager;    // Database manager for CRUD operations
    private List<FitnessRecord> records;         // Cached list of fitness records

    private DefaultListModel<String> listModel;  // Model for JList to display records
    private JList<String> recordJList;            // JList UI component to show records

    private JTextField idField, nameField, ageField, weightField, stepsField, caloriesField;
    private JTextField filenameField;             // Input for DB filename (e.g., fitness.db)
    private JLabel statusLabel;                    // Status message display

    /**
     * Constructor sets up the GUI components and event handlers.
     * Initializes FitnessDatabaseManager with the DB filename entered by the user.
     */
    public FitnessTrackerGUI() {
        setTitle("Fitness Tracker");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Weight (lbs):"));
        weightField = new JTextField();
        inputPanel.add(weightField);

        inputPanel.add(new JLabel("Steps Today:"));
        stepsField = new JTextField();
        inputPanel.add(stepsField);

        inputPanel.add(new JLabel("Calories Burned:"));
        caloriesField = new JTextField();
        inputPanel.add(caloriesField);

        inputPanel.add(new JLabel("Database Filename (e.g., fitness.db):"));
        filenameField = new JTextField("fitness.db");  // default DB filename
        inputPanel.add(filenameField);

        add(inputPanel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        JButton loadButton = new JButton("Load Data");
        JButton displayButton = new JButton("Display Records");
        JButton addButton = new JButton("Add Record");
        JButton removeButton = new JButton("Remove Record");
        JButton updateButton = new JButton("Update Record");
        JButton avgStepsButton = new JButton("Calculate Avg Steps");
        JButton saveButton = new JButton("Save Data");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(loadButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(avgStepsButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.EAST);

        // List panel to display records
        listModel = new DefaultListModel<>();
        recordJList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(recordJList);
        add(scrollPane, BorderLayout.CENTER);

        // Status label for feedback
        statusLabel = new JLabel("Welcome to Fitness Tracker!");
        add(statusLabel, BorderLayout.SOUTH);

        // Button actions
        loadButton.addActionListener(e -> loadData());
        displayButton.addActionListener(e -> displayRecords());
        addButton.addActionListener(e -> addRecord());
        removeButton.addActionListener(e -> removeRecord());
        updateButton.addActionListener(e -> updateRecord());
        avgStepsButton.addActionListener(e -> calculateAverageSteps());
        saveButton.addActionListener(e -> saveData());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    /**
     * Initializes database manager and loads all records from the database.
     * Uses the filename from filenameField to connect.
     */
    private void loadData() {
        String dbFileName = filenameField.getText().trim();
        if (dbFileName.isEmpty()) {
            statusLabel.setText("Please enter a database filename.");
            return;
        }

        // Initialize the database manager with the given file
        dbManager = new FitnessDatabaseManager(dbFileName);

        // Fetch all records from DB
        records = dbManager.getAllRecords();

        statusLabel.setText("Data loaded successfully from database: " + dbFileName);
        displayRecords();
    }

    /**
     * Displays all loaded records in the JList UI.
     */
    private void displayRecords() {
        listModel.clear();
        if (records == null || records.isEmpty()) {
            listModel.addElement("No records to display.");
            statusLabel.setText("No records found.");
        } else {
            for (FitnessRecord r : records) {
                listModel.addElement(r.toString());
            }
            statusLabel.setText("Displaying " + records.size() + " records.");
        }
    }

    /**
     * Adds a new record to the local list (does NOT immediately save to DB).
     * Input validation is performed.
     */
    private void addRecord() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int steps = Integer.parseInt(stepsField.getText().trim());
            double calories = Double.parseDouble(caloriesField.getText().trim());

            if (name.isEmpty()) {
                statusLabel.setText("Name cannot be empty.");
                return;
            }

            // Check for duplicate ID
            for (FitnessRecord r : records) {
                if (r.getId() == id) {
                    statusLabel.setText("ID already exists. Use Update instead.");
                    return;
                }
            }

            FitnessRecord newRecord = new FitnessRecord(id, name, age, weight, steps, calories);
            records.add(newRecord);
            statusLabel.setText("Record added locally. Remember to save changes.");
            displayRecords();

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input. Please enter proper data types.");
        }
    }

    /**
     * Removes a record from the local list by ID.
     * Does NOT immediately save changes to DB.
     */
    private void removeRecord() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            boolean removed = records.removeIf(r -> r.getId() == id);
            if (removed) {
                statusLabel.setText("Record removed locally. Remember to save changes.");
            } else {
                statusLabel.setText("Record not found.");
            }
            displayRecords();
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID input.");
        }
    }

    /**
     * Updates a record in the local list identified by ID.
     * Does NOT immediately save changes to DB.
     */
    private void updateRecord() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            for (FitnessRecord r : records) {
                if (r.getId() == id) {
                    String name = nameField.getText().trim();
                    int age = Integer.parseInt(ageField.getText().trim());
                    double weight = Double.parseDouble(weightField.getText().trim());
                    int steps = Integer.parseInt(stepsField.getText().trim());
                    double calories = Double.parseDouble(caloriesField.getText().trim());

                    if (name.isEmpty()) {
                        statusLabel.setText("Name cannot be empty.");
                        return;
                    }

                    r.setFullName(name);
                    r.setAge(age);
                    r.setWeight(weight);
                    r.setStepsToday(steps);
                    r.setCaloriesBurned(calories);

                    statusLabel.setText("Record updated locally. Remember to save changes.");
                    displayRecords();
                    return;
                }
            }
            statusLabel.setText("Record not found.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input. Please enter proper data types.");
        }
    }

    /**
     * Calculates average steps from records currently loaded in memory
     * and displays the result.
     */
    private void calculateAverageSteps() {
        if (records == null || records.isEmpty()) {
            statusLabel.setText("No records to calculate.");
            return;
        }
        double avg = records.stream().mapToInt(FitnessRecord::getStepsToday).average().orElse(0);
        statusLabel.setText(String.format("Average Steps: %.2f", avg));
    }

    /**
     * Saves all records from the local list to the database in a batch operation.
     */
    private void saveData() {
        if (dbManager == null) {
            statusLabel.setText("Please load data from a database first.");
            return;
        }

        if (records == null || records.isEmpty()) {
            statusLabel.setText("No records to save.");
            return;
        }

        dbManager.saveAllRecords(records);
        statusLabel.setText("All records saved successfully to database.");
    }

    /**
     * Main method to start the GUI application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FitnessTrackerGUI::new);
    }
}
