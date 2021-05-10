package sample;

import java.time.LocalDate;

/**
 * Abstract class for representing a Goal in the ProActive app that a User can set, work towards, and achieve. Extends
 * Goal with active and completed flags and a progress counter.
 *
 * @author Samuel Scarfe
 *
 * @version 1.0
 *
 * @see Goal
 * @see IndividualGoal
 *
 * 1.0 - First working version.
 */
public abstract class UserGoal extends Goal {

    /**
     * The active status of the UserGoal.
     */
    protected boolean active;
    /**
     * The completion status of the UserGoal
     */
    protected boolean completed;
    /**
     * The current progress of the UserGoal.
     */
    protected float progress;

    /**
     * Constructs a UserGoal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public UserGoal(float target, Unit unit, LocalDate endDate) {
        super(target, unit, endDate);
        this.completed = progress >= target;
        this.active    = endDate.isAfter(LocalDate.now()) && !this.completed;
        this.progress  = 0;
    }

    /**
     * Constructs a UserGoal from a target amount, unit, end date, and progress amount. Intended for use when loading
     * goals from the database.
     * @param target   the target amount for this UserGoal.
     * @param unit     the targeted unit for this UserGoal.
     * @param endDate  the end date for this UserGoal.
     * @param progress the progress amount for this UserGoal.
     */
    public UserGoal(float target, Unit unit, LocalDate endDate, float progress) {
        super(target, unit, endDate);
        if (progress < 0) {
            throw new IllegalArgumentException();
        }

        this.progress = progress;
        this.completed = progress >= target;
        this.active    = endDate.isAfter(LocalDate.now()) && !this.completed;
    }

    /**
     * Default constructor.
     */
    protected UserGoal() {
        super();
        this.progress = -1;
        this.completed = false;
        this.active = false;
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
     * Increments the current progress by the passed amount, provided the goal is marked as ongoing. Updates the goal
     * status to completed if the target has been met.
     *
     * @param update the amount to increment progress by
     */
    public boolean updateProgress(Unit unit, float update, String username) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (update < 0) {
            throw new IllegalArgumentException();
        }

        if (this.active && !this.completed && this.unit == unit) { //if active and not completed and units match

            this.progress = this.progress + update; //update progress

            if (this.progress >= this.target) { //if completed
                this.completed = true;
                this.active = false;

                if (this instanceof GroupGoal) {
                    ((GroupGoal) this).notifyGroup(username);
                }

                NotificationHandler notificationHandler = NotificationHandler.getInstance();
                notificationHandler.displayFadeNotification("Goal Complete: " + this.toString() + ", Set another in Goals!");
            }

            return true; //Goal updated
        }
        return false; //Goal not updated
    }

    /**
     * Method to mark a goal as inactive and update its end date.
     */
    public void quitGoal() {
        if (this.completed) {
            throw new IllegalStateException("Goal is already complete");
        }

        this.endDate = LocalDate.now();
        this.active = false;
    }
}
