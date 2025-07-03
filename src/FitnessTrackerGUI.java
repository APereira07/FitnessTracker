import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FitnessTrackerGUI class builds a graphical user interface for
 * the Fitness Tracker app, providing CRUD operations and user interaction.
 */
public class FitnessTrackerGUI extends JFrame {
    private List<FitnessRecord> records;  // List to hold fitness records

    private DefaultListModel<String> listModel; // Model for JList to display records
    private JList<String> recordJList;           // JList UI component to show records
    private JTextField idField, nameField, ageField, weightField, stepsField, caloriesField;
    private JTextField filenameField;            // Input for file name
    private JLabel statusLabel;                   // Status message display

    /**
     * Constructor sets up the GUI components and event handlers.
     */
    public FitnessTrackerGUI() {
        records = new ArrayList<>();
        setTitle("Fitness Tracker");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create panel for input fields
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

        inputPanel.add(new JLabel("Filename (for load/save):"));
        filenameField = new JTextField();
        inputPanel.add(filenameField);

        add(inputPanel, BorderLayout.WEST);

        // Create buttons panel
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

        // Create list panel to display records
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

    // Load data from file specified in filenameField
    private void loadData() {
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) {
            statusLabel.setText("Please enter a filename.");
            return;
        }
        try {
            records = FileManager.loadFromFile(filename);
            statusLabel.setText("Data loaded successfully from " + filename);
            displayRecords();
        } catch (Exception e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }

    // Display all records in the list UI
    private void displayRecords() {
        listModel.clear();
        if (records.isEmpty()) {
            listModel.addElement("No records to display.");
        } else {
            for (FitnessRecord r : records) {
                listModel.addElement(r.toString());
            }
        }
        statusLabel.setText("Displaying " + records.size() + " records.");
    }

    // Add a new record from input fields with validation
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

            FitnessRecord newRecord = new FitnessRecord(id, name, age, weight, steps, calories);
            records.add(newRecord);
            statusLabel.setText("Record added.");
            displayRecords();
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input. Please enter proper data types.");
        }
    }

    // Remove record by ID from input field
    private void removeRecord() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            boolean removed = records.removeIf(r -> r.getId() == id);
            if (removed) {
                statusLabel.setText("Record removed.");
            } else {
                statusLabel.setText("Record not found.");
            }
            displayRecords();
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID input.");
        }
    }

    // Update a record identified by ID with values from input fields
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

                    statusLabel.setText("Record updated.");
                    displayRecords();
                    return;
                }
            }
            statusLabel.setText("Record not found.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input. Please enter proper data types.");
        }
    }

    // Calculate and display average steps
    private void calculateAverageSteps() {
        if (records.isEmpty()) {
            statusLabel.setText("No records to calculate.");
            return;
        }
        double avg = records.stream().mapToInt(FitnessRecord::getStepsToday).average().orElse(0);
        statusLabel.setText(String.format("Average Steps: %.2f", avg));
    }

    // Save current records to file specified in filenameField
    private void saveData() {
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) {
            statusLabel.setText("Please enter a filename.");
            return;
        }
        try {
            FileManager.saveToFile(filename, records);
            statusLabel.setText("Data saved successfully to " + filename);
        } catch (Exception e) {
            statusLabel.setText("Error saving file: " + e.getMessage());
        }
    }

    // Main method to start the GUI application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FitnessTrackerGUI::new);
    }
}
