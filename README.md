# FitnessTracker

Phase 1 – Menu System and Core Logic

The initial phase focused on designing a working menu system and implementing the basic CRUD logic for managing fitness records.

The application was structured around:

Main.java: the main application and menu controller.
FitnessRecord.java: the data model class representing each fitness entry.
Users can:

Add, view, update, and delete fitness records.
View all data in a readable format.
Calculate the average number of steps across all records (custom feature).
All inputs are validated (name, age, weight, steps, and calories) to prevent crashes or incorrect data entry. The console menu loops until the user chooses to exit and does not accept invalid options.

Phase 2 – Unit Testing

In Phase 2, unit testing was added using JUnit 5 in a separate class named MainTest.java.

Tests included:

Verifying that records can be added, removed, and updated.
Ensuring the average steps calculation returns accurate results.
Simulating file loading and checking data parsing behavior.
Each test is isolated with setup methods and assertions to verify correct logic execution.

Phase 3 – Graphical User Interface (GUI)

Phase 3 introduced a fully functional GUI using Java Swing, replacing the console-based menu.

FitnessTrackerGUI.java: contains all GUI elements and logic.
FileManager.java: separates file operations from the main logic for modularity.
Features of the GUI:

Interactive buttons for each action (load, save, add, update, delete, calculate average).
Clean layout and intuitive user experience.
Input validation continues to ensure robust behavior.
A sample data file (testdata.txt) with 20+ records is included for demonstration.
The application was compiled into FitnessTracker.jar with GUI support.

Phase 4 – Database Integration (SQLite)

In the final phase, the application was upgraded to support persistent data storage via SQLite.

FitnessDatabaseManager.java: encapsulates all SQL logic using JDBC (Java Database Connectivity).
Main.java: updated to perform database CRUD operations using the manager class.
Database Features:

The user is prompted to specify the SQLite .db file, avoiding hardcoded paths.
The database table (fitness_records) is created if it does not already exist.
Users can perform all CRUD operations with data saved directly to the SQLite database.
The custom feature (average steps calculation) is performed via SQL's AVG() function.
Sample records are inserted and fetched to verify connectivity, and debug messages confirm successful operations. The application gracefully handles invalid inputs or connection issues. A properly configured database file (fitness.db) is included with the project submission.
