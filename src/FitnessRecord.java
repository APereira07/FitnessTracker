/**
 * FitnessRecord class represents a single fitness tracking entry for a user.
 * It includes details such as ID, full name, age, weight, steps taken, and calories burned.
 */
public class FitnessRecord {
    private int id;
    private String fullName;
    private int age;
    private double weight;
    private int stepsToday;
    private double caloriesBurned;

    // Constructor to initialize a fitness record
    public FitnessRecord(int id, String fullName, int age, double weight, int stepsToday, double caloriesBurned) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.weight = weight;
        this.stepsToday = stepsToday;
        this.caloriesBurned = caloriesBurned;
    }

    // Getter and setter methods
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public int getStepsToday() { return stepsToday; }
    public void setStepsToday(int stepsToday) { this.stepsToday = stepsToday; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(double caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    // String representation for printing record details
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Weight: %.1f lbs | Steps: %d | Calories: %.2f",
                id, fullName, age, weight, stepsToday, caloriesBurned);
    }
}
