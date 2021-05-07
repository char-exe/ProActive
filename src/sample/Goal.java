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
 * 1.3 - Extended to include group_id and methods to support this.
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

    protected boolean active;

    protected boolean completed;

    /**
     * If the goal was derived from a group goal it will have a group id, else this value will be 0
     */
    protected int group_id;

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
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
        this.group_id = 0;
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
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
        this.group_id = 0;
    }

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal from a group goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     * @param group_id the id of the group the goal is associated with.
     */
    public Goal(float target, Unit unit, LocalDate endDate, int group_id) {
        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = 0;
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
        this.group_id = group_id;
    }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling user instances of group goals from the database.
     *
     * @param target    the target amount of the goal.
     * @param unit      the units targeted by the goal.
     * @param endDate   the end date of the goal.
     * @param progress  the current progress of the goal.
     * @param group_id the id of the group the goal is associated with.
     */
    public Goal(float target, Unit unit, LocalDate endDate, float progress, int group_id) {
        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = progress;
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
        this.group_id = group_id;
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
     * Gets the active status of this goal.
     *
     * @return the active status of this goal.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Gets the completed status of this goal.
     *
     * @return the completed status of this goal.
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * Gets the group_id for the goal (Note this is default 0 if no group is associated with this goal).
     *
     * @return the group_id for this goal.
     */
    public int getGroup_id() { return group_id; }

    /**
     * Increments the current progress by the passed amount, provided the goal is marked as ongoing. Updates the goal
     * status to completed if the target has been met.
     *
     * @param unit unit used for the goal.
     * @param update the amount to increment progress by.
     * @param user the user object, used for finding group goal to allow notification calls
     */
    public boolean updateProgress(Unit unit, float update, User user) {
        if (this.active && !this.completed && this.unit == unit) {

            this.progress = this.progress + update;

            if (this.progress > this.target) {
                this.completed = true;
                NotificationHandler.getInstance().sendGoalNotificationWithEmail_Fade(user.getEmail(), this);
                if (this.getGroup_id() > 0){
                    //Group group = DatabaseHandler.getInstance().getGroupFromID(this.getGroup_id());
                    //group.sendGroupNotifications(user, this);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Combines the target and unit for use in sending notifications about goal completment.
     * @return the target value and unit with a space between them.
     */
    public String getMessageFragment(){
        return (getTarget() + " " + getUnit());
    }

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
