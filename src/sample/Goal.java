package sample;

import java.time.LocalDate;

/**
 * Abstract class for modelling a user goal in the ProActive app by its target amount, concerned units, end date,
 * current progress, and completion status.
 *
 * @author Samuel Scarfe
 *
 * @version 1.0
 *
 * 1.0 - First working version.
 */
public abstract class Goal {

    /**
     * Enum class containing the various units that could be selected for a goal. Either a dietary unit, calories or
     * protein, or an exercise unit of a specific exercise or all exercise.
     */
    public enum Unit {
        CALORIES, PROTEIN, BURN, EXERCISE, WALKING, JOGGING, RUNNING, FOOTBALL, RUGBY, YOGA,
        TENNIS, SWIMMING, CYCLING, KARATE, HIKING, CLEANING, BOXING, BILLIARDS, JUDO
    }

    /**
     * The target amount for the goal.
     */
    protected final int target;
    /**
     * The units the goal is concerned with.
     */
    protected final Unit unit;
    /**
     * The end date of the goal.
     */
    protected final LocalDate endDate;
    /**
     * The current progress of the goal.
     */
    protected int progress;
    /**
     * The active status of the goal.
     */
    protected boolean active;
    /**
     * The completion status of the goal
     */
    protected boolean completed;


    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public Goal(int target, Unit unit, LocalDate endDate) {
        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = 0;
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
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
    public Goal(int target, Unit unit, LocalDate endDate, int progress) {
        this.target    = target;
        this.unit      = unit;
        this.endDate   = endDate;
        this.progress  = progress;
        this.active    = endDate.isAfter(LocalDate.now());
        this.completed = progress >= target;
    }

    /**
     * Gets the target amount for this goal.
     *
     * @return the target amount for this goal.
     */
    public int getTarget() {
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
    public int getProgress() {
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
     * Increments the current progress by the passed amount, provided the goal is marked as ongoing. Updates the goal
     * status to completed if the target has been met.
     *
     * @param update the amount to increment progress by
     */
    public boolean updateProgress(Unit unit, int update) {
        if (this.active && !this.completed && this.unit == unit) {

            this.progress = this.progress + update;

            if (this.progress > this.target) {
                this.completed = true;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        return this.target + " of " + this.unit + " by " + this.endDate;
    }
}
