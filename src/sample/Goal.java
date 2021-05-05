package sample;

import java.time.LocalDate;

/**
 * Abstract class for modelling a user goal in the ProActive app by its target amount, concerned units, end date,
 * current progress, and completion status.
 *
 * @author Samuel Scarfe
 *
 * @version 1.2
 *
 * 1.0 - First working version.
 * 1.1 - Updated with minimum target amounts as part of automatic goal generation.
 * 1.2 - Increased units to include vitamins and minerals. Updated all units with a unit string, this is preferable
 *       over a to string due to the variable nature of translation from unit to unit string.
 */
public abstract class Goal {

    /**
     * Enum class containing the various units that could be selected for a goal. Either a dietary unit, calories or
     * protein, or an exercise unit of a specific exercise or all exercise.
     */
    public enum Unit {
        CALORIES(-1, "calories"),                   PROTEIN(-1, "grams of protein"),
        CARBS(-1, "grams of carbs"),                FIBRE(-1, "grams of fibre"),
        VITAMIN_A(-1, "micrograms of vitamin A"),   THIAMIN(-1, "milligrams of thiamin"),
        RIBOFLAVIN(-1, "milligrams of riboflavin"), NIACIN(-1, "milligrams of niacin"),
        VITAMIN_B6(-1, "milligrams of vitamin B6"), VITAMIN_B12(-1, "micrograms of vitamin B12"),
        FOLATE(-1, "micrograms of folate"),         VITAMIN_C(-1, "milligrams of vitamin C"),
        VITAMIN_D(-1, "micrograms of vitamin D"),   IRON(-1, "milligrams of iron"),
        CALCIUM(-1, "milligrams of calcium"),       MAGNESIUM(-1, "milligrams of magnesium"),
        POTASSIUM(-1, "milligrams of potassium"),   ZINC(-1, "milligrams of zinc"),
        COPPER(-1, "milligrams of copper"),         IODINE(-1, "micrograms of iodine"),
        SELENIUM(-1, "micrograms of selenium"),     PHOSPHORUS(-1, "milligrams of phosphorus"),
        CHLORIDE(-1, "milligrams of chloride"),     SODIUM(-1, "milligrams of sodium"),
        BURNED(-1, "calories burned"),              EXERCISE(30, "minutes of exercise"),
        WALKING(30, "minutes of walking"),          JOGGING(30, "minutes of jogging"),
        RUNNING(30, "minutes of running"),          FOOTBALL(30, "minutes of football"),
        RUGBY(30, "minutes of rugby"),              YOGA(30, "minutes of yoga"),
        TENNIS(30, "minutes of tennis"),            SWIMMING(30, "minutes of swimming"),
        CYCLING(30, "minutes of cycling"),          KARATE(30, "minutes of karate"),
        HIKING(30, "minutes of hiking"),            CLEANING(30, "minutes of cleaning"),
        BOXING(30, "minutes of boxing"),            BILLIARDS(30, "minutes of billiards"),
        JUDO(30, "minutes of judo");

        /**
         * The minimum target amount set by the system for a daily goal of this unit.
         */
        private final int minimum;

        private final String unitString;

        /**
         * Constructs a Unit with its minimum value.
         * @param minimum the minimum target amount set by the system for a daily got of this unit.
         */
        Unit(int minimum, String unitString) {
            this.minimum = minimum;
            this.unitString = unitString;
        }

        /**
         * Gets the minimum value for this unit.
         *
         * @return the minimum value for this unit.
         */
        public int getMinimum() {
            return this.minimum;
        }

        public String getUnitString() {
            return unitString;
        }
    }

    /**
     * The target amount for the goal.
     */
    protected float target;
    /**
     * The units the goal is concerned with.
     */
    protected Unit unit;
    /**
     * The end date of the goal.
     */
    protected LocalDate endDate;
    /**
     * The current progress of the goal.
     */
    protected float progress;



    protected Goal() {
        this.target = -1;
        this.unit = null;
        this.endDate = null;
        this.progress = -1;
    }

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public Goal(float target, Unit unit, LocalDate endDate) {
        checkConstructorInputs(target, unit, endDate);

        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = 0;
    }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling user goals from the database.
     *
     * @param target    the target amount of the goal.
     * @param unit      the units targeted by the goal.
     * @param endDate   the end date of the goal.
     * @param progress  the current progress of the goal.
     */
    public Goal(float target, Unit unit, LocalDate endDate, float progress) {
        checkConstructorInputs(target, unit, endDate, progress);
        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = progress;
    }

    /**
     * Gets the target amount for this goal.
     *
     * @return the target amount for this goal.
     */
    public float getTarget() {
        return this.target;
    }

    /**
     * Gets the units for this goal.
     *
     * @return the units for this goal.
     */
    public Unit getUnit() {
        return this.unit;
    }

    /**
     * Gets the end date for this goal.
     *
     * @return the end date for this goal.
     */
    public LocalDate getEndDate() {
        return LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
    }

    /**
     * Gets the progress amount for this goal.
     *
     * @return the progress amount for this goal.
     */
    public float getProgress() {
        return this.progress;
    }

    /**
     * Method for creating a String representation of this Goal.
     *
     * @return a String representing this Goal.
     */
    public String toString() {

        if (this.unit.getMinimum() > -1) { //If this is a fitness goal
            //Strip decimal part of target
            return (int) this.target + " " + this.unit.getUnitString() + " by " + this.endDate;
        }

        return this.target + " " + this.unit.getUnitString() + " by " + this.endDate;
    }

    /**
     * Private helper method for checking constructor inputs for the short constructor.
     *
     * @param target the target for this Goal.
     * @param unit the unit for this Goal.
     * @param endDate the endDate for this Goal.
     */
    private void checkConstructorInputs(float target, Unit unit, LocalDate endDate) {
        if (target <= 0) {
            throw new IllegalArgumentException();
        }
        if (unit == null) {
            throw new NullPointerException();
        }
        if (endDate == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Private helper method for checking constructor inputs for the long constructor.
     *
     * @param target the target for this Goal.
     * @param unit the unit for this Goal.
     * @param endDate the endDate for this Goal.
     * @param progress the progress for this Goal.
     */
    private void checkConstructorInputs(float target, Unit unit, LocalDate endDate, float progress) {
        checkConstructorInputs(target, unit, endDate);
        if (progress < 0) {
            throw new IllegalArgumentException();
        }
    }
}
