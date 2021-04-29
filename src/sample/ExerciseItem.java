package sample;

/**
 * Class to represent an exercise undertaken by the User in the Application.
 *
 * @author Evan Clayton?
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version
 * 1.1 - Added Javadoc, exceptions, and a separate JUnit test class.
 * 1.2 - Corrected bug in calculate burn methods. Previously assumed burn rate was per minute, but it is per 30 minutes.
 */

public class ExerciseItem {

    /**
     * The name of the exercise.
     */
    private String name;

    /**
     * The burn rate of the exercise in calories per minute.
     */
    private int burnRate;

    /**
     * Constructs an exercise from a name and a caloric burn rate.
     *
     * @param name The name of the exercise.
     * @param burnRate The burn rate of the exercise in calories per minute.
     */
    public ExerciseItem(String name, int burnRate) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() < 1)
        {
            throw new IllegalArgumentException();
        }
        if (burnRate < 0) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.burnRate = burnRate;
    }

    /**
     * Returns the name of this exercise.
     *
     * @return The name of this exercise.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the caloric burn rate of this exercise.
     *
     * @return The caloric burn rate of this exercise.
     */
    public int getBurnRate() {
        return burnRate;
    }

    /**
     * Returns the number of calories burned by undertaking this exercise for a provided number of minutes.
     *
     * @param minutes The number of minutes for which the exercise was undertaken.
     * @return The number of calories burned.
     */
    public int calculateBurn(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException();
        }

        return this.burnRate*minutes/30;
    }

    /**
     * Returns the number of calories burned by undertaking this exercise for one hour.
     * @return the number of calories burned by undertaking this exercise for one hour.
     */
    public int calculateBurn() {
        return this.burnRate*2;
    }
}