package sample;

/**
 * A class to hold an exercise item, stores the name and caloric burn rate of the exercise
 *
 * @author ??
 *
 * @version 1.0
 */
public class ExerciseItem {
    private String name;
    private int burn_rate;

    /**
     * Constructor for Exercise Item
     *
     * @param name takes in the name of the exercise
     * @param burn_rate takes in the caloric burn of the above exercise
     */
    public ExerciseItem(String name, int burn_rate) {
        this.name = name;
        this.burn_rate = burn_rate;
    }

    /**
     * Default Constructor for exercise item
     */
    public ExerciseItem() {
        this.name = "Default";
        this.burn_rate = 0;
    }

    /**
     * Getter for name
     *
     * @return returns name
     */
    public String getName() { return name; }

    /**
     * Getter for caloric burn rate
     *
     * @return returns burn rate
     */
    public int getBurn_rate() { return burn_rate; }

    /**
     * Method to calculate the amount of calories burnt over a period of time
     *
     * @param minutes takes in the amount of time spent doing an exercise
     *
     * @return returns a double representing the amount of calories burnt over the amount of time specified
     */
    public double calculateBurn(int minutes) { return this.burn_rate*minutes; }

}

